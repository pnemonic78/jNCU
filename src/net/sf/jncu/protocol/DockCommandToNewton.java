package net.sf.jncu.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
		ByteArrayOutputStream dataStream;
		int length = 0;
		byte[] data = null;

		try {
			dataStream = getCommandData();
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
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected ByteArrayOutputStream getCommandData() throws IOException {
		return null;
	}

	/**
	 * Write 4 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param out
	 *            the output.
	 * @param frame
	 *            the frame data.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void ntohl(int n, OutputStream out) throws IOException {
		out.write((n >> 24) & 0xFF);
		out.write((n >> 16) & 0xFF);
		out.write((n >> 8) & 0xFF);
		out.write((n >> 0) & 0xFF);
	}

	/**
	 * Write 4 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param out
	 *            the output.
	 * @param frame
	 *            the frame data.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void ntohl(long n, OutputStream out) throws IOException {
		ntohl((int) ((n >> 32) & 0xFFFFFFFFL), out);
		ntohl((int) ((n >> 0) & 0xFFFFFFFFL), out);
	}

	/**
	 * Write a <tt>C</tt>-style Unicode string (UTF-16, null-terminated).
	 * 
	 * @param s
	 *            the string.
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeString(String s, OutputStream out) throws IOException {
		if ((s != null) && (s.length() > 0)) {
			byte[] utf16 = s.getBytes("UTF-16");
			// The 1st and 2nd bytes are UTF-16 header 0xFE and 0xFF.
			out.write(utf16, 2, utf16.length - 2);
		}
		// Null-terminated string.
		out.write(0);
		out.write(0);
	}
}
