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
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.TooManyListenersException;

/**
 * Trace serial port traffic.
 * <p>
 * The program traces traffic sent and received between two serial ports. At
 * least of the ports would be a null-port (virtual port), for example:<br>
 * <tt>device</tt> <-> <tt>COM1</tt> <-> <tt>CommTrace</tt> <->
 * <tt>COM2-COM3</tt> <-> <tt>application</tt><br>
 * or<br>
 * <tt>device</tt> <-> <tt>COM2-COM3</tt> <-> <tt>CommTrace</tt> <->
 * <tt>COM4-COM5</tt> <-> <tt>application</tt><br>
 * <p>
 * Must specify VM arguments for the Java library path variable
 * <tt>java.library.path</tt> at start-up:
 * <ul>
 * <li>for Linux i686:
 * <code>-Djava.library.path=<em>${user.dir}</em>/lib;<em>${user.dir}</em>/lib/Linux/i686</code>
 * <li>for Linux ia64:
 * <code>-Djava.library.path=<em>${user.dir}</em>/lib;<em>${user.dir}</em>/lib/Linux/ia64</code>
 * <li>for Linux x86_64 (x64):
 * <code>-Djava.library.path=<em>${user.dir}</em>/lib;<em>${user.dir}</em>/lib/Linux/x86_64</code>
 * <li>for Mac OS X:
 * <code>-Djava.library.path=<em>${user.dir}</em>/lib;<em>${user.dir}</em>/lib/Mac_OS_X</code>
 * <li>for Solaris SPARC 32:
 * <code>-Djava.library.path=<em>${user.dir}</em>/lib;<em>${user.dir}</em>/lib/Solaris/sparc32</code>
 * <li>for Solaris SPARC 64:
 * <code>-Djava.library.path=<em>${user.dir}</em>/lib;<em>${user.dir}</em>/lib/Solaris/sparc64</code>
 * <li>for Windows x86:
 * <code>-Djava.library.path="<em>${user.dir}</em>\lib";"<em>${user.dir}</em>\lib\Windows\x86"</code>
 * <li>for Windows ia64:
 * <code>-Djava.library.path="<em>${user.dir}</em>\lib";"<em>${user.dir}</em>\lib\Windows\ia64"</code>
 * <li>for Windows x86_64:
 * <code>-Djava.library.path="<em>${user.dir}</em>\lib";"<em>${user.dir}</em>\lib\Windows\x86_64"</code>
 * </ul>
 * 
 * @author moshew
 */
public class CommTrace implements SerialPortEventListener {

	public static final char CHAR_DIRECTION_1TO2 = '>';
	public static final char CHAR_DIRECTION_2TO1 = '<';
	private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private SerialPort port1;
	private SerialPort port2;
	private PrintStream logOut = System.out;
	private int logWidth = 0;
	private int baud = MNPSerialPort.BAUD_38400;

	/**
	 * Creates a new trace.
	 */
	public CommTrace() {
		super();
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

	public void trace(String portName1, String portName2) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
		CommPortIdentifier portId1 = CommPortIdentifier.getPortIdentifier(portName1);
		CommPortIdentifier portId2 = CommPortIdentifier.getPortIdentifier(portName2);
		trace(portId1, portId2);
	}

	public void trace(CommPortIdentifier portId1, CommPortIdentifier portId2) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
		if (portId1.getPortType() != CommPortIdentifier.PORT_SERIAL) {
			throw new NoSuchPortException();
		}
		if (portId2.getPortType() != CommPortIdentifier.PORT_SERIAL) {
			throw new NoSuchPortException();
		}
		String owner = this.getClass().getName();
		SerialPort port1 = (SerialPort) portId1.open(owner, 30000);
		SerialPort port2 = (SerialPort) portId2.open(owner, 30000);
		trace(port1, port2);
	}

	public void trace(SerialPort port1, SerialPort port2) throws PortInUseException, UnsupportedCommOperationException {
		this.port1 = port1;
		this.port2 = port2;

		port1.setSerialPortParams(baud, port1.getDataBits(), port1.getStopBits(), port1.getParity());
		port2.setSerialPortParams(baud, port2.getDataBits(), port2.getStopBits(), port2.getParity());
		port1.notifyOnDataAvailable(true);
		port2.notifyOnDataAvailable(true);

		try {
			port1.addEventListener(this);
			port2.addEventListener(this);
		} catch (TooManyListenersException tmle) {
			throw new PortInUseException();
		}
	}

	@Override
	public void serialEvent(SerialPortEvent e) {
		SerialPort port = (SerialPort) e.getSource();
		if (e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			char direction;

			try {
				InputStream in = port.getInputStream();
				OutputStream out = null;

				if (port == port1) {
					direction = CHAR_DIRECTION_1TO2;
					out = port2.getOutputStream();
				} else {
					direction = CHAR_DIRECTION_2TO1;
					out = port1.getOutputStream();
				}

				int b = in.read();
				do {
					synchronized (logOut) {
						logOut.print(direction);
						logOut.print(HEX[(b >> 4) & 0x0F]);
						logOut.print(HEX[b & 0x0F]);
						logWidth++;
						if (logWidth >= 32) {
							logOut.println();
							logWidth = 0;
						}
					}
					out.write(b);
					b = in.read();
				} while (b != -1);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
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
	 *            the baude rate.
	 */
	public void setBaud(int baud) {
		this.baud = baud;
	}
}
