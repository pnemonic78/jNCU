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
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

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
	 *            the queue for populating data.
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
	 *            the queue for populating data.
	 */
	public MNPSerialPortEventListener(SerialPort port, OutputStream out) {
		super();
		this.port = port;
		this.out = out;
		this.q = null;
	}

	/*
	 * (non-Javadoc)
	 * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
	 */
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
			breakInterrupt(event);
			break;
		case SerialPortEvent.CD:
			carrierDetect(event);
			break;
		case SerialPortEvent.CTS:
			clearToSend(event);
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			dataAvailable(event);
			break;
		case SerialPortEvent.DSR:
			dataSetReady(event);
			break;
		case SerialPortEvent.FE:
			framingError(event);
			break;
		case SerialPortEvent.OE:
			overrunError(event);
			break;
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			outputBufferEmpty(event);
			break;
		case SerialPortEvent.PE:
			parityError(event);
			break;
		case SerialPortEvent.RI:
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
	 * Handle parity error events.
	 * 
	 * @param event
	 *            the event.
	 */
	protected void parityError(SerialPortEvent event) {
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
	 * Handle overrun rrror events.
	 * 
	 * @param event
	 *            the event.
	 */
	protected void overrunError(SerialPortEvent event) {
	}

	/**
	 * Handle framing error events.
	 * 
	 * @param event
	 *            the event.
	 */
	protected void framingError(SerialPortEvent event) {
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
	 * Handle data available events.
	 * 
	 * @param event
	 *            the event.
	 */
	protected void dataAvailable(SerialPortEvent event) {
		SerialPort port = (SerialPort) event.getSource();
		InputStream in;
		int b;
		try {
			in = port.getInputStream();
			b = in.read();
			while (b != -1) {
				if (out == null) {
					q.put((byte) b);
				} else {
					out.write(b);
				}
				b = in.read();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
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
	 * Handle carrier detect events.
	 * 
	 * @param event
	 *            the event.
	 */
	protected void carrierDetect(SerialPortEvent event) {
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
		if (q != null) {
			q.clear();
		}
	}

}
