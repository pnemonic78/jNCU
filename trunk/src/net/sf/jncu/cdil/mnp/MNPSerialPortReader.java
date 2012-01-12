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

import gnu.io.SerialPort;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.TooManyListenersException;

/**
 * MNP serial port reader.
 * 
 * @author moshew
 */
public class MNPSerialPortReader extends Thread implements Closeable {

	protected final SerialPort port;
	private MNPSerialPortEventListener listener;
	/** Stream for the serial port to populate with data. */
	private final PipedOutputStream data = new PipedOutputStream();
	/** Stream of usable data that has been populated from the serial port. */
	private InputStream in;

	/**
	 * Creates a new port reader.
	 * 
	 * @param port
	 *            the serial port.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public MNPSerialPortReader(SerialPort port) throws TooManyListenersException, IOException {
		super();
		setName("SerialPortReader-" + getId());
		this.port = port;
		this.in = new PipedInputStream(data);
		this.listener = createPortListener(port, data);
		port.notifyOnDataAvailable(true);
		port.addEventListener(listener);
	}

	@Override
	public void close() throws IOException {
		if (listener != null) {
			listener.close();
			listener = null;
			try {
				port.removeEventListener();
			} catch (NullPointerException npe) {
				// consume
			}
		}
		if (in != null) {
			try {
				in.close();
			} catch (Exception e) {
				// ignore
			}
		}
	}

	/**
	 * Get the blocking input stream for reading data from the port.
	 * 
	 * @return the stream.
	 */
	public InputStream getInputStream() {
		return in;
	}

	/**
	 * Create a port event listener.
	 * 
	 * @param port
	 *            the serial port.
	 * @param out
	 *            the buffer populate.
	 * @return the listener.
	 */
	protected MNPSerialPortEventListener createPortListener(SerialPort port, OutputStream out) {
		return new MNPSerialPortEventListener(port, out);
	}

}
