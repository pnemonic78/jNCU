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
package net.sf.jncu.protocol.v2_0.io.win;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sf.jncu.Preferences;
import net.sf.jncu.cdil.CDPacket;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.newton.stream.NSOFSymbol;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.io.Store;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.app.DLoadPackageFile;
import net.sf.jncu.protocol.v2_0.data.DImportFile;
import net.sf.jncu.protocol.v2_0.data.DRestoreFile;
import net.sf.jncu.protocol.v2_0.io.DFileInfo;
import net.sf.jncu.protocol.v2_0.io.DFilesAndFolders;
import net.sf.jncu.protocol.v2_0.io.DGetDefaultPath;
import net.sf.jncu.protocol.v2_0.io.DGetFileInfo;
import net.sf.jncu.protocol.v2_0.io.DGetFilesAndFolders;
import net.sf.jncu.protocol.v2_0.io.DGetInternalStore;
import net.sf.jncu.protocol.v2_0.io.DInternalStore;
import net.sf.jncu.protocol.v2_0.io.DPath;
import net.sf.jncu.protocol.v2_0.io.DRequestToBrowse;
import net.sf.jncu.protocol.v2_0.io.DSetPath;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.swing.AcceptAllFileFilter;
import net.sf.swing.SwingUtils;

/**
 * Data source for interacting with the Newton file browser.
 * 
 * @author Moshe
 */
public class FileChooser extends IconModule {

	/**
	 * File chooser event.
	 */
	public static interface FileChooserListener {
		/**
		 * The file was selected after the user clicked an approval button.
		 * 
		 * @param file
		 *            the selected file.
		 * @param command
		 *            the command from the Newton.
		 */
		public void approveSelection(File file, IDockCommandFromNewton command);

		/**
		 * The browsing was cancelled.
		 */
		public void cancelSelection();
	}

	public static final NSOFSymbol IMPORT = DRequestToBrowse.IMPORT;
	public static final NSOFSymbol PACKAGES = DRequestToBrowse.PACKAGES;
	public static final NSOFSymbol SYNC_FILES = DRequestToBrowse.SYNC_FILES;

	/** Property key for default path. */
	protected static final String KEY_PATH = "FileChooser.path";

	private static enum State {
		None, Initialised, Browsing, Cancelled, Selected, Finished
	}

	private static final String TITLE = "File Chooser";

	private final List<NSOFString> types = new ArrayList<NSOFString>();
	private State state = State.None;
	private File path;
	private File file;
	private IDockCommandFromNewton command;
	private final List<FileFilter> filters = new ArrayList<FileFilter>();
	private FileFilter filter;
	private final List<FileChooserListener> listeners = new ArrayList<FileChooserListener>();
	private Store internalStore;

	/**
	 * Constructs a new file chooser.
	 * 
	 * @param pipe
	 *            the pipe.
	 * @param types
	 *            the chooser types.
	 */
	public FileChooser(CDPipe<? extends CDPacket> pipe, Collection<NSOFString> types) {
		super(TITLE, pipe);
		setName("FileChooser-" + getId());

		if ((types == null) || types.isEmpty())
			throw new IllegalArgumentException("types required");
		this.types.addAll(types);

		init();
	}

	/**
	 * Constructs a new file chooser.
	 * 
	 * @param pipe
	 *            the pipe.
	 * @param type
	 *            the chooser type.
	 */
	public FileChooser(CDPipe<? extends CDPacket> pipe, NSOFString type) {
		super(TITLE, pipe);
		setName("FileChooser-" + getId());

		if (type == null)
			throw new IllegalArgumentException("type required");
		this.types.add(type);

		init();
	}

	/**
	 * Initialise.
	 */
	private void init() {
		// Load the path from the properties file.
		Preferences prefs = Preferences.getInstance();
		String folderPath = prefs.get(KEY_PATH);
		if (folderPath == null) {
			folderPath = SwingUtils.getFileSystemView().getDefaultDirectory().getPath();
			prefs.set(KEY_PATH, folderPath);
			prefs.save();
		}
		this.path = new File(folderPath);
		populateFilters();

		if (types.contains(IMPORT)) {
			DGetInternalStore cmdGet = new DGetInternalStore();
			write(cmdGet);
		} else {
			DResult cmdResult = new DResult();
			write(cmdResult);
		}
		state = State.Initialised;
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		if (!isEnabled())
			return;

		super.commandReceived(command);

		String cmd = command.getCommand();

		if (DInternalStore.COMMAND.equals(cmd)) {
			DInternalStore cmdStore = (DInternalStore) command;
			internalStore = cmdStore.getStore();
			DResult cmdResult = new DResult();
			write(cmdResult);
		} else if (DGetDevices.COMMAND.equals(cmd)) {
			DDevices cmdDevices = new DDevices();
			write(cmdDevices);
		} else if (DGetDefaultPath.COMMAND.equals(cmd)) {
			DPath cmdPath = new DPath();
			cmdPath.setPath(path);
			write(cmdPath);
		} else if (DGetFilesAndFolders.COMMAND.equals(cmd)) {
			sendFiles();
		} else if (DGetFilters.COMMAND.equals(cmd)) {
			DFilters cmdFilters = new DFilters();
			cmdFilters.addFilters(filters);
			write(cmdFilters);
		} else if (DGetFileInfo.COMMAND.equals(cmd)) {
			DGetFileInfo cmdGet = (DGetFileInfo) command;
			File f = new File(path, cmdGet.getFilename());
			DFileInfo cmdInfo = new DFileInfo();
			cmdInfo.setFile(f);
			write(cmdInfo);
		} else if (DSetDrive.COMMAND.equals(cmd)) {
			DSetDrive cmdSet = (DSetDrive) command;
			setPath(new File(cmdSet.getDrive()));
			DPath cmdPath = new DPath();
			cmdPath.setPath(path);
			write(cmdPath);
		} else if (DSetPath.COMMAND.equals(cmd)) {
			DSetPath cmdSet = (DSetPath) command;
			setPath(cmdSet.getPath());
			sendFiles();
		} else if (DSetFilter.COMMAND.equals(cmd)) {
			DSetFilter cmdSet = (DSetFilter) command;
			int index = cmdSet.getIndex();
			this.filter = filters.get(index);
			sendFiles();
		} else if (DImportFile.COMMAND.equals(cmd)) {
			DImportFile cmdGet = (DImportFile) command;
			String filename = cmdGet.getFilename();
			this.file = new File(path, filename);
			this.command = command;
			state = State.Selected;
			fireApproved(this.file, command);
			done();
		} else if (DRestoreFile.COMMAND.equals(cmd)) {
			DRestoreFile cmdGet = (DRestoreFile) command;
			String filename = cmdGet.getFilename();
			this.file = new File(path, filename);
			this.command = command;
			state = State.Selected;
			fireApproved(this.file, command);
			done();
		} else if (DLoadPackageFile.COMMAND.equals(cmd)) {
			DLoadPackageFile cmdGet = (DLoadPackageFile) command;
			String filename = cmdGet.getFilename();
			this.file = new File(path, filename);
			this.command = command;
			state = State.Selected;
			fireApproved(this.file, command);
			done();
		} else if (DOperationCanceled.COMMAND.equals(cmd)) {
			fireCancelled();
			state = State.Cancelled;
		}
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		if (!isEnabled())
			return;

		super.commandSent(command);

		String cmd = command.getCommand();

		if (DResult.COMMAND.equals(cmd)) {
			state = State.Browsing;
		} else if (DOperationCanceled.COMMAND.equals(cmd)) {
			state = State.Cancelled;
		} else if (DOperationDone.COMMAND.equals(cmd)) {
			state = State.Finished;
		}
	}

	@Override
	protected void done() {
		super.done();
		state = State.Finished;
	}

	/**
	 * Send the filtered list of files and folders.
	 */
	protected void sendFiles() {
		DFilesAndFolders cmdFiles = new DFilesAndFolders();
		cmdFiles.setFolder(path);
		cmdFiles.setFilter(filter);
		write(cmdFiles);
	}

	/**
	 * Populate the list of filters.
	 */
	protected void populateFilters() {
		filters.clear();

		FileFilter filter;
		// TODO Load the strings from resources bundle like the
		// AcceptAllFileFilter.
		if (types.contains(IMPORT)) {
			filter = new FileNameExtensionFilter("Rich Text Format", "rtf", "RTF");
			filters.add(filter);
			filter = new FileNameExtensionFilter("Text Only", "txt", "TXT");
			filters.add(filter);
			filter = new FileNameExtensionFilter("Windows MetaFile", "wmf", "WMF");
			filters.add(filter);
		}
		if (types.contains(PACKAGES)) {
			filter = new FileNameExtensionFilter("Packages", "pkg", "PKG");
			filters.add(filter);
		}
		if (types.contains(SYNC_FILES)) {
			filter = new FileNameExtensionFilter("Backup Files", "nbk", "NBK");
			filters.add(filter);
		}
		filter = new AcceptAllFileFilter();
		filters.add(filter);

		this.filter = filters.get(0);
	}

	/**
	 * Get the current path.
	 * 
	 * @return the path.
	 */
	public File getPath() {
		return path;
	}

	/**
	 * Set the path.
	 * 
	 * @param path
	 *            the path.
	 */
	private void setPath(File path) {
		this.path = path;
		// Save the last path back to preferences.
		Preferences prefs = Preferences.getInstance();
		prefs.set(KEY_PATH, path.getPath());
		prefs.save();
	}

	/**
	 * Get the selected file.
	 * 
	 * @return the file - {@code null} if browsing was cancelled.
	 */
	public File getSelectedFile() {
		return file;
	}

	/**
	 * Get the selected command.
	 * 
	 * @return the command - {@code null} if browsing was cancelled.
	 */
	public IDockCommandFromNewton getSelectedCommand() {
		return command;
	}

	/**
	 * Add a browsing listener.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addListener(FileChooserListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Remove a browsing listener.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeListener(FileChooserListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notify all the listeners that a file has been approved.
	 * 
	 * @param file
	 *            the selected file.
	 * @param command
	 *            the received command.
	 */
	protected void fireApproved(File file, IDockCommandFromNewton command) {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<FileChooserListener> listenersCopy = new ArrayList<FileChooserListener>(listeners);
		for (FileChooserListener listener : listenersCopy) {
			listener.approveSelection(file, command);
		}
	}

	/**
	 * Notify all the listeners that browsing has been cancelled.
	 */
	protected void fireCancelled() {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<FileChooserListener> listenersCopy = new ArrayList<FileChooserListener>(listeners);
		for (FileChooserListener listener : listenersCopy) {
			listener.cancelSelection();
		}
	}

	/**
	 * Get the internal store.
	 * 
	 * @return the store
	 */
	public Store getInternalStore() {
		return internalStore;
	}

	@Override
	protected boolean isEnabled() {
		if (state == State.Cancelled)
			return false;
		if (state == State.Finished)
			return false;
		return super.isEnabled();
	}
}
