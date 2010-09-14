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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v2_0.DockCommandFactory;

/**
 * CDIL command layer.
 * 
 * @author moshew
 */
public abstract class CDCommandLayer extends Thread {

	/** List of command listeners. */
	protected final Collection<DockCommandListener> listeners = new ArrayList<DockCommandListener>();
	/** Queue of incoming commands. */
	protected final BlockingQueue<IDockCommandFromNewton> queueIn = new LinkedBlockingQueue<IDockCommandFromNewton>();
	/** Still listening for incoming commands? */
	protected boolean running = false;

	/**
	 * Creates a new command layer.
	 */
	public CDCommandLayer() {
		super();
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
	 * Close the layer and release resources.
	 */
	public void close() {
		running = false;
		listeners.clear();
		try {
			getInput().close();
		} catch (IOException ioe) {
			// Ignore.
		}
	}

	/**
	 * Sends the given command to the Newton device.
	 * 
	 * @param cmd
	 *            the command.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public abstract void write(IDockCommandToNewton cmd) throws IOException;

	/**
	 * Get the blocking input stream with commands.
	 * 
	 * @return the stream.
	 */
	protected abstract InputStream getInput();

	@Override
	public void run() {
		running = true;

		DockCommandFactory factory = DockCommandFactory.getInstance();
		IDockCommandFromNewton cmd;
		InputStream in = getInput();

		try {
			while (running) {
				cmd = (IDockCommandFromNewton) factory.create(in);
				if (cmd != null) {
					cmd.decode(in);
					try {
						queueIn.put(cmd);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Read the next available command.
	 * 
	 * @return the command.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public IDockCommandFromNewton read() throws TimeoutException {
		try {
			return queueIn.take();
		} catch (InterruptedException ie) {
			throw new TimeoutException(ie.getMessage());
		}
	}
}
