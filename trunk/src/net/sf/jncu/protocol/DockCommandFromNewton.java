package net.sf.jncu.protocol;

import java.net.ProtocolException;

import net.sf.jncu.protocol.v2_0.DockCommandFactory;

/**
 * Docking command from Newton to desktop.
 * 
 * @author moshew
 */
public abstract class DockCommandFromNewton extends DockCommand {

	/**
	 * Creates a new docking command from Newton.
	 * 
	 * @param cmd
	 *            the command.
	 */
	protected DockCommandFromNewton(String cmd) {
		super(cmd);
	}

	/**
	 * Creates a new docking command from Newton.
	 * 
	 * @param cmd
	 *            the command.
	 */
	protected DockCommandFromNewton(byte[] cmd) {
		super(cmd);
	}

	/**
	 * Is the frame a command frame?
	 * 
	 * @param frame
	 *            the frame data.
	 * @return <tt>true</tt> if frame contains a command - <tt>false</tt>
	 *         otherwise.
	 */
	public static boolean isCommand(byte[] frame) {
//		if (frame[DockingFrame.INDEX_LENGTH] < DockingFrame.INDEX_COMMAND + kDNewtonDockLength + COMMAND_LENGTH) {
//			return false;
//		}
		int o = DockingFrame.INDEX_COMMAND;
		for (int i = 0; i < kDNewtonDockLength; i++, o++) {
			if (kDNewtonDockBytes[i] != frame[o]) {
				return false;
			}
		}
		// Null-terminated strings.
		if (frame[o] != 0) {

		}
		return true;
	}

	/**
	 * Decode the frame.
	 * 
	 * @param frame
	 *            the frame data.
	 * @return the command.
	 * @throws ProtocolException
	 *             if a protocol error occurs.
	 */
	public static DockCommandFromNewton deserialize(byte[] frame) throws ProtocolException {
		byte[] cmdName = new byte[COMMAND_LENGTH];
		System.arraycopy(frame, DockingFrame.INDEX_COMMAND + kDNewtonDockLength, cmdName, 0, COMMAND_LENGTH);
		DockCommandFactory factory = DockCommandFactory.getInstance();
		DockCommandFromNewton cmd = (DockCommandFromNewton) factory.create(cmdName);
		cmd.decode(frame, DockingFrame.INDEX_COMMAND + kDNewtonDockLength + COMMAND_LENGTH);
		return cmd;
	}

	/**
	 * Decode the frame.
	 * 
	 * @param frame
	 *            the frame data.
	 * @param offset
	 *            the frame offset.
	 * @throws ProtocolException
	 *             if a protocol error occurs.
	 */
	protected abstract void decode(byte[] frame, int offset) throws ProtocolException;

	/**
	 * Read 4 bytes.
	 * 
	 * @param frame
	 *            the frame data.
	 * @param offset
	 *            the frame offset.
	 * @return the word.
	 */
	protected int readWord(byte[] frame, int offset) {
		int b24 = (frame[offset++] & 0xFF) << 24;
		int b16 = (frame[offset++] & 0xFF) << 16;
		int b08 = (frame[offset++] & 0xFF) << 8;
		int b00 = (frame[offset] & 0xFF) << 0;
		return b24 | b16 | b08 | b00;
	}
}
