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

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/**
 * Wraps a serial port.
 * 
 * @author moshew
 */
public class MNPSerialPort {

	public static final int BAUD_2400 = 2400;
	public static final int BAUD_4800 = BAUD_2400 << 1;
	public static final int BAUD_9600 = BAUD_4800 << 1;
	public static final int BAUD_19200 = BAUD_9600 << 1;
	public static final int BAUD_38400 = BAUD_19200 << 1;
	public static final int BAUD_57600 = BAUD_19200 * 3;
	public static final int BAUD_115200 = BAUD_57600 << 1;

	private final SerialPort serialPort;
	private MNPSerialPortReader reader;
	private MNPSerialPortWriter writer;

	/**
	 * Creates a new port.
	 * 
	 * @param portId
	 *            the serial port identifier.
	 * @param baud
	 *            the baud rate.
	 * @param timeout
	 *            the timeout period.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws PortInUseException
	 *             if port is not found.
	 * @throws UnsupportedCommOperationException
	 *             if operation unsupported.
	 */
	public MNPSerialPort(CommPortIdentifier portId, int baud, int timeout) throws TooManyListenersException, IOException, PortInUseException,
			UnsupportedCommOperationException {
		this((SerialPort) portId.open(portId.getName(), timeout));
	}

	/**
	 * Creates a new port.
	 * 
	 * @param port
	 *            the serial port.
	 * @param baud
	 *            the baud rate.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws UnsupportedCommOperationException
	 *             if operation unsupported.
	 */
	public MNPSerialPort(SerialPort port, int baud) throws TooManyListenersException, IOException, UnsupportedCommOperationException {
		super();
		port.setSerialPortParams(baud, port.getDataBits(), port.getStopBits(), port.getParity());
		this.serialPort = port;
		this.reader = createReader(port);
		this.writer = createWriter(port);
		reader.start();
		writer.start();
	}

	/**
	 * Creates a new port at 38,400 baud.
	 * 
	 * @param port
	 *            the serial port.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws UnsupportedCommOperationException
	 *             if operation unsupported.
	 */
	public MNPSerialPort(SerialPort port) throws TooManyListenersException, IOException, UnsupportedCommOperationException {
		this(port, BAUD_38400);
	}

	/**
	 * Close the port.
	 */
	public void close() {
		try {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
		serialPort.close();
	}

	/**
	 * Get the serial port.
	 * 
	 * @return the port.
	 */
	public SerialPort getPort() {
		return serialPort;
	}

	/**
	 * Get the blocking input stream for reading data from the port.
	 * 
	 * @return the stream.
	 */
	public InputStream getInputStream() {
		return reader.getInputStream();
	}

	/**
	 * Get the port output stream.
	 * 
	 * @return the stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public OutputStream getOutputStream() throws IOException {
		return writer.getOutputStream();
	}

	/**
	 * Create a serial port reader.
	 * 
	 * @param port
	 *            the serial port.
	 * @return the reader.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected MNPSerialPortReader createReader(SerialPort port) throws TooManyListenersException, IOException {
		return new MNPSerialPortReader(port);
	}

	/**
	 * Create a serial port writer.
	 * 
	 * @param port
	 *            the serial port.
	 * @return the reader.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected MNPSerialPortWriter createWriter(SerialPort port) throws TooManyListenersException, IOException {
		return new MNPSerialPortWriter(port);
	}

}
