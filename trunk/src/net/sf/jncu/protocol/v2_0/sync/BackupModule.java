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
package net.sf.jncu.protocol.v2_0.sync;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDPacket;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.data.Archive;
import net.sf.jncu.data.ArchiveWriter;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.data.DEntry;
import net.sf.jncu.protocol.v1_0.data.DSoupInfo;
import net.sf.jncu.protocol.v1_0.data.DSoupNames;
import net.sf.jncu.protocol.v1_0.io.DGetStoreNames;
import net.sf.jncu.protocol.v1_0.io.DStoreNames;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v1_0.sync.DCurrentTime;
import net.sf.jncu.protocol.v1_0.sync.DLastSyncTime;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.app.AppName;
import net.sf.jncu.protocol.v2_0.app.DAppNames;
import net.sf.jncu.protocol.v2_0.app.DGetAppNames;
import net.sf.jncu.protocol.v2_0.data.DSendSoup;
import net.sf.jncu.protocol.v2_0.data.DSetSoupGetInfo;
import net.sf.jncu.protocol.v2_0.io.DSetStoreGetNames;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceled2;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;

/**
 * Backup module.
 * 
 * @author mwaisberg
 * 
 */
public class BackupModule extends IconModule {

	protected enum State {
		NONE,
		/** Request from the Newton that we want to backup. */
		INITIALISED,
		/** Ask the Newton for is options. */
		GET_OPTIONS,
		/** Newton sent us its options, or user selected options from dialog. */
		OPTIONS,
		/** Doing the backup. */
		BACKUP,
		/** Doing the backup for the store. */
		BACKUP_STORE,
		/** Doing the backup for the store's soups. */
		BACKUP_SOUPS,
		/** Doing the backup for the soup. */
		BACKUP_SOUP,
		/** Cancelled. */
		CANCELLED,
		/** Finished. */
		FINISHED
	}

	protected static final String TITLE = "Backup";

	private State state = State.NONE;
	private BackupDialog dialog;
	private SyncOptions options;
	private File file;
	private Archive archive;
	private Collection<Store> stores;
	private Collection<AppName> appNames;
	private Store store;
	private Soup soup;

	/**
	 * Creates a new module.
	 * 
	 * @param pipe
	 *            the pipe.
	 * @param requested
	 *            backup was requested by Newton?
	 */
	public BackupModule(CDPipe<? extends CDPacket> pipe, boolean requested) {
		super(TITLE, pipe);
		setName("BackupModule-" + getId());
		this.archive = new Archive();
		archive.setDeviceInfo(DockingProtocol.getNewtonInfo());

		state = State.INITIALISED;

		// Newton wants to load package?.
		if (requested) {
			state = State.GET_OPTIONS;
		}
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		if (!isEnabled())
			return;

		super.commandReceived(command);

		String cmd = command.getCommand();

		if (DResult.COMMAND.equals(cmd)) {
			DResult result = (DResult) command;
			int code = result.getErrorCode();

			if (code == DResult.OK) {
				switch (state) {
				case INITIALISED:
					// In order to show the dialog, we need to populate it with
					// stores and applications.
					DGetStoreNames getStores = new DGetStoreNames();
					write(getStores);
					break;
				}
			} else {
				done();
				state = State.CANCELLED;
				showError(result.getError().getMessage() + "\nCode: " + code);
			}
		} else if (DOperationCanceled.COMMAND.equals(cmd)) {
			DOperationCanceledAck ack = new DOperationCanceledAck();
			write(ack);
		} else if (DOperationCanceled2.COMMAND.equals(cmd)) {
			DOperationCanceledAck ack = new DOperationCanceledAck();
			write(ack);
		} else if (DStoreNames.COMMAND.equals(cmd)) {
			DStoreNames storeNames = (DStoreNames) command;
			stores = storeNames.getStores();
			archive.setStores(stores);

			// In order to show the dialog, we need to populate it with
			// stores and applications.
			if (state == State.INITIALISED) {
				DGetAppNames getApps = new DGetAppNames();
				getApps.setWhat(DGetAppNames.ALL_STORES_NAMES_SOUPS);
				write(getApps);
			}
		} else if (DAppNames.COMMAND.equals(cmd)) {
			DAppNames names = (DAppNames) command;
			appNames = names.getNames();

			if (state == State.INITIALISED) {
				showDialog();
			}
		} else if (DCurrentTime.COMMAND.equals(cmd)) {
			switch (state) {
			case OPTIONS:
				state = State.BACKUP;
				break;
			}
		} else if (DSoupNames.COMMAND.equals(cmd)) {
			DSoupNames soupNames = (DSoupNames) command;
			store.setSoups(soupNames.getSoups());

			if (state == State.BACKUP_STORE)
				state = State.BACKUP_SOUPS;
		} else if (DSoupInfo.COMMAND.equals(cmd)) {
			DSoupInfo info = (DSoupInfo) command;
			if (soup == null)
				this.soup = info.getSoup();
			else {
				this.store.addSoup(info.getSoup());
			}

			if (state == State.BACKUP_SOUP) {
				DSendSoup getEntries = new DSendSoup();
				write(getEntries);
			}
		} else if (DEntry.COMMAND.equals(cmd)) {
			DEntry entry = (DEntry) command;
			soup.addEntry(entry.getEntry());
		} else if (DBackupSoupDone.COMMAND.equals(cmd)) {
			// state == State.BACKUP_SOUP
			state = State.BACKUP_SOUPS;
		}
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		if (!isEnabled())
			return;

		super.commandSent(command);

		String cmd = command.getCommand();

		if (DOperationDone.COMMAND.equals(cmd)) {
			state = State.FINISHED;
		} else if (DOperationCanceledAck.COMMAND.equals(cmd)) {
			state = State.CANCELLED;
		} else if (DGetSyncOptions.COMMAND.equals(cmd)) {
			state = State.OPTIONS;
		}
	}

	@Override
	protected void done() {
		if (dialog != null) {
			dialog.close();
			dialog = null;
		}
		pipe.stopPing();
		super.done();
	}

	@Override
	protected boolean isEnabled() {
		if (state == State.CANCELLED)
			return false;
		if (state == State.FINISHED)
			return false;
		return super.isEnabled();
	}

	/** Show the options dialog. */
	protected void showDialog() {
		state = BackupModule.State.GET_OPTIONS;

		// Run this in thread to release the commandReceived call.
		new Thread() {
			@Override
			public void run() {
				dialog = new BackupDialog();
				dialog.setStores(stores);
				dialog.setApplications(appNames);
				// Ping the Newton so as not to cause connection timeout.
				pipe.ping();
				dialog.setVisible(true);
				pipe.stopPing();
				options = dialog.getSyncOptions();
				if (options == null) {
					cancel();
				} else {
					state = BackupModule.State.OPTIONS;
					BackupModule.this.start();
				}
			}
		}.start();
	}

	/**
	 * Backup to a file.
	 * 
	 * @param file
	 *            the file.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws BadPipeStateException
	 *             if invalid state.
	 */
	public void backup(File file) throws IOException, BadPipeStateException {
		this.file = file;

		switch (state) {
		case INITIALISED:
			DRequestToSync req = new DRequestToSync();
			write(req);
			break;
		case GET_OPTIONS:
			DGetSyncOptions get = new DGetSyncOptions();
			write(get);
			break;
		default:
			throw new BadPipeStateException("bad state " + state);
		}
	}

	/**
	 * Start the actual backup of data.
	 */
	@Override
	public void run() {
		if (state != State.OPTIONS)
			throw new BadPipeStateException("bad state " + state);

		DLastSyncTime time = new DLastSyncTime();
		write(time);

		while ((state != State.BACKUP) && isEnabled())
			yield();

		backupStores();
	}

	/**
	 * Backup the selected stores.
	 */
	protected void backupStores() {
		if ((stores == null) || stores.isEmpty()) {
			// Populate the archive with valid stores.
			DGetStoreNames getStores = new DGetStoreNames();
			write(getStores);
			while ((stores == null) && (state == State.BACKUP_STORE) && isEnabled())
				yield();
		}

		for (Store store : options.getStores()) {
			if ((state != State.BACKUP) || !isEnabled())
				return;
			backupStore(store);
		}

		if (isEnabled()) {
			try {
				writeArchive();
			} catch (IOException e) {
				e.printStackTrace();
				done();
			}
		}
	}

	/**
	 * Backup the selected store.
	 * 
	 * @param store
	 *            the store.
	 */
	protected void backupStore(Store store) {
		// Get an actual store.
		if (store.getSignature() == 0) {
			for (Store s : stores) {
				if (s.getName().equals(store.getName())) {
					store = s;
					break;
				}
			}
		}
		this.store = store;

		state = State.BACKUP_STORE;

		DSetStoreGetNames setStore = new DSetStoreGetNames();
		setStore.setStore(store);
		write(setStore);
		while ((state == State.BACKUP_STORE) && isEnabled())
			yield();

		if ((state == State.BACKUP_SOUPS) && isEnabled())
			backupSoups(store);
	}

	/**
	 * Backup the store's selected soups.
	 * 
	 * @param store
	 *            the store.
	 */
	protected void backupSoups(Store store) {
		Collection<Soup> soupsSelected = null;
		String storeName = store.getName();
		for (Store storeSelected : options.getStores()) {
			if (storeName.equals(storeSelected.getName())) {
				soupsSelected = storeSelected.getSoups();
				break;
			}
		}
		if ((soupsSelected == null) || soupsSelected.isEmpty())
			return;

		String soupName;
		// Clone the list to avoid ConcurrentModificationException
		Collection<Soup> soups = new ArrayList<Soup>(store.getSoups());
		for (Soup soupSelected : soupsSelected) {
			if ((state != State.BACKUP_SOUPS) || !isEnabled())
				return;

			soupName = soupSelected.getName();
			for (Soup soup : soups) {
				if (soupName.equals(soup.getName()))
					backupSoup(soup);
			}
		}

		state = State.BACKUP;
	}

	/**
	 * Backup the soup.
	 * 
	 * @param soup
	 *            the soup.
	 */
	protected void backupSoup(Soup soup) {
		this.soup = soup;
		state = State.BACKUP_SOUP;
		DSetSoupGetInfo setSoup = new DSetSoupGetInfo();
		setSoup.setSoup(soup);
		write(setSoup);
		while ((state == State.BACKUP_SOUP) && isEnabled())
			yield();
	}

	/**
	 * Write the archive.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeArchive() throws IOException {
		DOperationDone done = new DOperationDone();
		write(done);

		ArchiveWriter writer = new ArchiveWriter();
		writer.write(archive, file);
	}
}
