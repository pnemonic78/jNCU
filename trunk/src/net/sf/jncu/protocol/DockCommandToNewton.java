package net.sf.jncu.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Docking command from desktop to Newton.
 * 
 * @author moshew
 */
public abstract class DockCommandToNewton extends DockCommand {

	/**
	 * Creates a new docking command from Newton.
	 * 
	 * @param cmd
	 *            the command.
	 */
	protected DockCommandToNewton(String cmd) {
		super(cmd);
	}

	/**
	 * Creates a new docking command from Newton.
	 * 
	 * @param cmd
	 *            the command.
	 */
	protected DockCommandToNewton(byte[] cmd) {
		super(cmd);
	}

	/**
	 * Get the frame content for sending.
	 * 
	 * @return the frame content.
	 */
	public byte[] getFrame() {
		ByteArrayOutputStream frame = new ByteArrayOutputStream();
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		try {
			byte[] data = encode();
			int length = (data == null) ? 0 : data.length;
			setLength(length);

			buf.write(DockingFrame.FRAME_TYPE_LT);
			buf.write(kDNewtonDockBytes);
			buf.write(cmdBytes);
			buf.write((length >> 24) & 0xFF);
			buf.write((length >> 16) & 0xFF);
			buf.write((length >> 8) & 0xFF);
			buf.write((length >> 0) & 0xFF);
			buf.write(data);
			buf.close();

			frame.write(buf.size());
			frame.write(buf.toByteArray());
			frame.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return frame.toByteArray();
	}

	/**
	 * Encode the data to write.
	 */
	protected abstract byte[] encode();

}
