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

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import net.sf.jncu.protocol.DockCommand;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommand;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v2_0.DockCommandFactory;

/**
 * CDIL command layer.
 * 
 * @author moshew
 * @param <P>
 */
public abstract class CDCommandLayer<P extends CDPacket> extends Thread implements CDPacketListener<P> {

	/** The packet layer. */
	protected final CDPacketLayer<P> packetLayer;
	/** List of command listeners. */
	protected final Collection<DockCommandListener> listeners = new ArrayList<DockCommandListener>();
	/** Still listening for incoming commands? */
	protected boolean running;

	/**
	 * Creates a new command layer.
	 * 
	 * @param packetLayer
	 *            the packet layer.
	 */
	public CDCommandLayer(CDPacketLayer<P> packetLayer) {
		super();
		setName("CDCommandLayer-" + getId());
		this.packetLayer = packetLayer;
		packetLayer.addPacketListener(this);
	}

	/**
	 * Add a command listener.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addCommandListener(DockCommandListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Remove a command listener.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeCommandListener(DockCommandListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notify all the listeners that a command has been received.
	 * 
	 * @param command
	 *            the received command.
	 */
	protected void fireCommandReceived(IDockCommandFromNewton command) {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<DockCommandListener> listenersCopy = new ArrayList<DockCommandListener>(listeners);
		for (DockCommandListener listener : listenersCopy) {
			listener.commandReceived(command);
		}
	}

	/**
	 * Notify all the listeners that a command has been sent.
	 * 
	 * @param command
	 *            the sent command.
	 */
	protected void fireCommandSent(IDockCommandToNewton command) {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<DockCommandListener> listenersCopy = new ArrayList<DockCommandListener>(listeners);
		for (DockCommandListener listener : listenersCopy) {
			listener.commandSent(command);
		}
	}

	/**
	 * Notify all the listeners that a command is being received.
	 * 
	 * @param command
	 *            the receiving command.
	 * @param progress
	 *            the number of bytes received.
	 * @param total
	 *            the total number of bytes to receive.
	 */
	protected void fireCommandReceiving(IDockCommandFromNewton command, int progress, int total) {
		// Avoid "divide by 0" error.
		if (total == 0)
			return;
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<DockCommandListener> listenersCopy = new ArrayList<DockCommandListener>(listeners);
		for (DockCommandListener listener : listenersCopy) {
			listener.commandReceiving(command, progress, total);
		}
	}

	/**
	 * Notify all the listeners that a command is being sent.
	 * 
	 * @param command
	 *            the sending command.
	 * @param progress
	 *            the number of bytes sent.
	 * @param total
	 *            the total number of bytes to send.
	 */
	protected void fireCommandSending(IDockCommandToNewton command, int progress, int total) {
		// Avoid "divide by 0" error.
		if (total == 0)
			return;
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<DockCommandListener> listenersCopy = new ArrayList<DockCommandListener>(listeners);
		for (DockCommandListener listener : listenersCopy) {
			listener.commandSending(command, progress, total);
		}
	}

	/**
	 * Notify all the listeners that no more commands will be available.
	 */
	protected void fireCommandEOF() {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<DockCommandListener> listenersCopy = new ArrayList<DockCommandListener>(listeners);
		for (DockCommandListener listener : listenersCopy) {
			listener.commandEOF();
		}
	}

	/**
	 * Close the layer and release resources.
	 */
	public void close() {
		running = false;
		packetLayer.removePacketListener(this);
		listeners.clear();
		try {
			getInput().close();
		} catch (IOException ioe) {
			// Ignore.
		}
		interrupt();
	}

	/**
	 * Sends the given command to the Newton device.
	 * 
	 * @param cmd
	 *            the command.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public abstract void write(IDockCommandToNewton cmd) throws IOException, TimeoutException;

	/**
	 * Get the blocking input stream with commands.
	 * 
	 * @return the stream.
	 */
	protected abstract InputStream getInput();

	/**
	 * Get the output stream for commands.
	 * 
	 * @return the stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected abstract OutputStream getOutput() throws IOException;

	/**
	 * Wait for incoming commands.
	 */
	@Override
	public void run() {
		running = true;

		InputStream in;
		IDockCommand cmd = null;
		boolean hasCommand;
		int length = 0;
		final byte[] lengthBytes = new byte[IDockCommand.LENGTH_WORD];
		byte[] data = null;
		int offset = 0;
		int available;
		int count;
		final Vector<InputStream> v = new Vector<InputStream>();

		try {
			do {
				hasCommand = false;

				in = getInput();
				while (running && isAlive() && !isInterrupted() && (in.available() == 0)) {
					synchronized (in) {
						try {
							in.wait(1000);
						} catch (InterruptedException ie) {
							// Probably received some data, or closed.
						}
					}
				}

				if (running && isAlive() && !isInterrupted()) {
					if (cmd == null) {
						if (DockCommand.isCommand(in)) {
							cmd = DockCommandFactory.getInstance().create(in);
							length = DockCommandFromNewton.ntohl(in);
							// Need to put the length back into the command
							// stream for decoding.
							lengthBytes[0] = (byte) ((length >> 24) & 0xFF);
							lengthBytes[1] = (byte) ((length >> 16) & 0xFF);
							lengthBytes[2] = (byte) ((length >> 8) & 0xFF);
							lengthBytes[3] = (byte) ((length >> 0) & 0xFF);
							v.clear();
							v.add(new ByteArrayInputStream(lengthBytes));
						}
					}
					available = in.available();
					if (cmd != null) {
						hasCommand = true;

						if (cmd instanceof IDockCommandFromNewton) {
							IDockCommandFromNewton cmdFromNewton = (IDockCommandFromNewton) cmd;
							if (((offset == 0) && (available < length)) || (offset > 0)) {
								if (data == null) {
									data = new byte[length];
									offset = 0;
								}
								count = in.read(data, offset, Math.min(available, length - offset));
								if (count >= 0) {
									offset += count;
									fireCommandReceiving(cmdFromNewton, offset, length);
								}
							}
							if (((offset == 0) && (available >= length)) || (offset == length)) {
								if (data != null)
									v.add(new ByteArrayInputStream(data));
								v.add(in);
								in = new SequenceInputStream(v.elements());
								cmdFromNewton.decode(in);

								fireCommandReceived(cmdFromNewton);
								cmd = null;
								length = 0;
								data = null;
								offset = 0;
								v.clear();
							}
						} else if (cmd instanceof IDockCommandToNewton) {
							IDockCommandToNewton cmdToNewton = (IDockCommandToNewton) cmd;
							if (((offset == 0) && (available < length)) || (offset > 0)) {
								count = (int) in.skip(Math.min(available, length - offset));
								if (count >= 0) {
									offset += count;
									fireCommandSending(cmdToNewton, offset, length);
								}
							}
							if (((offset == 0) && (available >= length)) || (offset == length)) {
								// Commands are 4-byte aligned.
								switch (length & 3) {
								case 1:
									length++;
								case 2:
									length++;
								case 3:
									length++;
									break;
								}
								in.skip(length);

								fireCommandSent(cmdToNewton);
								cmd = null;
								length = 0;
								data = null;
								offset = 0;
								v.clear();
							}
						}
					}
				}
				yield();
			} while (running && hasCommand && isAlive() && !isInterrupted());
		} catch (EOFException eofe) {
			// eofe.printStackTrace();
			fireCommandEOF();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		running = false;
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
	public void setTimeout(int timeoutInSecs) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		packetLayer.setTimeout(timeoutInSecs);
	}

	/**
	 * Get the timeout period.
	 * 
	 * @return the timeout in seconds.
	 */
	public int getTimeout() {
		return packetLayer.getTimeout();
	}

	@Override
	public void packetReceived(P packet) {
	}

	@Override
	public void packetSent(P packet) {
	}

	@Override
	public void packetAcknowledged(P packet) {
	}

	@Override
	public void packetEOF() {
		fireCommandEOF();
	}
}
