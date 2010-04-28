package net.sf.jncu.protocol;

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
	 * Is the data a command?
	 * 
	 * @param data
	 *            the data.
	 * @return <tt>true</tt> if frame contains a command - <tt>false</tt>
	 *         otherwise.
	 */
	public static boolean isCommand(byte[] data) {
		for (int i = 0; i < kDNewtonDockLength; i++) {
			if (kDNewtonDockBytes[i] != data[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Decode the data.
	 * 
	 * @param data
	 *            the data.
	 * @return the command.
	 */
	public static DockCommandFromNewton deserialize(byte[] data) {
		if ((data == null) || (data.length < 16)) {
			return null;
		}
		int offset = 0;
		byte[] cmdName = new byte[COMMAND_LENGTH];
		System.arraycopy(data, kDNewtonDockLength, cmdName, offset, COMMAND_LENGTH);
		offset += kDNewtonDockLength;
		DockCommandFactory factory = DockCommandFactory.getInstance();
		DockCommandFromNewton cmd = (DockCommandFromNewton) factory.create(cmdName);
		offset += COMMAND_LENGTH;
		if (cmd != null) {
			cmd.decode(data, offset);
		}
		return cmd;
	}

	/**
	 * Decode the command.
	 * 
	 * @param frame
	 *            the frame data.
	 * @param offset
	 *            the frame offset.
	 * @return the new offset.
	 */
	protected void decode(byte[] frame, int offset) {
		int length = htonl(frame, offset);
		setLength(length);
		offset += LENGTH_WORD;
		decodeData(frame, offset, length);
	}

	/**
	 * Decode the command data.
	 * 
	 * @param data
	 *            the data.
	 * @param offset
	 *            the data offset.
	 * @param length
	 *            the data length.
	 */
	protected abstract void decodeData(byte[] data, int offset, int length);

	/**
	 * Read 4 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param frame
	 *            the frame data.
	 * @param offset
	 *            the frame offset.
	 * @return the number.
	 */
	protected int htonl(byte[] frame, int offset) {
		int n24 = (frame[offset++] & 0xFF) << 24;
		int n16 = (frame[offset++] & 0xFF) << 16;
		int n08 = (frame[offset++] & 0xFF) << 8;
		int n00 = (frame[offset] & 0xFF) << 0;

		return n24 | n16 | n08 | n00;
	}
}
