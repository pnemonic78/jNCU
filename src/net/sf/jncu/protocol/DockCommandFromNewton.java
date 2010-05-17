package net.sf.jncu.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
		if ((data == null) || (data.length < kDNewtonDockLength)) {
			return false;
		}
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
			byte[] dataBytes = new byte[data.length - offset];
			System.arraycopy(data, offset, dataBytes, 0, dataBytes.length);
			InputStream in = new ByteArrayInputStream(dataBytes);
			try {
				cmd.decode(in);
			} catch (IOException ioe) {
				throw new ArrayIndexOutOfBoundsException();
			}
		}
		return cmd;
	}

	/**
	 * Decode the command.
	 * 
	 * @param frame
	 *            the frame data.
	 * @throws IOException
	 *             if read past data buffer.
	 */
	protected void decode(InputStream frame) throws IOException {
		int length = htonl(frame);
		setLength(length);
		if ((frame.available() < length) && (length != 0x0010000)) {
			throw new ArrayIndexOutOfBoundsException();
		}
		decodeData(frame);
	}

	/**
	 * Decode the command data.
	 * 
	 * @param data
	 *            the data.
	 * @throws IOException
	 *             if read past data buffer.
	 */
	protected abstract void decodeData(InputStream data) throws IOException;

	/**
	 * Read 4 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param in
	 *            the input.
	 * @return the number.
	 * @throws IOException
	 *             if read past buffer.
	 */
	protected int htonl(InputStream in) throws IOException {
		int n24 = (in.read() & 0xFF) << 24;
		int n16 = (in.read() & 0xFF) << 16;
		int n08 = (in.read() & 0xFF) << 8;
		int n00 = (in.read() & 0xFF) << 0;

		return n24 | n16 | n08 | n00;
	}
}
