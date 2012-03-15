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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import jssc.SerialPort;
import jssc.SerialPortException;
import net.sf.jncu.io.NoSuchPortException;
import net.sf.jncu.io.PortInUseException;

/**
 * Wraps a serial port.
 * 
 * @author moshew
 */
public class MNPSerialPort {

	public static final int BAUD_2400 = SerialPort.BAUDRATE_4800 >> 1;
	public static final int BAUD_4800 = SerialPort.BAUDRATE_4800;
	public static final int BAUD_9600 = SerialPort.BAUDRATE_9600;
	public static final int BAUD_19200 = SerialPort.BAUDRATE_19200;
	public static final int BAUD_38400 = SerialPort.BAUDRATE_38400;
	public static final int BAUD_57600 = SerialPort.BAUDRATE_57600;
	public static final int BAUD_115200 = SerialPort.BAUDRATE_115200;

	/** Port can timeout after 1 minute. */
	public static final int PORT_TIMEOUT = 60;

	private final SerialPort serialPort;
	private MNPSerialPortReader reader;
	private MNPSerialPortWriter writer;

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
	 * @throws PortInUseException
	 *             if port is busy.
	 * @throws NoSuchPortException
	 *             if port is not found.
	 */
	public MNPSerialPort(SerialPort port, int baud) throws TooManyListenersException, IOException, PortInUseException, NoSuchPortException {
		super();
		try {
			port.openPort();
			port.setParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (SerialPortException se) {
			if (SerialPortException.TYPE_PORT_BUSY.equals(se.getExceptionType()))
				throw new PortInUseException(port.getPortName());
			if (SerialPortException.TYPE_PORT_NOT_FOUND.equals(se.getExceptionType()))
				throw new NoSuchPortException(port.getPortName());
			throw new IOException(se);
		}
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
	 * @throws PortInUseException
	 *             if port is busy.
	 * @throws NoSuchPortException
	 *             if port is not found.
	 */
	public MNPSerialPort(SerialPort port) throws TooManyListenersException, IOException, PortInUseException, NoSuchPortException {
		this(port, BAUD_38400);
	}

	/**
	 * Creates a new port.
	 * 
	 * @param portName
	 *            the serial port name.
	 * @param baud
	 *            the baud rate.
	 * @param timeout
	 *            the timeout period.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws PortInUseException
	 *             if port is busy.
	 * @throws NoSuchPortException
	 *             if port is not found.
	 */
	@Deprecated
	public MNPSerialPort(String portName, int baud, int timeout) throws TooManyListenersException, IOException, PortInUseException, NoSuchPortException {
		this(new SerialPort(portName), baud);
	}

	/**
	 * Creates a new port.
	 * 
	 * @param portName
	 *            the serial port name.
	 * @param baud
	 *            the baud rate.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws PortInUseException
	 *             if port is busy.
	 * @throws NoSuchPortException
	 *             if port is not found.
	 */
	public MNPSerialPort(String portName, int baud) throws TooManyListenersException, IOException, PortInUseException, NoSuchPortException {
		this(portName, baud, PORT_TIMEOUT);
	}

	/**
	 * Creates a new port.
	 * 
	 * @param portName
	 *            the serial port name.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws PortInUseException
	 *             if port is busy.
	 * @throws NoSuchPortException
	 *             if port is not found.
	 */
	public MNPSerialPort(String portName) throws TooManyListenersException, IOException, PortInUseException, NoSuchPortException {
		this(portName, BAUD_38400);
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
		try {
			serialPort.closePort();
		} catch (SerialPortException se) {
			// ignore
		}
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
