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
package net.sf.jncu.cdil;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.concurrent.TimeoutException;

/**
 * CD packet layer.
 * 
 * @author Moshe
 */
public abstract class CDPacketLayer<T extends CDPacket> {

	private boolean finished;
	private int timeout = 30;
	private final Timer timer = new Timer();
	private CDTimeout timeoutTask;
	/** List of packet listeners. */
	private final Collection<CDPacketListener<T>> listeners = new ArrayList<CDPacketListener<T>>();

	/**
	 * Constructs a new packet layer.
	 */
	public CDPacketLayer() {
		super();
	}

	/**
	 * Get the output stream for packets.
	 * 
	 * @return the stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected OutputStream getOutput() throws IOException {
		return null;
	}

	/**
	 * Get the input stream for packets.
	 * 
	 * @return the stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected InputStream getInput() throws IOException {
		return null;
	}

	/**
	 * Is finished?
	 * 
	 * @return finished.
	 */
	protected boolean isFinished() {
		return finished;
	}

	/**
	 * Add a packet listener.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addPacketListener(CDPacketListener<T> listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Remove a packet listener.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removePacketListener(CDPacketListener<T> listener) {
		listeners.remove(listener);
	}

	/**
	 * Notify all the listeners that a packet has been received.
	 * 
	 * @param packet
	 *            the received packet.
	 */
	protected void firePacketReceived(T packet) {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<CDPacketListener<T>> listenersCopy = new ArrayList<CDPacketListener<T>>(listeners);
		for (CDPacketListener<T> listener : listenersCopy) {
			listener.packetReceived(packet);
		}
	}

	/**
	 * Notify all the listeners that a packet has been sent.
	 * 
	 * @param packet
	 *            the sent packet.
	 */
	protected void firePacketSent(T packet) {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<CDPacketListener<T>> listenersCopy = new ArrayList<CDPacketListener<T>>(listeners);
		for (CDPacketListener<T> listener : listenersCopy) {
			listener.packetSent(packet);
		}
	}

	/**
	 * Notify all the listeners that no more packets will be available.
	 */
	protected void firePacketEOF() {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<CDPacketListener<T>> listenersCopy = new ArrayList<CDPacketListener<T>>(listeners);
		for (CDPacketListener<T> listener : listenersCopy) {
			listener.packetEOF();
		}
	}

	/**
	 * Close the layer and release resources.
	 */
	public void close() {
		timer.cancel();
		listeners.clear();
		finished = true;
	}

	/**
	 * Restart the timeout.
	 */
	protected void restartTimeout() {
		if (timeoutTask != null)
			timeoutTask.cancel();
		this.timeoutTask = new CDTimeout(null);
		timer.schedule(timeoutTask, timeout * 1000L);
	}

	/**
	 * Receive a packet.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @return the packet.
	 */
	public T receive() throws IOException {
		if (finished)
			return null;
		T packet = null;
		try {
			byte[] payload = read();
			packet = createPacket(payload);
			if (packet != null) {
				restartTimeout();
				firePacketReceived(packet);
			}
		} catch (EOFException eofe) {
			firePacketEOF();
		}
		return packet;
	}

	/**
	 * Receive a packet payload.
	 * 
	 * @return the payload - {@code null} otherwise.
	 * @throws EOFException
	 *             if end of stream is reached.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected abstract byte[] read() throws EOFException, IOException;

	/**
	 * Read a byte.
	 * 
	 * @return the byte value.
	 * @throws EOFException
	 *             if end of stream is reached.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected int readByte() throws IOException {
		InputStream in = getInput();
		int b;
		try {
			b = in.read();
		} catch (IOException ioe) {
			// PipedInputStream throws IOException instead of returning -1.
			if (in.available() == 0) {
				throw new EOFException();
			}
			throw ioe;
		}
		if (b == -1) {
			throw new EOFException();
		}
		return b;
	}

	/**
	 * Create a link packet, and decode.
	 * 
	 * @param payload
	 *            the payload.
	 * @return the packet.
	 */
	protected abstract T createPacket(byte[] payload);

	/**
	 * Listen for incoming packets until no more packets are available.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void listen() throws IOException {
		T packet;
		do {
			packet = receive();
		} while (packet != null);
	}

	/**
	 * Send a packet.
	 * 
	 * @param packet
	 *            the packet.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void send(T packet) throws IOException, TimeoutException {
		if (finished)
			return;
		byte[] payload = packet.serialize();
		try {
			write(payload);
			firePacketSent(packet);
		} catch (EOFException eof) {
			firePacketEOF();
		}
	}

	/**
	 * Send a packet.
	 * 
	 * @param payload
	 *            the payload.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void write(byte[] payload) throws IOException {
		write(payload, 0, payload.length);
	}

	/**
	 * Send a packet.
	 * 
	 * @param payload
	 *            the payload.
	 * @param offset
	 *            the frame offset.
	 * @param length
	 *            the frame length.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void write(byte[] payload, int offset, int length) throws IOException {
		OutputStream out = getOutput();
		if (out == null)
			return;
		out.write(payload, offset, length);
	}

	/**
	 * Sets the timeout period for CD_Read and CD_Write calls in a pipe.<br>
	 * <tt>DIL_Error CD_SetTimeout(CD_Handle pipe, long timeoutInSecs)</tt>
	 * <p>
	 * When the CDIL pipe is created, it is initialised with a default timeout
	 * period of 30 seconds. This timeout period is used to control
	 * <tt>CD_Read</tt> and <tt>CD_Write</tt> calls (and, indirectly, any
	 * flushing of outgoing data). Timeout values are specified on a per-pipe
	 * basis.<br>
	 * For <tt>CD_Read</tt>, if the requested number of bytes are not available
	 * after the timeout period, a <tt>kCD_Timeout</tt> error is returned and no
	 * bytes will be transferred. For <tt>CD_Write</tt>, if no data can be sent
	 * after the timeout period, a <tt>kCD_Timeout</tt> error is returned.<br>
	 * The timeout does not occur, if the data is presently being transferred.
	 * That is, a long operation does not fail due to a timeout. Note that an
	 * attempt is made to send data even if the timeout is set to zero seconds.
	 * 
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void setTimeout(int timeoutInSecs) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException {
		this.timeout = timeoutInSecs;
		restartTimeout();
	}

	/**
	 * Get the timeout period.
	 * 
	 * @return the timeout in seconds.
	 */
	public int getTimeout() {
		return timeout;
	}
}
