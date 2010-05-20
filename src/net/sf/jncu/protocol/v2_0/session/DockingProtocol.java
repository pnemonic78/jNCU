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
import net.sf.jncu.protocol.NewtonInfo;
import net.sf.jncu.protocol.v1_0.DResult;
import net.sf.jncu.protocol.v2_0.DReply;
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
	private int protocolVersion = DDesktopInfo.PROTOCOL_VERSION;
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
			if (!DRequestToDock.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DRequestToDock.COMMAND + "', and not '" + cmd.getCommand() + "'");
			}
			setState(state, DockingState.HANDSHAKE_RTDK_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_RTDK_RECEIVED:
			if (!DRequestToDock.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DRequestToDock.COMMAND + "', and not '" + cmd.getCommand() + "'");
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
			if (!DNewtonName.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DNewtonName.COMMAND + "', and not '" + cmd.getCommand() + "', and not '"
						+ cmd.getCommand() + "'");
			}
			setState(state, DockingState.HANDSHAKE_NAME_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_NAME_RECEIVED:
			if (!DNewtonName.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DNewtonName.COMMAND + "', and not '" + cmd.getCommand() + "'");
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
			if (!DNewtonInfo.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DNewtonInfo.COMMAND + "', and not '" + cmd.getCommand() + "'");
			}
			setState(state, DockingState.HANDSHAKE_NINFO_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_NINFO_RECEIVED:
			if (!DNewtonInfo.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DNewtonInfo.COMMAND + "', and not '" + cmd.getCommand() + "'");
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
			if (!DResult.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DResult.COMMAND + "', and not '" + cmd.getCommand() + "'");
			}
			setState(state, DockingState.HANDSHAKE_ICONS_RESULT_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_ICONS_RESULT_RECEIVED:
			if (!DResult.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DResult.COMMAND + "', and not '" + cmd.getCommand() + "'");
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
			if (!DPassword.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DPassword.COMMAND + "', and not '" + cmd.getCommand() + "'");
			}
			setState(state, DockingState.HANDSHAKE_PASS_RECEIVED, data, cmd);
			break;
		case HANDSHAKE_PASS_RECEIVED:
			if (!DPassword.COMMAND.equals(cmd.getCommand())) {
				throw new BadPipeStateException("expected command '" + DPassword.COMMAND + "', and not '" + cmd.getCommand() + "'");
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
			DInitiateDocking cmdInitiateDocking = (DInitiateDocking) DockCommandFactory.getInstance().create(DInitiateDocking.COMMAND);
			cmdInitiateDocking.setSession(DInitiateDocking.SESSION_SETTING_UP);
			setState(state, DockingState.HANDSHAKE_DOCK_SENDING, null, cmd);
			pipe.write(cmdInitiateDocking);
			break;
		case HANDSHAKE_NAME_LISTEN:
			setState(state, DockingState.HANDSHAKE_NAME_RECEIVED, null, cmd);
			break;
		case HANDSHAKE_NAME_RECEIVED:
			this.info = ((DNewtonName) cmd).getInformation();
			DDesktopInfo cmdDesktopInfo = (DDesktopInfo) DockCommandFactory.getInstance().create(DDesktopInfo.COMMAND);
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

			DWhichIcons cmdWhichIcons = (DWhichIcons) DockCommandFactory.getInstance().create(DWhichIcons.COMMAND);
			cmdWhichIcons.setIcons(DWhichIcons.kBackupIcon | DWhichIcons.kImportIcon | DWhichIcons.kInstallIcon | DWhichIcons.kKeyboardIcon
					| DWhichIcons.kRestoreIcon | DWhichIcons.kSyncIcon);
			setState(state, DockingState.HANDSHAKE_ICONS_SENDING, null, cmd);
			pipe.write(cmdWhichIcons);
			break;
		case HANDSHAKE_ICONS_RESULT_LISTEN:
			setState(state, DockingState.HANDSHAKE_ICONS_RESULT_RECEIVED, null, cmd);
			break;
		case HANDSHAKE_ICONS_RESULT_RECEIVED:
			DResult cmdResult = (DResult) cmd;
			if (cmdResult.getErrorCode() == 0) {
				DSetTimeout cmdSetTimeout = (DSetTimeout) DockCommandFactory.getInstance().create(DSetTimeout.COMMAND);
				cmdSetTimeout.setTimeout(pipe.getTimeout());
				setState(state, DockingState.HANDSHAKE_TIMEOUT_SENDING, null, cmd);
				pipe.write(cmdSetTimeout);
			} else {
				// Was problem, so try send again with less icons?
				cmdWhichIcons = (DWhichIcons) DockCommandFactory.getInstance().create(DWhichIcons.COMMAND);
				cmdWhichIcons.setIcons(DWhichIcons.kInstallIcon | DWhichIcons.kKeyboardIcon);
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
				DPasswordReply cmdPasswordReply = new DPasswordReply();
				cmdPasswordReply.setEncryptedKey(challengeNewtonCiphered);
				setState(state, DockingState.HANDSHAKE_PASS_SENDING, null, cmdPassword);
				pipe.write(cmdPasswordReply);
			} else {
				int error = DPassword.ERROR_BAD_PASSWORD;
				challengePasswordAttempt++;
				if (challengePasswordAttempt < MAX_PASSWORD_ATTEMPTS) {
					error = DPassword.ERROR_RETRY_PASSWORD;
				}
				DReply cmdError = new DReply();
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

}
