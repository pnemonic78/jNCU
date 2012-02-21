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
import net.sf.jncu.cdil.CDPacketListener;
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
public class DockingProtocol<P extends CDPacket> implements DockCommandListener, CDPacketListener<P> {

	private final CDPipe<P> pipe;
	/** Internal state. */
	private DockingState state = DockingState.HANDSHAKE_LR_LISTEN;
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

	private static final int ERROR_COMM_TIMEDOUT = -10021;
	private static final int ERROR_DISCONNECTED = -16005;

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
		pipe.addPacketListener(this);
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

	public void setState(DockingState state) throws PipeDisconnectedException {
		if (this.state == DockingState.DISCONNECTED) {
			throw new PipeDisconnectedException();
		}
		this.state = state;
	}

	/**
	 * Flow to the next state.
	 * 
	 * @param state
	 *            the new state.
	 * @param cmd
	 *            the command.
	 * @throws PipeDisconnectedException
	 *             if pipe is disconnected.
	 * @throws TimeoutException
	 *             if a timeout occurs.
	 * @throws PlatformException
	 * @throws CDILNotInitializedException
	 * @throws BadPipeStateException
	 */
	public void setState(DockingState state, IDockCommandFromNewton cmd) throws PipeDisconnectedException, TimeoutException, BadPipeStateException, CDILNotInitializedException,
			PlatformException {
		setState(getState(), state, cmd);
	}

	/**
	 * Flow to the next state.
	 * 
	 * @param oldDockingState
	 *            the old state.
	 * @param state
	 *            the new state.
	 * @param command
	 *            the command.
	 * @throws PipeDisconnectedException
	 *             if pipe is disconnected.
	 * @throws TimeoutException
	 *             if a timeout occurs.
	 * @throws PlatformException
	 * @throws CDILNotInitializedException
	 * @throws BadPipeStateException
	 */
	protected void setState(DockingState oldDockingState, DockingState state, IDockCommandFromNewton command) throws PipeDisconnectedException, TimeoutException,
			BadPipeStateException, CDILNotInitializedException, PlatformException {
		// Only move the previous state to the next state.
		int compare = state.compareTo(oldDockingState);
		if (compare < 0) {
			// Maybe Newton sent us same packet again until we acknowledged it?
			return;
		}
		if (compare > 1) {
			throw new BadPipeStateException("bad state from " + oldDockingState + " to " + state);
		}

		setState(state);

		String cmdName = (command == null) ? null : command.getCommand();

		switch (oldDockingState) {
		case DISCONNECTED:
			if (state != DockingState.DISCONNECTED) {
				throw new PipeDisconnectedException();
			}
		}

		switch (state) {
		case HANDSHAKE_LR_LISTEN:
		case HANDSHAKE_LR_RECEIVED:
		case HANDSHAKE_LR_SENDING:
		case HANDSHAKE_LR_SENT:
			break;
		case HANDSHAKE_RTDK_LISTEN:
			break;
		case HANDSHAKE_RTDK_RECEIVED:
			if (command == null) {
				throw new BadPipeStateException("command required");
			}
			if (DResult.COMMAND.equals(cmdName)) {
				handleError((DResult) command);
			} else if (!DRequestToDock.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DRequestToDock.COMMAND + "', and not '" + cmdName + "'");
			}

			DInitiateDocking cmdInitiateDocking = new DInitiateDocking();
			cmdInitiateDocking.setSession(DInitiateDocking.SESSION_SETTING_UP);
			setState(state, DockingState.HANDSHAKE_DOCK_SENDING, command);
			pipe.write(cmdInitiateDocking);
			break;
		case HANDSHAKE_DOCK_SENDING:
			break;
		case HANDSHAKE_DOCK_SENT:
			setState(state, DockingState.HANDSHAKE_NAME_LISTEN, null);
			break;
		case HANDSHAKE_NAME_LISTEN:
			if (command != null) {
				setState(state, DockingState.HANDSHAKE_NAME_RECEIVED, command);
			}
			break;
		case HANDSHAKE_NAME_RECEIVED:
			if (command == null) {
				throw new BadPipeStateException("command required");
			}
			if (DResult.COMMAND.equals(cmdName)) {
				handleError((DResult) command);
			} else if (DRequestToDock.COMMAND.equals(cmdName)) {
				// Ignore duplicates.
				break;
			} else if (!DNewtonName.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DNewtonName.COMMAND + "', and not '" + cmdName + "'");
			}

			info = ((DNewtonName) command).getInformation();
			DDesktopInfo cmdDesktopInfo = new DDesktopInfo();
			this.challengeDesktop = cmdDesktopInfo.getEncryptedKey();
			this.challengeDesktopCiphered = crypto.cipher(challengeDesktop);
			setState(state, DockingState.HANDSHAKE_DINFO_SENDING, command);
			pipe.write(cmdDesktopInfo);
			break;
		case HANDSHAKE_DINFO_SENDING:
			break;
		case HANDSHAKE_DINFO_SENT:
			setState(state, DockingState.HANDSHAKE_NINFO_LISTEN, null);
			break;
		case HANDSHAKE_NINFO_LISTEN:
			if (command != null) {
				setState(state, DockingState.HANDSHAKE_NINFO_RECEIVED, command);
			}
			break;
		case HANDSHAKE_NINFO_RECEIVED:
			if (command == null) {
				throw new BadPipeStateException("command required");
			}
			if (DResult.COMMAND.equals(cmdName)) {
				handleError((DResult) command);
			} else if (DNewtonName.COMMAND.equals(cmdName)) {
				// Ignore duplicates.
				break;
			} else if (!DNewtonInfo.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DNewtonInfo.COMMAND + "', and not '" + cmdName + "'");
			}

			DNewtonInfo cmdNewtonInfo = (DNewtonInfo) command;
			this.protocolVersion = cmdNewtonInfo.getProtocolVersion();
			this.challengeNewton = cmdNewtonInfo.getEncryptedKey();
			this.challengeNewtonCiphered = crypto.cipher(challengeNewton);

			DWhichIcons cmdWhichIcons = new DWhichIcons();
			cmdWhichIcons.setIcons(DWhichIcons.BACKUP | DWhichIcons.IMPORT | DWhichIcons.INSTALL | DWhichIcons.KEYBOARD | DWhichIcons.RESTORE | DWhichIcons.SYNC);
			setState(state, DockingState.HANDSHAKE_ICONS_SENDING, command);
			pipe.write(cmdWhichIcons);
			break;
		case HANDSHAKE_ICONS_SENDING:
			break;
		case HANDSHAKE_ICONS_SENT:
			setState(state, DockingState.HANDSHAKE_ICONS_RESULT_LISTEN, null);
			break;
		case HANDSHAKE_ICONS_RESULT_LISTEN:
			if (command != null) {
				setState(state, DockingState.HANDSHAKE_ICONS_RESULT_RECEIVED, command);
			}
			break;
		case HANDSHAKE_ICONS_RESULT_RECEIVED:
			if (command == null) {
				throw new BadPipeStateException("command required");
			}
			if (DNewtonInfo.COMMAND.equals(cmdName)) {
				// Ignore duplicates.
				break;
			}
			if (!DResult.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DResult.COMMAND + "', and not '" + cmdName + "'");
			}

			DResult cmdResult = (DResult) command;
			if (cmdResult.getErrorCode() == 0) {
				DSetTimeout cmdSetTimeout = new DSetTimeout();
				cmdSetTimeout.setTimeout(pipe.getTimeout());
				setState(state, DockingState.HANDSHAKE_TIMEOUT_SENDING, command);
				pipe.write(cmdSetTimeout);
			} else {
				// Was problem, so try send again with less icons?
				cmdWhichIcons = new DWhichIcons();
				cmdWhichIcons.setIcons(DWhichIcons.INSTALL | DWhichIcons.KEYBOARD);
				setState(state, DockingState.HANDSHAKE_ICONS_SENDING, command);
				pipe.write(cmdWhichIcons);
			}
			break;
		case HANDSHAKE_TIMEOUT_SENDING:
			break;
		case HANDSHAKE_TIMEOUT_SENT:
			setState(state, DockingState.HANDSHAKE_PASS_LISTEN, null);
			break;
		case HANDSHAKE_PASS_LISTEN:
			if (command != null) {
				setState(state, DockingState.HANDSHAKE_PASS_RECEIVED, command);
			}
			break;
		case HANDSHAKE_PASS_RECEIVED:
			if (command == null) {
				throw new BadPipeStateException("command required");
			}
			if (DResult.COMMAND.equals(cmdName)) {
				handleError((DResult) command);
			} else if (!DPassword.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DPassword.COMMAND + "', and not '" + cmdName + "'");
			}

			DPassword cmdPassword = (DPassword) command;

			if (cmdPassword.getEncryptedKey() == challengeDesktopCiphered) {
				DPassword cmdPasswordReply = new DPassword();
				cmdPasswordReply.setEncryptedKey(challengeNewtonCiphered);
				setState(state, DockingState.HANDSHAKE_PASS_SENDING, cmdPassword);
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
		case HANDSHAKE_PASS_SENDING:
			break;
		case HANDSHAKE_PASS_SENT:
			setState(state, DockingState.HANDSHAKE_DONE, null);
			break;
		case HANDSHAKE_DONE:
			done();
			break;
		case DISCONNECTING:
			pipe.disconnect();
			break;
		case DISCONNECTED:
			commandEOF();
			break;
		default:
			throw new BadPipeStateException("bad state from " + oldDockingState + " to " + state);
		}
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		try {
			switch (state) {
			case HANDSHAKE_RTDK_LISTEN:
				setState(state, DockingState.HANDSHAKE_RTDK_RECEIVED, command);
				break;
			case HANDSHAKE_RTDK_RECEIVED:
				break;
			case HANDSHAKE_NAME_LISTEN:
				setState(state, DockingState.HANDSHAKE_NAME_RECEIVED, command);
				break;
			case HANDSHAKE_NAME_RECEIVED:
				break;
			case HANDSHAKE_NINFO_LISTEN:
				setState(state, DockingState.HANDSHAKE_NINFO_RECEIVED, command);
				break;
			case HANDSHAKE_NINFO_RECEIVED:
				break;
			case HANDSHAKE_ICONS_RESULT_LISTEN:
				setState(state, DockingState.HANDSHAKE_ICONS_RESULT_RECEIVED, command);
				break;
			case HANDSHAKE_ICONS_RESULT_RECEIVED:
				break;
			case HANDSHAKE_PASS_LISTEN:
				setState(state, DockingState.HANDSHAKE_PASS_RECEIVED, command);
				break;
			case HANDSHAKE_PASS_RECEIVED:
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
		String cmdName = command.getCommand();

		try {
			switch (state) {
			case HANDSHAKE_DOCK_SENDING:
				if (DInitiateDocking.COMMAND.equals(cmdName)) {
					setState(state, DockingState.HANDSHAKE_DOCK_SENT, null);
				}
				break;
			case HANDSHAKE_DINFO_SENDING:
				if (DDesktopInfo.COMMAND.equals(cmdName)) {
					setState(state, DockingState.HANDSHAKE_DINFO_SENT, null);
				}
				break;
			case HANDSHAKE_ICONS_SENDING:
				if (DWhichIcons.COMMAND.equals(cmdName)) {
					setState(state, DockingState.HANDSHAKE_ICONS_SENT, null);
				}
				break;
			case HANDSHAKE_TIMEOUT_SENDING:
				if (DSetTimeout.COMMAND.equals(cmdName)) {
					setState(state, DockingState.HANDSHAKE_TIMEOUT_SENT, null);
				}
				break;
			case HANDSHAKE_PASS_SENDING:
				if (DPassword.COMMAND.equals(cmdName)) {
					setState(state, DockingState.HANDSHAKE_PASS_SENT, null);
					pipe.notifyConnected();
				}
				break;
			}
		} catch (BadPipeStateException bpse) {
			bpse.printStackTrace();
		} catch (PipeDisconnectedException pde) {
			pde.printStackTrace();
		} catch (CDILNotInitializedException cnie) {
			cnie.printStackTrace();
		} catch (PlatformException pe) {
			pe.printStackTrace();
		} catch (TimeoutException te) {
			te.printStackTrace();
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
		case ERROR_DISCONNECTED:
			pipe.disconnect();
			break;
		case ERROR_COMM_TIMEDOUT:
			throw new TimeoutException(error.getMessage());
		default:
			throw new BadPipeStateException(error);
		}
	}

	@Override
	public void packetReceived(P packet) {
	}

	@Override
	public void packetSent(P packet) {
	}

	@Override
	public void packetEOF() {
		done();
	}

	/**
	 * Docking protocol is finished.
	 */
	protected void done() {
		pipe.removePacketListener(this);
		pipe.removeCommandListener(this);
	}
}
