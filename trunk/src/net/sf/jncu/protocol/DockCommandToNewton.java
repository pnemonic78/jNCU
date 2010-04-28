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
	 * Get the payload to send.
	 * 
	 * @return the payload.
	 */
	public byte[] getPayload() {
		ByteArrayOutputStream payload = new ByteArrayOutputStream();
		ByteArrayOutputStream dataStream = getData();
		int length = 0;
		byte[] data = null;

		try {
			if (dataStream != null) {
				dataStream.flush();
				dataStream.close();
				data = dataStream.toByteArray();
				length = data.length;
			}
			setLength(length);

			payload.write(kDNewtonDockBytes);
			payload.write(commandBytes);
			ntohl(length, payload);
			if (data != null) {
				payload.write(data);
			}
			// 4-byte align
			switch (payload.size() & 3) {
			case 1:
				payload.write(0);
			case 2:
				payload.write(0);
			case 3:
				payload.write(0);
				break;
			}
			payload.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return payload.toByteArray();
	}

	/**
	 * Encode the data to write.
	 */
	protected ByteArrayOutputStream getData() {
		return null;
	}

	/**
	 * Write 4 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param n
	 *            the number.
	 * @param frame
	 *            the frame data.
	 */
	protected void ntohl(int n, ByteArrayOutputStream frame) {
		frame.write((n >> 24) & 0xFF);
		frame.write((n >> 16) & 0xFF);
		frame.write((n >> 8) & 0xFF);
		frame.write((n >> 0) & 0xFF);
	}
}
