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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import net.sf.jncu.io.NoSuchPortException;
import net.sf.jncu.io.PortInUseException;

/**
 * Trace serial port traffic.
 * <p>
 * The program traces traffic sent and received between two serial ports. At
 * least of the ports would be a null-port (virtual port), for example:<br>
 * <tt>device</tt> <-> <tt>COM1</tt> <-> <tt>CommTrace</tt> <->
 * <tt>COM2-COM3</tt> <-> <tt>application</tt><br>
 * or<br>
 * <tt>device</tt> <-> <tt>COM2-COM3</tt> <-> <tt>CommTrace</tt> <->
 * <tt>COM4-COM5</tt> <-> <tt>application</tt>
 * 
 * @author moshew
 */
public class CommTrace implements SerialPortEventListener {

	/** Incoming bytes. */
	public static final char CHAR_DIRECTION_1TO2 = '>';
	/** Outgoing bytes. */
	public static final char CHAR_DIRECTION_2TO1 = '<';
	/** System property name for a filter class. */
	public static final String PROPERTY_FILTER = "jncu.cdil.mnp.MNPSerialPortFilter";
	private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/** Port attached to Newton. */
	private SerialPort portN;
	/** Port attached to PC. */
	private SerialPort portP;
	private PrintStream logOut = System.out;
	private int logWidth = 0;
	private int baud = MNPSerialPort.BAUD_38400;
	/** List of filters on the outgoing stream, e.g. from NCU to Newton. */
	private final Collection<MNPSerialPortFilter> filters = new ArrayList<MNPSerialPortFilter>();

	/**
	 * Creates a new trace.
	 */
	public CommTrace() {
		super();

		String filterClassName = System.getProperty(PROPERTY_FILTER);
		if (filterClassName != null) {
			Class<?> clazz;
			MNPSerialPortFilter filter;
			try {
				clazz = Class.forName(filterClassName);
				filter = (MNPSerialPortFilter) clazz.newInstance();
				addFilter(filter);
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			} catch (InstantiationException ie) {
				ie.printStackTrace();
			} catch (IllegalAccessException iae) {
				iae.printStackTrace();
			}
		}
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		if ((args == null) || (args.length < 2)) {
			System.out.println("args: port1 port2 [baud [file]]");
			System.exit(1);
			return;
		}

		CommTrace tracer = new CommTrace();
		try {
			if (args.length > 2) {
				tracer.setBaud(Integer.parseInt(args[2]));
				if (args.length > 3) {
					tracer.setOutput(new File(args[3]));
				}
			}
			tracer.trace(args[0], args[1]);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		}
	}

	public void trace(String portName1, String portName2) throws NoSuchPortException, PortInUseException, SerialPortException {
		SerialPort port1 = new SerialPort(portName1);
		SerialPort port2 = new SerialPort(portName2);
		try {
			port1.openPort();
			port2.openPort();
			trace(port1, port2);
		} catch (SerialPortException se) {
			if (SerialPortException.TYPE_PORT_BUSY == se.getExceptionType())
				throw new PortInUseException(portName1);
			if (SerialPortException.TYPE_PORT_NOT_FOUND == se.getExceptionType())
				throw new NoSuchPortException(portName1);
			throw se;
		}
	}

	public void trace(SerialPort port1, SerialPort port2) throws SerialPortException {
		this.portN = port1;
		this.portP = port2;

		port1.setParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		port2.setParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		port1.setEventsMask(SerialPort.MASK_RXCHAR);
		port2.setEventsMask(SerialPort.MASK_RXCHAR);
		port1.addEventListener(this);
		port2.addEventListener(this);
	}

	@Override
	public void serialEvent(SerialPortEvent e) {
		String portName = e.getPortName();
		if (e.isRXCHAR()) {
			int count = e.getEventValue();
			if (count == 0)
				return;
			char direction;
			SerialPort in;
			SerialPort out;
			byte[] buf;

			try {
				if (portName.equals(portN.getPortName())) {
					direction = CHAR_DIRECTION_1TO2;
					in = portN;
					out = portP;
				} else {
					direction = CHAR_DIRECTION_2TO1;
					in = portP;
					out = portN;
				}

				synchronized (in) {
					buf = in.readBytes(count);
					if (in == portP)
						buf = filter(buf);
					if (buf != null) {
						for (byte b : buf) {
							logOut.print(direction);
							logOut.print(HEX[(b >> 4) & 0x0F]);
							logOut.print(HEX[b & 0x0F]);
							logWidth++;
							if (logWidth >= 32) {
								logOut.println();
								logWidth = 0;
							}
						}
						out.writeBytes(buf);
					}
				}
			} catch (SerialPortException se) {
				se.printStackTrace();
			}
		} else if (e.isERR()) {
			cancel();
		}
	}

	/**
	 * Set the trace output.
	 * 
	 * @param file
	 *            the output file.
	 * @throws FileNotFoundException
	 *             if file is not found.
	 */
	public void setOutput(File file) throws FileNotFoundException {
		setOutput(new FileOutputStream(file));
	}

	/**
	 * Set the trace output.
	 * 
	 * @param out
	 *            the output.
	 */
	public void setOutput(OutputStream out) {
		setOutput(new PrintStream(out));
	}

	/**
	 * Set the trace output.
	 * 
	 * @param out
	 *            the output.
	 */
	public void setOutput(PrintStream out) {
		this.logOut = out;
	}

	/**
	 * Set the baud rate.
	 * 
	 * @param baud
	 *            the baud rate.
	 */
	public void setBaud(int baud) {
		this.baud = baud;
	}

	/**
	 * Cancel tracing.
	 */
	public void cancel() {
		try {
			finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			portN.closePort();
		} catch (SerialPortException se) {
			// ignore
		}
		try {
			portP.closePort();
		} catch (SerialPortException se) {
			// ignore
		}
		super.finalize();
	}

	/**
	 * Add a packet filter.
	 * 
	 * @param filter
	 *            the filter to add.
	 */
	public void addFilter(MNPSerialPortFilter filter) {
		if (!filters.contains(filter)) {
			filters.add(filter);
		}
	}

	/**
	 * Remove a packet filter.
	 * 
	 * @param filter
	 *            the filter to remove.
	 */
	public void removeFilter(MNPSerialPortFilter filter) {
		filters.remove(filter);
	}

	/**
	 * Filter the packet.
	 * 
	 * @param buf
	 *            the input buffer.
	 * @return the filter buffer - {@code null} otherwise.
	 */
	protected byte[] filter(byte[] buf) {
		for (MNPSerialPortFilter filter : filters) {
			buf = filter.filterSerialPort(buf);
			if (buf == null)
				return null;
		}
		return buf;
	}
}
