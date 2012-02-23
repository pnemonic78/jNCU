/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 * 
 * http://sourceforge.net/projects/jncu
 * 
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import java.util.concurrent.TimeoutException;

import javax.crypto.Cipher;

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.CDPacket;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.crypto.DESNewton;
import net.sf.jncu.newton.NewtonError;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.NewtonInfo;
import net.sf.jncu.protocol.v1_0.query.DResult;

/**
 * Manage the docking protocol.
 * 
 * @author moshew
 */
public class DockingProtocol<P extends CDPacket> implements DockCommandListener {

	protected static final int ERROR_COMM_TIMEDOUT = -10021;
	protected static final int ERROR_DISCONNECTED = -16005;

	private final CDPipe<P> pipe;
	/** Internal state. */
	private DockingState state = DockingState.NONE;
	/** Newton information. */
	private static NewtonInfo info;
	/** Protocol version. */
	private int protocolVersion = DRequestToDock.PROTOCOL_VERSION;
	/** The password sent by the Desktop. */
	private transient long challengeDesktop;
	/** The ciphered password sent by the Desktop. */
	private transient long challengeDesktopCiphered;
	/** The password sent by the Newton. */
	private transient long challengeNewton;
	/** The ciphered password sent by the Newton. */
	private transient long challengeNewtonCiphered;
	/** The Newton DES cryptography. */
	private DESNewton crypto;
	/**
	 * The password exchange can occur up to 3 times before the desktop gives
	 * up.
	 */
	private static final int MAX_PASSWORD_ATTEMPTS = 3;

	private int challengePasswordAttempt = 0;

	/**
	 * Creates a new docker.
	 * 
	 * @param pipe
	 *            the pipe.
	 */
	public DockingProtocol(CDPipe<P> pipe) {
		super();
		if (pipe == null)
			throw new IllegalArgumentException("pipe required");
		this.pipe = pipe;
		pipe.addCommandListener(this);
		this.crypto = new DESNewton();
		crypto.init(Cipher.ENCRYPT_MODE);
		info = null;
	}

	/**
	 * Get the state.
	 * 
	 * @return the state.
	 */
	public DockingState getState() {
		return state;
	}

	/**
	 * Set the state.
	 * 
	 * @param state
	 *            the state.
	 * @throws PipeDisconnectedException
	 *             if the state is set to disconnected.
	 */
	public void setState(DockingState state) throws PipeDisconnectedException {
		validateState(this.state, state);
		this.state = state;
	}

	/**
	 * Check that the state transition is valid.
	 * 
	 * @param oldState
	 *            the old state.
	 * @param newState
	 *            the new state.
	 * @throws BadPipeStateException
	 *             if the transition is invalid.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 */
	protected void validateState(DockingState oldState, DockingState newState) throws BadPipeStateException, PipeDisconnectedException {
		if (oldState == DockingState.DISCONNECTED) {
			if (newState != DockingState.DISCONNECTED) {
				throw new PipeDisconnectedException();
			}
		}
		// Only move the previous state to the next state.
		int compare = newState.compareTo(oldState);
		if (compare < 0) {
			// Maybe Newton sent us same packet again until we acknowledged it?
			return;
		}
		if (compare > 1) {
			throw new BadPipeStateException("bad state from " + oldState + " to " + newState);
		}
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		String cmd = command.getCommand();

		try {
			switch (state) {
			case HANDSHAKE_RTDK_LISTEN:
			case HANDSHAKE_RTDK_RECEIVED:
				setState(DockingState.HANDSHAKE_RTDK_RECEIVED);
				if (DResult.COMMAND.equals(cmd)) {
					handleError((DResult) command);
				} else if (!DRequestToDock.COMMAND.equals(cmd)) {
					throw new BadPipeStateException("expected command '" + DRequestToDock.COMMAND + "', but received '" + cmd + "'");
				}

				DInitiateDocking cmdInitiateDocking = new DInitiateDocking();
				cmdInitiateDocking.setSession(DInitiateDocking.SESSION_SETTING_UP);
				setState(DockingState.HANDSHAKE_DOCK_SENDING);
				pipe.write(cmdInitiateDocking);
				break;
			case HANDSHAKE_NAME_LISTEN:
				if (DResult.COMMAND.equals(cmd)) {
					handleError((DResult) command);
				} else if (DRequestToDock.COMMAND.equals(cmd)) {
					// Ignore duplicates.
					break;
				} else if (!DNewtonName.COMMAND.equals(cmd)) {
					throw new BadPipeStateException("expected command '" + DNewtonName.COMMAND + "', but received '" + cmd + "'");
				}
				setState(DockingState.HANDSHAKE_NAME_RECEIVED);
			case HANDSHAKE_NAME_RECEIVED:
				DNewtonName cmdNewtonName = (DNewtonName) command;
				info = cmdNewtonName.getInformation();
				DDesktopInfo cmdDesktopInfo = new DDesktopInfo();
				this.challengeDesktop = cmdDesktopInfo.getEncryptedKey();
				this.challengeDesktopCiphered = crypto.cipher(challengeDesktop);
				setState(DockingState.HANDSHAKE_DINFO_SENDING);
				pipe.write(cmdDesktopInfo);
				break;
			case HANDSHAKE_NINFO_LISTEN:
				if (DResult.COMMAND.equals(cmd)) {
					handleError((DResult) command);
				} else if (DNewtonName.COMMAND.equals(cmd)) {
					// Ignore duplicates.
					break;
				} else if (!DNewtonInfo.COMMAND.equals(cmd)) {
					throw new BadPipeStateException("expected command '" + DNewtonInfo.COMMAND + "', but received '" + cmd + "'");
				}
				setState(DockingState.HANDSHAKE_NINFO_RECEIVED);
			case HANDSHAKE_NINFO_RECEIVED:
				DNewtonInfo cmdNewtonInfo = (DNewtonInfo) command;
				this.protocolVersion = cmdNewtonInfo.getProtocolVersion();
				this.challengeNewton = cmdNewtonInfo.getEncryptedKey();
				this.challengeNewtonCiphered = crypto.cipher(challengeNewton);

				DWhichIcons cmdWhichIcons = new DWhichIcons();
				cmdWhichIcons.setIcons(DWhichIcons.ALL);
				setState(DockingState.HANDSHAKE_ICONS_SENDING);
				pipe.write(cmdWhichIcons);
				break;
			case HANDSHAKE_ICONS_RESULT_LISTEN:
				if (DNewtonInfo.COMMAND.equals(cmd)) {
					// Ignore duplicates.
					break;
				} else if (!DResult.COMMAND.equals(cmd)) {
					throw new BadPipeStateException("expected command '" + DResult.COMMAND + "', but received '" + cmd + "'");
				}
				setState(DockingState.HANDSHAKE_ICONS_RESULT_RECEIVED);
			case HANDSHAKE_ICONS_RESULT_RECEIVED:
				DResult cmdResult = (DResult) command;
				if (cmdResult.getErrorCode() == DResult.OK) {
					DSetTimeout cmdSetTimeout = new DSetTimeout();
					cmdSetTimeout.setTimeout(pipe.getTimeout());
					setState(DockingState.HANDSHAKE_TIMEOUT_SENDING);
					pipe.write(cmdSetTimeout);
				} else {
					handleError((DResult) command);
				}
				break;
			case HANDSHAKE_PASS_LISTEN:
				if (DResult.COMMAND.equals(cmd)) {
					handleError((DResult) command);
				} else if (!DPassword.COMMAND.equals(cmd)) {
					throw new BadPipeStateException("expected command '" + DPassword.COMMAND + "', but received '" + cmd + "'");
				}
				setState(DockingState.HANDSHAKE_PASS_RECEIVED);
			case HANDSHAKE_PASS_RECEIVED:
				DPassword cmdPassword = (DPassword) command;

				if (cmdPassword.getEncryptedKey() == challengeDesktopCiphered) {
					DPassword cmdPasswordReply = new DPassword();
					cmdPasswordReply.setEncryptedKey(challengeNewtonCiphered);
					setState(DockingState.HANDSHAKE_PASS_SENDING);
					pipe.write(cmdPasswordReply);
				} else {
					int error = DPassword.ERROR_BAD_PASSWORD;
					challengePasswordAttempt++;
					if (challengePasswordAttempt < MAX_PASSWORD_ATTEMPTS) {
						error = DPassword.ERROR_RETRY_PASSWORD;
					}
					DResult cmdError = new DResult();
					cmdError.setErrorCode(error);
					pipe.write(cmdError);
				}
				break;
			case HANDSHAKE_DONE:
				break;
			case DISCONNECTED:
				throw new PipeDisconnectedException();
			default:
				throw new BadPipeStateException("bad state " + state);
			}
		} catch (BadPipeStateException be) {
			be.printStackTrace();
		} catch (PipeDisconnectedException pde) {
			pde.printStackTrace();
		} catch (CDILNotInitializedException ce) {
			ce.printStackTrace();
		} catch (PlatformException pe) {
			pe.printStackTrace();
		} catch (TimeoutException te) {
			te.printStackTrace();
		}
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		String cmd = command.getCommand();

		try {
			switch (state) {
			case HANDSHAKE_DOCK_SENDING:
				if (DInitiateDocking.COMMAND.equals(cmd)) {
					setState(DockingState.HANDSHAKE_DOCK_SENT);
					setState(DockingState.HANDSHAKE_NAME_LISTEN);
				}
				break;
			case HANDSHAKE_DINFO_SENDING:
				if (DDesktopInfo.COMMAND.equals(cmd)) {
					setState(DockingState.HANDSHAKE_DINFO_SENT);
					setState(DockingState.HANDSHAKE_NINFO_LISTEN);
				}
				break;
			case HANDSHAKE_ICONS_SENDING:
				if (DWhichIcons.COMMAND.equals(cmd)) {
					setState(DockingState.HANDSHAKE_ICONS_SENT);
					setState(DockingState.HANDSHAKE_ICONS_RESULT_LISTEN);
				}
				break;
			case HANDSHAKE_TIMEOUT_SENDING:
				if (DSetTimeout.COMMAND.equals(cmd)) {
					setState(DockingState.HANDSHAKE_TIMEOUT_SENT);
					setState(DockingState.HANDSHAKE_PASS_LISTEN);
				}
				break;
			case HANDSHAKE_PASS_SENDING:
				if (DPassword.COMMAND.equals(cmd)) {
					setState(DockingState.HANDSHAKE_PASS_SENT);
					setState(DockingState.HANDSHAKE_DONE);
					pipe.notifyConnected();
					done();
				}
				break;
			}
		} catch (BadPipeStateException bpse) {
			bpse.printStackTrace();
		} catch (PipeDisconnectedException pde) {
			pde.printStackTrace();
		}
	}

	@Override
	public void commandEOF() {
		done();
	}

	/**
	 * Get the Newton information.
	 * 
	 * @return the info.
	 */
	public static NewtonInfo getNewtonInfo() {
		return info;
	}

	/**
	 * Get the Newton's protocol version.
	 * 
	 * @return the version.
	 */
	public int getProtocolVersion() {
		return protocolVersion;
	}

	/**
	 * Handle an error result.
	 * 
	 * @param cmd
	 *            the result command with error.
	 * @throws BadPipeStateException
	 * @throws CDILNotInitializedException
	 * @throws PlatformException
	 * @throws PipeDisconnectedException
	 * @throws TimeoutException
	 */
	protected void handleError(DResult cmd) throws BadPipeStateException, CDILNotInitializedException, PlatformException, PipeDisconnectedException, TimeoutException {
		handleError(cmd.getError());
	}

	/**
	 * Handle an error result.
	 * 
	 * @param errorCode
	 *            the error code.
	 * @throws BadPipeStateException
	 * @throws CDILNotInitializedException
	 * @throws PlatformException
	 * @throws PipeDisconnectedException
	 * @throws TimeoutException
	 */
	protected void handleError(int errorCode) throws BadPipeStateException, CDILNotInitializedException, PlatformException, PipeDisconnectedException, TimeoutException {
		handleError(new NewtonError(errorCode));
	}

	/**
	 * Handle an error result.
	 * 
	 * @param error
	 *            the error.
	 * @throws BadPipeStateException
	 * @throws CDILNotInitializedException
	 * @throws PlatformException
	 * @throws PipeDisconnectedException
	 * @throws TimeoutException
	 */
	protected void handleError(NewtonError error) throws BadPipeStateException, CDILNotInitializedException, PlatformException, PipeDisconnectedException, TimeoutException {
		switch (error.getErrorCode()) {
		case DResult.OK:
			break;
		case ERROR_DISCONNECTED:
			pipe.disconnect();
			break;
		case ERROR_COMM_TIMEDOUT:
			throw new TimeoutException(error.getMessage());
		default:
			throw new BadPipeStateException(error);
		}
	}

	/**
	 * Docking protocol is finished.
	 */
	protected void done() {
		pipe.removeCommandListener(this);
	}
}
