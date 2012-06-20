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
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * MNP serial port reader.
 * 
 * @author moshew
 */
public class MNPSerialPortReader extends Thread implements Closeable {

	/** System property name for a reader filter class. */
	public static final String PROPERTY_FILTER = "jncu.cdil.mnp.MNPSerialPortReaderFilter";

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
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	@SuppressWarnings("unchecked")
	public MNPSerialPortReader(SerialPort port) throws IOException {
		super();
		setName("MNPSerialPortReader-" + getId());
		this.port = port;
		this.in = new PipedInputStream(data);
		this.listener = createPortListener(port, data);
		try {
			port.addEventListener(listener);
		} catch (SerialPortException se) {
			throw new IOException(se.getMessage());
		}

		String filterClassName = System.getProperty(PROPERTY_FILTER);
		if (filterClassName != null) {
			Class<? extends FilterInputStream> clazz;
			try {
				clazz = (Class<? extends FilterInputStream>) Class.forName(filterClassName);
				addFilter(clazz);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void close() throws IOException {
		// TODO flush the filters.

		if (listener != null) {
			listener.close();
			listener = null;
			try {
				port.removeEventListener();
			} catch (SerialPortException se) {
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

	/**
	 * Add a stream filter. Wraps the input stream.
	 * 
	 * @param filterClass
	 *            the filter class to create and add. Must have a constructor
	 *            with {@code InputStream} parameter.
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 */
	public void addFilter(Class<? extends FilterInputStream> filterClass) throws IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException {
		Constructor<? extends FilterInputStream> cons;
		FilterInputStream filter;
		try {
			cons = filterClass.getConstructor(InputStream.class);
			filter = cons.newInstance(getInputStream());
			this.in = filter;
		} catch (NoSuchMethodException nsme) {
			throw new IllegalArgumentException(nsme);
		}
	}
}
