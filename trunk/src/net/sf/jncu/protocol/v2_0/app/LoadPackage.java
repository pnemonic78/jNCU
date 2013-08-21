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

import java.awt.Window;
import java.io.File;

import net.sf.jncu.cdil.CDPacket;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.app.DLoadPackage;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceled2;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;

/**
 * Load package module.
 * 
 * @author Moshe
 */
public class LoadPackage extends IconModule implements DockCommandListener {

	protected enum State {
		/** None. */
		NONE,
		/** Initialised. */
		INITIALISED,
		/** Requesting. */
		REQUESTING,
		/** Requested. */
		REQUESTED,
		/** Loading. */
		LOADING,
		/** Loaded. */
		LOADED,
		/** Cancelled. */
		CANCELLED,
		/** Finished. */
		FINISHED
	}

	private static final String TITLE = "Load Package";

	private File file;
	private State state = State.NONE;

	/**
	 * Constructs a new loader.
	 * 
	 * @param pipe
	 *            the pipe.
	 * @param requested
	 *            loading was requested by Newton?
	 * @param owner
	 *            the owner window.
	 */
	public LoadPackage(CDPipe<? extends CDPacket> pipe, boolean requested,
			Window owner) {
		super(TITLE, pipe, owner);
		setName("LoadPackage-" + getId());

		state = State.INITIALISED;

		// Newton wants to load package so skip Requesting and Requested states.
		if (requested)
			state = State.LOADING;
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		if (!isEnabled())
			return;

		super.commandReceived(command);

		String cmd = command.getCommand();

		if (DOperationCanceled2.COMMAND.equals(cmd)) {
			// TODO Stop sending the package command.
			// pipe.cancel(load);
			// interrupt();
		} else if (DResult.COMMAND.equals(cmd)) {
			DResult result = (DResult) command;
			int code = result.getErrorCode();
			// Upload can begin or was finished?
			if (code == DResult.OK) {
				if (state == State.REQUESTED) {
					state = State.LOADING;
					loadPackage(file);
				} else if (state == State.LOADED) {
					writeDone();
				}
			} else {
				done();
				state = State.CANCELLED;
				showError(result.getError().getMessage() + "\nCode: " + code);
			}
		} else if (DLoadPackageFile.COMMAND.equals(cmd)) {
			state = State.LOADING;
		}
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		if (!isEnabled())
			return;

		super.commandSent(command);

		String cmd = command.getCommand();

		if (DRequestToInstall.COMMAND.equals(cmd)) {
			state = State.REQUESTED;
		} else if (DLoadPackage.COMMAND.equals(cmd)) {
			state = State.LOADED;
		} else if (DOperationDone.COMMAND.equals(cmd)) {
			state = State.FINISHED;
		} else if (DOperationCanceledAck.COMMAND.equals(cmd)) {
			state = State.CANCELLED;
		}
	}

	/**
	 * Upload the package to the Newton.
	 * 
	 * @param file
	 */
	public void loadPackage(File file) {
		this.file = file;

		if (state == State.INITIALISED) {
			DRequestToInstall req = new DRequestToInstall();
			write(req);
		} else if (state == State.LOADING) {
			start();
		}
	}

	/**
	 * Send the file contents in a non-blocking thread so that the command
	 * receivers can continue to function.
	 */
	public void run() {
		DLoadPackage load = new DLoadPackage();
		load.setFile(file);
		write(load);
	}

	@Override
	protected boolean isEnabled() {
		if (state == State.CANCELLED)
			return false;
		if (state == State.FINISHED)
			return false;
		return super.isEnabled();
	}
}
