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

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sf.jncu.JNCUResources;
import net.sf.jncu.Preferences;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.protocol.DockCommand;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v1_0.app.DLoadPackage;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.io.FileChooser;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceled2;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.jncu.swing.JNCUModuleDialog;
import net.sf.swing.SwingUtils;

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

	/**
	 * Property key for default path.
	 * 
	 * @see FileChooser#KEY_PATH
	 */
	protected static final String KEY_PATH = "jncu.fileChooser.path";

	private File file;
	private State state = State.NONE;
	private JFileChooser packageChooser;

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
	public LoadPackage(CDPipe pipe, boolean requested, Window owner) {
		super(JNCUResources.getString("loadPackage"), pipe, owner);
		setName("LoadPackage-" + getId());

		state = State.INITIALISED;

		// Newton wants to load package so skip Requesting and Requested states.
		if (requested)
			state = State.LOADING;
	}

	@Override
	public void commandReceived(DockCommandFromNewton command) {
		if (!isEnabled())
			return;

		super.commandReceived(command);

		final String cmd = command.getCommand();

		if (DResult.COMMAND.equals(cmd)) {
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
				showError("Error: " + code, result.getError());
			}
		} else if (DLoadPackageFile.COMMAND.equals(cmd)) {
			state = State.LOADING;
		}
	}

	@Override
	public void commandSent(DockCommandToNewton command) {
		if (!isEnabled())
			return;

		super.commandSent(command);

		final String cmd = command.getCommand();

		if (DRequestToInstall.COMMAND.equals(cmd)) {
			state = State.REQUESTED;
		} else if (DLoadPackage.COMMAND.equals(cmd)) {
			state = State.LOADED;
		} else if (DOperationDone.COMMAND.equals(cmd)) {
			state = State.FINISHED;
		} else if (DOperationCanceled.COMMAND.equals(cmd)) {
			state = State.CANCELLED;
		} else if (DOperationCanceled2.COMMAND.equals(cmd)) {
			state = State.CANCELLED;
		} else if (DOperationCanceledAck.COMMAND.equals(cmd)) {
			state = State.CANCELLED;
		}
	}

	@Override
	protected void cancelReceivedRun() {
		super.cancelReceivedRun();
		state = State.CANCELLED;
	}

	@Override
	protected void cancelSendRun() {
		super.cancelSendRun();
		state = State.CANCELLED;
	}

	/**
	 * Upload the package to the Newton.
	 * 
	 * @param file
	 *            the file to install.
	 */
	public void loadPackage(File file) {
		// Save the folder for next time.
		Preferences prefs = Preferences.getInstance();
		prefs.set(KEY_PATH, file.getParent());
		prefs.save();

		this.file = file;

		if (state == State.INITIALISED) {
			write(new DRequestToInstall());
		} else if (state == State.LOADING) {
			start();
		}
	}

	/**
	 * Send the file contents in a non-blocking thread so that the command
	 * receivers can continue to function.
	 */
	@Override
	protected void runImpl() throws Exception {
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

	/**
	 * Get the chooser for the package to install.
	 * 
	 * @return the file chooser.
	 */
	public JFileChooser getFileChooser() {
		if (packageChooser == null) {
			// Load the path from the properties file.
			Preferences prefs = Preferences.getInstance();
			String folderPath = prefs.get(KEY_PATH);
			File folder;
			if (folderPath == null) {
				folder = SwingUtils.getFileSystemView().getDefaultDirectory();
				folderPath = folder.getPath();
				prefs.set(KEY_PATH, folderPath);
				prefs.save();
			} else {
				folder = new File(folderPath);
			}

			packageChooser = new JFileChooser();
			packageChooser.setCurrentDirectory(folder);

			FileFilter filter = new FileNameExtensionFilter(JNCUResources.getString("packages"), "pkg", "PKG");
			packageChooser.setFileFilter(filter);
			packageChooser.setAcceptAllFileFilterUsed(false);
			packageChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		return packageChooser;
	}

	@Override
	protected JNCUModuleDialog createProgress() {
		// No cancel button because we cannot send cancel midway (Newton will
		// think that command is part of the package file).
		JNCUModuleDialog dialog = new JNCUModuleDialog(getOwner(), createDialogIcon(), getTitle(), "0%", 0, DockCommand.LENGTH_WORD);
		dialog.setCancelOption(false);
		return dialog;
	}

	@Override
	protected Icon createDialogIcon() {
		return JNCUResources.getIcon("/dialog/pkg.png");
	}
}
