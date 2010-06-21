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
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.crypto.DESNewton;
import net.sf.jncu.newton.NewtonError;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.NewtonInfo;
import net.sf.jncu.protocol.v1_0.query.DResult;

/**
 * Manage the docking protocol.
 * 
 * @author moshew
 */
public class DockingProtocol {

	private final CDPipe pipe;
	/** Internal state. */
	private DockingState state = DockingState.HANDSHAKE_LR_LISTEN;
	/** Newton information. */
	private NewtonInfo info;
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
	public DockingProtocol(CDPipe pipe) {
		super();
		this.pipe = pipe;
		this.crypto = new DESNewton();
		crypto.init(Cipher.ENCRYPT_MODE);
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
	 * @param oldDockingState
	 *            the old state.
	 * @param state
	 *            the new state.
	 * @param data
	 *            the command data.
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
	public void setState(DockingState oldDockingState, DockingState state, byte[] data, IDockCommandFromNewton cmd) throws PipeDisconnectedException,
			TimeoutException, BadPipeStateException, CDILNotInitializedException, PlatformException {
		// Only move the previous state to the next state.
		int compare = state.compareTo(oldDockingState);
		if (compare != 1) {
			throw new BadPipeStateException("bad state from " + oldDockingState + " to " + state);
		}

		setState(state);

		String cmdName = (cmd == null) ? null : cmd.getCommand();

		switch (oldDockingState) {
		case DISCONNECTED:
			if (state != DockingState.DISCONNECTED) {
				throw new PipeDisconnectedException();
			}
		}

		switch (state) {
		case HANDSHAKE_LR_LISTEN:
			break;
		case HANDSHAKE_LR_RECEIVED:
			break;
		case HANDSHAKE_LR_SENDING:
			break;
		case HANDSHAKE_LR_SENT:
			setState(DockingState.HANDSHAKE_RTDK_LISTEN);
			break;
		case HANDSHAKE_RTDK_LISTEN:
			if ((cmd == null) && !DockCommandFromNewton.isCommand(data)) {
				throw new BadPipeStateException("expected command");
			}
			cmd = DockCommandFromNewton.deserialize(data);
			if (DResult.COMMAND.equals(cmdName)) {
				handleError((DResult) cmd);
			} else if (!DRequestToDock.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DRequestToDock.COMMAND + "', and not '" + cmdName + "'");
			}
			setState(state, DockingState.HANDSHAKE_RTDK_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_RTDK_RECEIVED:
			if (DResult.COMMAND.equals(cmdName)) {
				handleError((DResult) cmd);
			} else if (!DRequestToDock.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DRequestToDock.COMMAND + "', and not '" + cmdName + "'");
			}
			commandReceived(cmd, state);
			break;
		case HANDSHAKE_DOCK_SENDING:
			break;
		case HANDSHAKE_DOCK_SENT:
			setState(state, DockingState.HANDSHAKE_NAME_LISTEN, data, null);
			break;
		case HANDSHAKE_NAME_LISTEN:
			if ((cmd == null) && (data == null)) {
				break;
			}
			if ((cmd == null) && !DockCommandFromNewton.isCommand(data)) {
				throw new BadPipeStateException("expected command");
			}
			cmd = DockCommandFromNewton.deserialize(data);
			if (DResult.COMMAND.equals(cmdName)) {
				handleError((DResult) cmd);
			} else if (!DNewtonName.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DNewtonName.COMMAND + "', and not '" + cmdName + "'");
			}
			setState(state, DockingState.HANDSHAKE_NAME_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_NAME_RECEIVED:
			if (DResult.COMMAND.equals(cmdName)) {
				handleError((DResult) cmd);
			} else if (!DNewtonName.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DNewtonName.COMMAND + "', and not '" + cmdName + "'");
			}
			commandReceived(cmd, state);
			break;
		case HANDSHAKE_DINFO_SENDING:
			break;
		case HANDSHAKE_DINFO_SENT:
			setState(state, DockingState.HANDSHAKE_NINFO_LISTEN, data, null);
			break;
		case HANDSHAKE_NINFO_LISTEN:
			if ((cmd == null) && (data == null)) {
				break;
			}
			if ((cmd == null) && !DockCommandFromNewton.isCommand(data)) {
				throw new BadPipeStateException("expected command");
			}
			cmd = DockCommandFromNewton.deserialize(data);
			if (DResult.COMMAND.equals(cmdName)) {
				handleError((DResult) cmd);
			} else if (!DNewtonInfo.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DNewtonInfo.COMMAND + "', and not '" + cmdName + "'");
			}
			setState(state, DockingState.HANDSHAKE_NINFO_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_NINFO_RECEIVED:
			if (DResult.COMMAND.equals(cmdName)) {
				handleError((DResult) cmd);
			} else if (!DNewtonInfo.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DNewtonInfo.COMMAND + "', and not '" + cmdName + "'");
			}
			commandReceived(cmd, state);
			break;
		case HANDSHAKE_ICONS_SENDING:
			break;
		case HANDSHAKE_ICONS_SENT:
			setState(state, DockingState.HANDSHAKE_ICONS_RESULT_LISTEN, data, null);
			break;
		case HANDSHAKE_ICONS_RESULT_LISTEN:
			if ((cmd == null) && (data == null)) {
				break;
			}
			if ((cmd == null) && !DockCommandFromNewton.isCommand(data)) {
				throw new BadPipeStateException("expected command");
			}
			cmd = DockCommandFromNewton.deserialize(data);
			if (!DResult.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DResult.COMMAND + "', and not '" + cmdName + "'");
			}
			setState(state, DockingState.HANDSHAKE_ICONS_RESULT_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_ICONS_RESULT_RECEIVED:
			if (!DResult.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DResult.COMMAND + "', and not '" + cmdName + "'");
			}
			commandReceived(cmd, state);
			break;
		case HANDSHAKE_TIMEOUT_SENDING:
			break;
		case HANDSHAKE_TIMEOUT_SENT:
			setState(state, DockingState.HANDSHAKE_PASS_LISTEN, data, null);
			break;
		case HANDSHAKE_PASS_LISTEN:
			if ((cmd == null) && (data == null)) {
				break;
			}
			if ((cmd == null) && !DockCommandFromNewton.isCommand(data)) {
				throw new BadPipeStateException("expected command");
			}
			cmd = DockCommandFromNewton.deserialize(data);
			if (DResult.COMMAND.equals(cmdName)) {
				handleError((DResult) cmd);
			} else if (!DPassword.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DPassword.COMMAND + "', and not '" + cmdName + "'");
			}
			setState(state, DockingState.HANDSHAKE_PASS_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_PASS_RECEIVED:
			if (DResult.COMMAND.equals(cmdName)) {
				handleError((DResult) cmd);
			} else if (!DPassword.COMMAND.equals(cmdName)) {
				throw new BadPipeStateException("expected command '" + DPassword.COMMAND + "', and not '" + cmdName + "'");
			}
			commandReceived(cmd, state);
			break;
		case HANDSHAKE_PASS_SENDING:
			break;
		case HANDSHAKE_PASS_SENT:
			setState(state, DockingState.HANDSHAKE_DONE, data, null);
			break;
		case HANDSHAKE_DONE:
			break;
		case DISCONNECTING:
			pipe.disconnect();
			break;
		case DISCONNECTED:
			break;
		default:
			throw new BadPipeStateException("bad state from " + oldDockingState + " to " + state);
		}
	}

	/**
	 * Command has been received, and now process it.
	 * 
	 * @param cmd
	 *            the command.
	 * @throws PipeDisconnectedException
	 *             if pipe disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 * @throws PlatformException
	 * @throws CDILNotInitializedException
	 * @throws BadPipeStateException
	 */
	public void commandReceived(IDockCommandFromNewton cmd) throws PipeDisconnectedException, TimeoutException, BadPipeStateException,
			CDILNotInitializedException, PlatformException {
		commandReceived(cmd, state);
	}

	/**
	 * Command has been received, and now process it.
	 * 
	 * @param cmd
	 *            the command.
	 * @param state
	 *            the state.
	 * @throws PipeDisconnectedException
	 *             if pipe disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 * @throws PlatformException
	 * @throws CDILNotInitializedException
	 * @throws BadPipeStateException
	 */
	public void commandReceived(IDockCommandFromNewton cmd, DockingState state) throws PipeDisconnectedException, TimeoutException, BadPipeStateException,
			CDILNotInitializedException, PlatformException {
		if (cmd == null) {
			throw new IllegalArgumentException("command expected");
		}

		switch (state) {
		case HANDSHAKE_RTDK_LISTEN:
			setState(state, DockingState.HANDSHAKE_RTDK_RECEIVED, null, cmd);
			break;
		case HANDSHAKE_RTDK_RECEIVED:
			DInitiateDocking cmdInitiateDocking = new DInitiateDocking();
			cmdInitiateDocking.setSession(DInitiateDocking.SESSION_SETTING_UP);
			setState(state, DockingState.HANDSHAKE_DOCK_SENDING, null, cmd);
			pipe.write(cmdInitiateDocking);
			break;
		case HANDSHAKE_NAME_LISTEN:
			setState(state, DockingState.HANDSHAKE_NAME_RECEIVED, null, cmd);
			break;
		case HANDSHAKE_NAME_RECEIVED:
			this.info = ((DNewtonName) cmd).getInformation();
			DDesktopInfo cmdDesktopInfo = new DDesktopInfo();
			this.challengeDesktop = cmdDesktopInfo.getEncryptedKey();
			this.challengeDesktopCiphered = crypto.cipher(challengeDesktop);
			setState(state, DockingState.HANDSHAKE_DINFO_SENDING, null, cmd);
			pipe.write(cmdDesktopInfo);
			break;
		case HANDSHAKE_NINFO_LISTEN:
			setState(state, DockingState.HANDSHAKE_NINFO_RECEIVED, null, cmd);
			break;
		case HANDSHAKE_NINFO_RECEIVED:
			DNewtonInfo cmdNewtonInfo = (DNewtonInfo) cmd;
			this.protocolVersion = cmdNewtonInfo.getProtocolVersion();
			this.challengeNewton = cmdNewtonInfo.getEncryptedKey();
			this.challengeNewtonCiphered = crypto.cipher(challengeNewton);

			DWhichIcons cmdWhichIcons = new DWhichIcons();
			cmdWhichIcons.setIcons(DWhichIcons.BACKUP | DWhichIcons.IMPORT | DWhichIcons.INSTALL | DWhichIcons.KEYBOARD | DWhichIcons.RESTORE
					| DWhichIcons.SYNC);
			setState(state, DockingState.HANDSHAKE_ICONS_SENDING, null, cmd);
			pipe.write(cmdWhichIcons);
			break;
		case HANDSHAKE_ICONS_RESULT_LISTEN:
			setState(state, DockingState.HANDSHAKE_ICONS_RESULT_RECEIVED, null, cmd);
			break;
		case HANDSHAKE_ICONS_RESULT_RECEIVED:
			DResult cmdResult = (DResult) cmd;
			if (cmdResult.getErrorCode() == 0) {
				DSetTimeout cmdSetTimeout = new DSetTimeout();
				cmdSetTimeout.setTimeout(pipe.getTimeout());
				setState(state, DockingState.HANDSHAKE_TIMEOUT_SENDING, null, cmd);
				pipe.write(cmdSetTimeout);
			} else {
				// Was problem, so try send again with less icons?
				cmdWhichIcons = new DWhichIcons();
				cmdWhichIcons.setIcons(DWhichIcons.INSTALL | DWhichIcons.KEYBOARD);
				setState(state, DockingState.HANDSHAKE_ICONS_SENDING, null, cmd);
				pipe.write(cmdWhichIcons);
			}
			break;
		case HANDSHAKE_PASS_LISTEN:
			setState(state, DockingState.HANDSHAKE_PASS_RECEIVED, null, cmd);
			break;
		case HANDSHAKE_PASS_RECEIVED:
			DPassword cmdPassword = (DPassword) cmd;

			if (cmdPassword.getEncryptedKey() == challengeDesktopCiphered) {
				DPassword cmdPasswordReply = new DPassword();
				cmdPasswordReply.setEncryptedKey(challengeNewtonCiphered);
				setState(state, DockingState.HANDSHAKE_PASS_SENDING, null, cmdPassword);
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
	}

	/**
	 * Get the Newton information.
	 * 
	 * @return the info.
	 */
	public NewtonInfo getNewtonInfo() {
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
	protected void handleError(DResult cmd) throws BadPipeStateException, CDILNotInitializedException, PlatformException, PipeDisconnectedException,
			TimeoutException {
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
	protected void handleError(int errorCode) throws BadPipeStateException, CDILNotInitializedException, PlatformException, PipeDisconnectedException,
			TimeoutException {
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
	protected void handleError(NewtonError error) throws BadPipeStateException, CDILNotInitializedException, PlatformException, PipeDisconnectedException,
			TimeoutException {
		switch (error.getErrorCode()) {
		case -16005:
			pipe.disconnect();
			break;
		case -10021:
			throw new TimeoutException(error.getMessage());
		default:
			throw new BadPipeStateException(error.getMessage());
		}
	}
}
