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
package net.sf.jncu.protocol.v2_0.app;

import java.io.File;

import javax.swing.JOptionPane;

import net.sf.jncu.cdil.CDPacket;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.app.DLoadPackage;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.swing.SwingUtils;

/**
 * Load package module.
 * 
 * @author Moshe
 */
public class LoadPackage implements DockCommandListener {

	static {
		SwingUtils.init();
	}

	protected enum State {
		None, Initialised, Requesting, Requested, Loading, Loaded, Cancelled, Finished
	}

	private static final String TITLE = "Load Package";

	private final CDPipe<? extends CDPacket> pipe;
	private File file;
	private Loader loader;
	private State state = State.None;

	/**
	 * Constructs a new loader.
	 * 
	 * @param pipe
	 *            the pipe.
	 * @param requested
	 *            loading was requested by Newton?
	 */
	public LoadPackage(CDPipe<? extends CDPacket> pipe, boolean requested) {
		super();
		if (pipe == null)
			throw new IllegalArgumentException("pipe required");
		this.pipe = pipe;
		pipe.addCommandListener(this);

		state = State.Initialised;

		// Newton wants to load package so skip Requesting and Requested states.
		if (requested)
			state = State.Loading;
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		if (state == State.Cancelled)
			return;
		if (state == State.Finished)
			return;

		String cmd = command.getCommand();

		if (DOperationCanceled.COMMAND.equals(cmd)) {
			// TODO Stop sending the package command.
			// pipe.cancel(load);
			// loader.kill();
			DOperationCanceledAck ack = new DOperationCanceledAck();
			send(ack);
			commandEOF();
			state = State.Cancelled;
		} else if (DResult.COMMAND.equals(cmd)) {
			final DResult result = (DResult) command;
			// Upload can begin or was finished?
			if (result.getErrorCode() == DResult.OK) {
				if (state == State.Requested) {
					state = State.Loading;
					loadPackage(file);
				} else if (state == State.Loaded) {
					DOperationDone done = new DOperationDone();
					send(done);
					commandEOF();
					state = State.Finished;
				}
			} else {
				commandEOF();
				state = State.Cancelled;
				showError(result.getError().getMessage() + "\nCode: " + result.getErrorCode());
			}
		} else if (DLoadPackageFile.COMMAND.equals(cmd)) {
			state = State.Loading;
		}
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		if (state == State.Cancelled)
			return;
		if (state == State.Finished)
			return;

		String cmd = command.getCommand();

		if (DRequestToInstall.COMMAND.equals(cmd)) {
			state = State.Requested;
		} else if (DLoadPackage.COMMAND.equals(cmd)) {
			state = State.Loaded;
		}
	}

	@Override
	public void commandEOF() {
		pipe.removeCommandListener(this);
	}

	/**
	 * Upload the package to the Newton.
	 * 
	 * @param file
	 */
	public void loadPackage(File file) {
		this.file = file;

		if (state == State.Initialised) {
			DRequestToInstall req = new DRequestToInstall();
			send(req);
		} else if (state == State.Loading) {
			loader = new Loader();
			loader.start();
		}
	}

	/**
	 * Send a command.
	 * 
	 * @param command
	 *            the command.
	 */
	protected void send(IDockCommandToNewton command) {
		try {
			if (pipe.canSend())
				pipe.write(command);
		} catch (Exception e) {
			e.printStackTrace();
			if (!DOperationDone.COMMAND.equals(command.getCommand())) {
				DOperationDone cancel = new DOperationDone();
				send(cancel);
			}
			commandEOF();
			showError(e.getMessage());
		}
	}

	/**
	 * Send the file contents in a non-blocking thread so that the command
	 * receivers can continue to function.
	 */
	private class Loader extends Thread {

		public Loader() {
			super();
		}

		@Override
		public void run() {
			DLoadPackage load = new DLoadPackage();
			load.setFile(file);
			send(load);
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
				JOptionPane.showMessageDialog(null, msg, TITLE, JOptionPane.ERROR_MESSAGE);
			}
		}.start();

	}
}
