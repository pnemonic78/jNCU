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
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 * Listener to handle all serial port events.
 * 
 * @author moshew
 */
public class MNPSerialPortEventListener implements SerialPortEventListener {

	protected final SerialPort port;
	private final BlockingQueue<Byte> q;
	private final OutputStream out;

	/**
	 * Creates a new port event listener.
	 * 
	 * @param port
	 *            the port.
	 * @param queue
	 *            the buffer for populating data.
	 */
	public MNPSerialPortEventListener(SerialPort port, BlockingQueue<Byte> queue) {
		super();
		this.port = port;
		this.out = null;
		this.q = queue;
	}

	/**
	 * Creates a new port event listener.
	 * 
	 * @param port
	 *            the port.
	 * @param out
	 *            the buffer for populating data.
	 */
	public MNPSerialPortEventListener(SerialPort port, OutputStream out) {
		super();
		this.port = port;
		this.out = out;
		this.q = null;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BREAK:
			breakInterrupt(event);
			break;
		case SerialPortEvent.CTS:
			clearToSend(event);
			break;
		case SerialPortEvent.RXCHAR:
			dataAvailable(event);
			break;
		case SerialPortEvent.DSR:
			dataSetReady(event);
			break;
		case SerialPortEvent.ERR:
			error(event);
			break;
		case SerialPortEvent.TXEMPTY:
			outputBufferEmpty(event);
			break;
		case SerialPortEvent.RING:
			ringIndicator(event);
			break;
		}
	}

	/**
	 * Handle ring indicator events.
	 * 
	 * @param event
	 *            the event.
	 */
	protected void ringIndicator(SerialPortEvent event) {
	}

	/**
	 * Handle output buffer empty events.
	 * 
	 * @param event
	 *            the event.
	 */
	protected void outputBufferEmpty(SerialPortEvent event) {
	}

	/**
	 * Handle error events.
	 * 
	 * @param event
	 *            the event.
	 */
	protected void error(SerialPortEvent event) {
	}

	/**
	 * Handle data set ready events.
	 * 
	 * @param event
	 *            the event.
	 */
	protected void dataSetReady(SerialPortEvent event) {
	}

	/**
	 * Handle data available events.<br>
	 * This method is <tt>synchronized</tt> to try and keep the input sequential
	 * and correctly ordered.
	 * 
	 * @param event
	 *            the event.
	 */
	protected synchronized void dataAvailable(SerialPortEvent event) {
		String portName = event.getPortName();
		if (!portName.equals(port.getPortName()))
			return;
		int count = event.getEventValue();
		if (count == 0)
			return;
		byte[] buf;

		try {
			buf = port.readBytes(count);
			if (q != null) {
				for (byte b : buf)
					q.put(b);
			} else {
				out.write(buf);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (SerialPortException se) {
			se.printStackTrace();
		}
	}

	/**
	 * Handle clear to send events.
	 * 
	 * @param event
	 *            the event.
	 */
	protected void clearToSend(SerialPortEvent event) {
	}

	/**
	 * Handle break interrupt events.
	 * 
	 * @param event
	 *            the event.
	 */
	protected void breakInterrupt(SerialPortEvent event) {
	}

	/**
	 * Listener has been removed and needs to free resources.
	 */
	public void close() {
		if (out != null) {
			try {
				out.close();
			} catch (Exception e) {
				// ignore
			}
		}
	}

}
