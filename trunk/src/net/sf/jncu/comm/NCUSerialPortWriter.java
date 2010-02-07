package net.sf.jncu.comm;

import gnu.io.SerialPort;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class NCUSerialPortWriter extends Thread implements Closeable {

	private final SerialPort port;
	private OutputStream out;

	public NCUSerialPortWriter(SerialPort port) {
		super();
		this.port = port;
	}

	@Override
	public void run() {
	}

	public void close() throws IOException {
		if (out != null) {
			try {
				out.close();
			} catch (Exception e) {
				// ignore
			}
			out = null;
		}
	}

	/**
	 * Write a byte to the port.
	 * 
	 * @param b
	 *            the byte.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void write(byte b) throws IOException {
		write(b & 0xFF);
	}

	/**
	 * Write a byte to the port.
	 * 
	 * @param b
	 *            the data.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void write(int b) throws IOException {
		out = port.getOutputStream();
		out.write(b & 0xFF);
	}

	/**
	 * Writes all the bytes from the specified byte array to the port.
	 * 
	 * @param b
	 *            the data.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void write(byte[] b) throws IOException {
		out = port.getOutputStream();
		out.write(b);
	}

	/**
	 * Writes bytes from the specified byte array to the port.
	 * 
	 * @param b
	 *            the data.
	 * @param offset
	 *            the array offset.
	 * @param length
	 *            the number of bytes.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void write(byte[] b, int offset, int length) throws IOException {
		out = port.getOutputStream();
		out.write(b, offset, length);
	}
}
