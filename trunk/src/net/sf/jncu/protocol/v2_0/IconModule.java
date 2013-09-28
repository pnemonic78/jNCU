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

import java.awt.Window;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ProgressMonitor;

import net.sf.jncu.JNCUApp;
import net.sf.jncu.JNCUResources;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommand;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.session.DDisconnect;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceled2;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;

/**
 * Module that does some function when user clicks an icon.
 * 
 * @author Moshe
 */
public abstract class IconModule extends Thread implements DockCommandListener {

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
	protected final CDPipe pipe;
	private final List<IconModuleListener> listeners = new ArrayList<IconModuleListener>();
	private ProgressMonitor progressMonitor;
	private IDockCommandFromNewton progressCommandFrom;
	private IDockCommandToNewton progressCommandTo;
	private Window owner;
	private String progressReceivingFormat;
	private String progressSendingFormat;
	private boolean cancelled;

	/**
	 * Constructs a new module.
	 * 
	 * @param title
	 *            the title.
	 * @param pipe
	 *            the pipe.
	 * @param owner
	 *            the owner window.
	 */
	public IconModule(String title, CDPipe pipe, Window owner) {
		super();
		setName("IconModule-" + getId());
		this.title = title;
		this.owner = owner;
		if (pipe == null)
			throw new IllegalArgumentException("pipe required");
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

	/**
	 * Get the owner window.
	 * 
	 * @return the owner.
	 */
	protected Window getOwner() {
		return owner;
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
			showError("write", e);
			if (!DOperationDone.COMMAND.equals(command.getCommand())) {
				writeDone();
			}
		}
	}

	/**
	 * Show the error to the user.
	 * 
	 * @param msg
	 *            the message.
	 * @param e
	 *            the error.
	 */
	protected void showError(String msg, Throwable e) {
		JNCUApp.showError(getOwner(), msg, e);
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
		IconModule.this.interrupt();
	}

	/**
	 * Module sends notification to Newton to finish.
	 */
	protected void writeDone() {
		write(new DOperationDone());
	}

	/**
	 * Module sends notification to Newton to cancel.
	 */
	protected void writeCancel() {
		pipe.clearCommands();
		write(new DOperationCanceled());
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
			monitor.setNote(String.format(progressReceivingFormat, (progress * 100) / total));
			if (monitor.isCanceled()) {
				cancel();
			}
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

		final String cmd = command.getCommand();

		if (DOperationCanceled.COMMAND.equals(cmd)) {
			cancelReceived();
		} else if (DOperationCanceled2.COMMAND.equals(cmd)) {
			cancelReceived();
		} else if (DDisconnect.COMMAND.equals(cmd)) {
			fireCancelled();
			done();
		}
	}

	@Override
	public void commandSending(IDockCommandToNewton command, int progress, int total) {
		if (!isEnabled())
			return;
		progressCommandTo = command;
		final ProgressMonitor monitor = getProgress();
		if (monitor != null) {
			monitor.setMaximum(total);
			monitor.setProgress(progress);
			monitor.setNote(String.format(progressSendingFormat, (progress * 100) / total));
			if (monitor.isCanceled()) {
				cancel();
			}
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

		final String cmd = command.getCommand();

		if (DOperationDone.COMMAND.equals(cmd)) {
			fireDone();
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
				progressMonitor = new ProgressMonitor(getOwner(), getTitle(), "0%%", 0, IDockCommand.LENGTH_WORD);
				progressMonitor.setMillisToDecideToPopup(0);
			}
		}
		progressReceivingFormat = JNCUResources.getString("progressReceiving");
		progressSendingFormat = JNCUResources.getString("progressSending");
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
		cancelSend();
	}

	/**
	 * Send cancellation to Newton.
	 */
	protected void cancelSend() {
		if (cancelled)
			return;
		cancelled = true;

		new Thread() {
			@Override
			public void run() {
				cancelSendRun();
			}
		}.start();
	}

	/**
	 * Send cancellation to Newton.
	 */
	protected void cancelSendRun() {
		writeCancel();
		fireCancelled();
		try {
			// Enough time to send the cancel and receive an
			// acknowledge.
			sleep(2000);
		} catch (InterruptedException e) {
		}
		done();
	}

	/**
	 * Received cancellation from Newton.
	 */
	protected void cancelReceived() {
		if (cancelled)
			return;
		cancelled = true;

		new Thread() {
			@Override
			public void run() {
				cancelReceivedRun();
			}
		}.start();
	}

	/**
	 * Received cancellation from Newton.
	 */
	protected void cancelReceivedRun() {
		pipe.clearCommands();
		write(new DOperationCanceledAck());
		try {
			// Enough time to send the cancel and receive an
			// acknowledge.
			sleep(2000);
		} catch (InterruptedException e) {
		}
		fireCancelled();
		done();
	}
}
