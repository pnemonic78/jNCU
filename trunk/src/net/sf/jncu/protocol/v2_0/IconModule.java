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
package net.sf.jncu.protocol.v2_0;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import net.sf.jncu.cdil.CDPacket;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommand;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.swing.SwingUtils;

/**
 * Module that does some function when user clicks an icon.
 * 
 * @author Moshe
 */
public abstract class IconModule extends Thread implements DockCommandListener {

	static {
		SwingUtils.init();
	}

	/**
	 * Icon module event.
	 */
	public static interface IconModuleListener {
		/**
		 * The operation completed successfully.
		 * 
		 * @param module
		 *            the module.
		 */
		public void successModule(IconModule module);

		/**
		 * The operation was cancelled.
		 * 
		 * @param module
		 *            the module.
		 */
		public void cancelModule(IconModule module);
	}

	private final String title;
	protected final CDPipe<? extends CDPacket> pipe;
	private final List<IconModuleListener> listeners = new ArrayList<IconModuleListener>();
	private ProgressMonitor progressMonitor;
	private IDockCommandFromNewton progressCommandFrom;
	private IDockCommandToNewton progressCommandTo;

	/**
	 * Constructs a new module.
	 * 
	 * @param title
	 *            the title.
	 * @param pipe
	 *            the pipe.
	 */
	public IconModule(String title, CDPipe<? extends CDPacket> pipe) {
		super();
		setName("IconModule-" + getId());
		this.title = title;
		if (pipe == null)
			throw new NullPointerException("pipe required");
		this.pipe = pipe;
		pipe.addCommandListener(this);
	}

	/**
	 * Get the module title.
	 * 
	 * @return the title.
	 */
	public String getTitle() {
		return title;
	}

	@Override
	public void commandEOF() {
		done();
	}

	/**
	 * Send a command.
	 * 
	 * @param command
	 *            the command.
	 */
	protected void write(IDockCommandToNewton command) {
		try {
			if (pipe.allowSend())
				pipe.write(command);
		} catch (Exception e) {
			e.printStackTrace();
			if (!DOperationDone.COMMAND.equals(command.getCommand())) {
				DOperationDone cancel = new DOperationDone();
				write(cancel);
			}
			showError(e.getMessage());
		}
	}

	/**
	 * Show the error to the user.
	 * 
	 * @param msg
	 *            the error message.
	 */
	protected void showError(final String msg) {
		new Thread() {
			public void run() {
				JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
			}
		}.start();
	}

	/**
	 * Module is finished.
	 */
	protected void done() {
		pipe.removeCommandListener(this);
		if (progressMonitor != null) {
			progressMonitor.close();
			progressMonitor = null;
		}
		interrupt();
	}

	/**
	 * Module sends notification to Newton to finish.
	 */
	protected void writeDone() {
		DOperationDone done = new DOperationDone();
		write(done);
	}

	/**
	 * Module sends notification to Newton to cancel.
	 */
	protected void writeCancel() {
		DOperationCanceled cancel = new DOperationCanceled();
		write(cancel);
	}

	/**
	 * Is the module enabled and active?
	 * 
	 * @return {@code true} if enabled.
	 */
	protected boolean isEnabled() {
		return !isInterrupted();
	}

	@Override
	public void commandReceiving(IDockCommandFromNewton command, int progress, int total) {
		if (!isEnabled())
			return;
		progressCommandFrom = command;
		ProgressMonitor monitor = getProgress();
		if (monitor != null) {
			monitor.setMaximum(total);
			monitor.setProgress(progress);
			monitor.setNote(String.format("Receiving %d%%", (progress * 100) / total));
		}
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		if (command == progressCommandFrom) {
			progressCommandFrom = null;
			closeProgress();
		}
		if (!isEnabled())
			return;

		String cmd = command.getCommand();

		if (DOperationCanceled.COMMAND.equals(cmd)) {
			DOperationCanceledAck ack = new DOperationCanceledAck();
			write(ack);
		}
	}

	@Override
	public void commandSending(IDockCommandToNewton command, int progress, int total) {
		if (!isEnabled())
			return;
		progressCommandTo = command;
		ProgressMonitor monitor = getProgress();
		if (monitor != null) {
			monitor.setMaximum(total);
			monitor.setProgress(progress);
			monitor.setNote(String.format("Sending %d%%", (progress * 100) / total));
		}
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		if (command == progressCommandTo) {
			progressCommandTo = null;
			closeProgress();
		}
		if (!isEnabled())
			return;

		String cmd = command.getCommand();

		if (DOperationDone.COMMAND.equals(cmd)) {
			fireDone();
			done();
		} else if (DOperationCanceled.COMMAND.equals(cmd)) {
			fireCancelled();
			done();
		} else if (DOperationCanceledAck.COMMAND.equals(cmd)) {
			fireCancelled();
			done();
		}
	}

	/**
	 * Add a module listener.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addListener(IconModuleListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Remove a module listener.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeListener(IconModuleListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notify all the listeners that the operation finished.
	 */
	protected void fireDone() {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<IconModuleListener> listenersCopy = new ArrayList<IconModuleListener>(listeners);
		for (IconModuleListener listener : listenersCopy) {
			listener.successModule(this);
		}
	}

	/**
	 * Notify all the listeners that the operation was cancelled.
	 */
	protected void fireCancelled() {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<IconModuleListener> listenersCopy = new ArrayList<IconModuleListener>(listeners);
		for (IconModuleListener listener : listenersCopy) {
			listener.cancelModule(this);
		}
	}

	/**
	 * Get the progress monitor.
	 * 
	 * @return the progress.
	 */
	protected ProgressMonitor getProgress() {
		if (progressMonitor == null) {
			synchronized (this) {
				progressMonitor = new ProgressMonitor(null, getTitle(), "0%%", 0, IDockCommand.LENGTH_WORD);
			}
		}
		return progressMonitor;
	}

	/**
	 * Close the progress monitor.
	 */
	protected void closeProgress() {
		if (progressMonitor != null) {
			if ((progressCommandFrom == null) && (progressCommandTo == null))
				progressMonitor.close();
			progressMonitor = null;
		}
	}

	/**
	 * Cancel the operation.
	 */
	public void cancel() {
		DOperationCanceled cancel = new DOperationCanceled();
		write(cancel);
		try {
			// Enough time to send the cancel and receive an acknowledge.
			sleep(2000);
		} catch (InterruptedException e) {
		}
		done();
	}
}
