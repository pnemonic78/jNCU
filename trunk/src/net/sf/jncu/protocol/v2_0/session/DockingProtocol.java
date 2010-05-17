package net.sf.jncu.protocol.v2_0.session;

import java.util.concurrent.TimeoutException;

import javax.crypto.Cipher;

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.crypto.DESNewton;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.v1_0.DCmdResult;
import net.sf.jncu.protocol.v2_0.DCmdReply;
import net.sf.jncu.protocol.v2_0.DockCommandFactory;

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
	private int protocolVersion = DCmdDesktopInfo.PROTOCOL_VERSION;
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
		crypto.init(Cipher.ENCRYPT_MODE, DESNewton.NEWTON_DEFAULT_KEY);
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
	public void setState(DockingState oldDockingState, DockingState state, byte[] data, DockCommandFromNewton cmd) throws PipeDisconnectedException,
			TimeoutException, BadPipeStateException, CDILNotInitializedException, PlatformException {
		// Only move the previous state to the next state.
		int compare = state.compareTo(oldDockingState);
		if (compare != 1) {
			throw new BadPipeStateException("bad state from " + oldDockingState + " to " + state);
		}

		setState(state);

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
			if (!DCmdRequestToDock.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DCmdRequestToDock.COMMAND + "', and not '" + cmd.getCommand() + "'");
			}
			setState(state, DockingState.HANDSHAKE_RTDK_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_RTDK_RECEIVED:
			if (!DCmdRequestToDock.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DCmdRequestToDock.COMMAND + "', and not '" + cmd.getCommand() + "'");
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
			if (!DCmdNewtonName.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DCmdNewtonName.COMMAND + "', and not '" + cmd.getCommand() + "', and not '"
						+ cmd.getCommand() + "'");
			}
			setState(state, DockingState.HANDSHAKE_NAME_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_NAME_RECEIVED:
			if (!DCmdNewtonName.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DCmdNewtonName.COMMAND + "', and not '" + cmd.getCommand() + "'");
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
			if (!DCmdNewtonInfo.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DCmdNewtonInfo.COMMAND + "', and not '" + cmd.getCommand() + "'");
			}
			setState(state, DockingState.HANDSHAKE_NINFO_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_NINFO_RECEIVED:
			if (!DCmdNewtonInfo.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DCmdNewtonInfo.COMMAND + "', and not '" + cmd.getCommand() + "'");
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
			if (!DCmdResult.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DCmdResult.COMMAND + "', and not '" + cmd.getCommand() + "'");
			}
			setState(state, DockingState.HANDSHAKE_ICONS_RESULT_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_ICONS_RESULT_RECEIVED:
			if (!DCmdResult.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DCmdResult.COMMAND + "', and not '" + cmd.getCommand() + "'");
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
			if (!DCmdPassword.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DCmdPassword.COMMAND + "', and not '" + cmd.getCommand() + "'");
			}
			setState(state, DockingState.HANDSHAKE_PASS_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_PASS_RECEIVED:
			if (!DCmdPassword.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DCmdPassword.COMMAND + "', and not '" + cmd.getCommand() + "'");
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
	public void commandReceived(DockCommandFromNewton cmd) throws PipeDisconnectedException, TimeoutException, BadPipeStateException,
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
	public void commandReceived(DockCommandFromNewton cmd, DockingState state) throws PipeDisconnectedException, TimeoutException, BadPipeStateException,
			CDILNotInitializedException, PlatformException {
		if (cmd == null) {
			throw new IllegalArgumentException("command expected");
		}

		switch (state) {
		case HANDSHAKE_RTDK_LISTEN:
			setState(state, DockingState.HANDSHAKE_RTDK_RECEIVED, null, cmd);
			break;
		case HANDSHAKE_RTDK_RECEIVED:
			DCmdInitiateDocking cmdInitiateDocking = (DCmdInitiateDocking) DockCommandFactory.getInstance().create(DCmdInitiateDocking.COMMAND);
			cmdInitiateDocking.setSession(DCmdInitiateDocking.SESSION_SETTING_UP);
			setState(state, DockingState.HANDSHAKE_DOCK_SENDING, null, cmd);
			pipe.write(cmdInitiateDocking);
			break;
		case HANDSHAKE_NAME_LISTEN:
			setState(state, DockingState.HANDSHAKE_NAME_RECEIVED, null, cmd);
			break;
		case HANDSHAKE_NAME_RECEIVED:
			this.info = ((DCmdNewtonName) cmd).getInformation();
			DCmdDesktopInfo cmdDesktopInfo = (DCmdDesktopInfo) DockCommandFactory.getInstance().create(DCmdDesktopInfo.COMMAND);
			this.challengeDesktop = cmdDesktopInfo.getEncryptedKey();
			this.challengeDesktopCiphered = crypto.cipher(challengeDesktop);
			setState(state, DockingState.HANDSHAKE_DINFO_SENDING, null, cmd);
			pipe.write(cmdDesktopInfo);
			break;
		case HANDSHAKE_NINFO_LISTEN:
			setState(state, DockingState.HANDSHAKE_NINFO_RECEIVED, null, cmd);
			break;
		case HANDSHAKE_NINFO_RECEIVED:
			DCmdNewtonInfo cmdNewtonInfo = (DCmdNewtonInfo) cmd;
			this.protocolVersion = cmdNewtonInfo.getProtocolVersion();
			this.challengeNewton = cmdNewtonInfo.getEncryptedKey();
			this.challengeNewtonCiphered = crypto.cipher(challengeNewton);

			DCmdWhichIcons cmdWhichIcons = (DCmdWhichIcons) DockCommandFactory.getInstance().create(DCmdWhichIcons.COMMAND);
			cmdWhichIcons.setIcons(DCmdWhichIcons.kBackupIcon | DCmdWhichIcons.kInstallIcon | DCmdWhichIcons.kKeyboardIcon | DCmdWhichIcons.kRestoreIcon);
			setState(state, DockingState.HANDSHAKE_ICONS_SENDING, null, cmd);
			pipe.write(cmdWhichIcons);
			break;
		case HANDSHAKE_ICONS_RESULT_LISTEN:
			setState(state, DockingState.HANDSHAKE_ICONS_RESULT_RECEIVED, null, cmd);
			break;
		case HANDSHAKE_ICONS_RESULT_RECEIVED:
			DCmdResult cmdResult = (DCmdResult) cmd;
			if (cmdResult.getErrorCode() == 0) {
				DCmdSetTimeout cmdSetTimeout = (DCmdSetTimeout) DockCommandFactory.getInstance().create(DCmdSetTimeout.COMMAND);
				cmdSetTimeout.setTimeout(pipe.getTimeout());
				setState(state, DockingState.HANDSHAKE_TIMEOUT_SENDING, null, cmd);
				pipe.write(cmdSetTimeout);
			} else {
				// Was problem, so try send again with less icons?
				cmdWhichIcons = (DCmdWhichIcons) DockCommandFactory.getInstance().create(DCmdWhichIcons.COMMAND);
				cmdWhichIcons.setIcons(DCmdWhichIcons.kInstallIcon | DCmdWhichIcons.kKeyboardIcon);
				setState(state, DockingState.HANDSHAKE_ICONS_SENDING, null, cmd);
				pipe.write(cmdWhichIcons);
			}
			break;
		case HANDSHAKE_PASS_LISTEN:
			setState(state, DockingState.HANDSHAKE_PASS_RECEIVED, null, cmd);
			break;
		case HANDSHAKE_PASS_RECEIVED:
			DCmdPassword cmdPassword = (DCmdPassword) cmd;

			if (cmdPassword.getEncryptedKey() == challengeDesktopCiphered) {
				DCmdPasswordReply cmdPasswordReply = new DCmdPasswordReply();
				cmdPasswordReply.setEncryptedKey(challengeNewtonCiphered);
				setState(state, DockingState.HANDSHAKE_PASS_SENDING, null, cmdPassword);
				pipe.write(cmdPasswordReply);
			} else {
				int error = -1;
				challengePasswordAttempt++;
				if (challengePasswordAttempt < MAX_PASSWORD_ATTEMPTS) {
					error = DCmdPassword.ERROR_RETRY_PASSWORD;
				} else {
					error = DCmdPassword.ERROR_BAD_PASSWORD;
				}
				DCmdReply cmdError = new DCmdReply();
				cmdError.setErrorCode(error);
				pipe.write(cmdError);
			}
			break;
		case DISCONNECTED:
			throw new PipeDisconnectedException();
		default:
			throw new BadPipeStateException("bad state " + state);
		}
	}

}
