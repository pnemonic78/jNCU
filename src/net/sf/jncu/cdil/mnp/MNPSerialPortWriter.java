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
package net.sf.jncu.cdil.mnp;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * MNP serial port writer.
 * 
 * @author moshew
 */
public class MNPSerialPortWriter extends Thread implements Closeable {

	protected final SerialPort port;
	private OutputStream out;

	/**
	 * Creates a new serial port writer.
	 * 
	 * @param port
	 *            the serial port.
	 */
	public MNPSerialPortWriter(SerialPort port) {
		super();
		setName("MNPSerialPortWriter-" + getId());
		this.port = port;
	}

	@Override
	public void close() throws IOException {
		getOutputStream().close();
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
		getOutputStream().write(b & 0xFF);
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
		getOutputStream().write(b);
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
		getOutputStream().write(b, offset, length);
	}

	/**
	 * Get the port output stream.
	 * 
	 * @return the stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public OutputStream getOutputStream() throws IOException {
		if (out == null)
			out = new SerialPortOutputStream(port);
		return out;
	}

	/**
	 * Output stream wrapper for writing to the port.
	 * 
	 * @author moshe
	 */
	protected static class SerialPortOutputStream extends OutputStream {

		private final SerialPort port;

		public SerialPortOutputStream(SerialPort port) {
			this.port = port;
		}

		@Override
		public void write(int b) throws IOException {
			try {
				port.writeByte((byte) b);
			} catch (SerialPortException se) {
				throw new IOException(se.getCause());
			}
		}

		@Override
		public void write(byte[] b) throws IOException {
			try {
				port.writeBytes(b);
			} catch (SerialPortException se) {
				throw new IOException(se.getCause());
			}
		}
	}
}
