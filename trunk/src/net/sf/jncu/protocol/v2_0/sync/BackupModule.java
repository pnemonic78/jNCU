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

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

import net.sf.jncu.JNCUResources;
import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.data.DEntry;
import net.sf.jncu.protocol.v1_0.data.DSoupInfo;
import net.sf.jncu.protocol.v1_0.data.DSoupNames;
import net.sf.jncu.protocol.v1_0.io.DGetStoreNames;
import net.sf.jncu.protocol.v1_0.io.DStoreNames;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v1_0.sync.DCurrentTime;
import net.sf.jncu.protocol.v1_0.sync.DLastSyncTime;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.app.AppName;
import net.sf.jncu.protocol.v2_0.app.DAppNames;
import net.sf.jncu.protocol.v2_0.app.DGetAppNames;
import net.sf.jncu.protocol.v2_0.data.DSendSoup;
import net.sf.jncu.protocol.v2_0.data.DSetSoupGetInfo;
import net.sf.jncu.protocol.v2_0.io.DSetStoreGetNames;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;
import net.sf.jncu.sync.BackupException;
import net.sf.jncu.sync.BackupHandler;
import net.sf.jncu.sync.BackupWriter;

/**
 * Backup module.
 * 
 * @author mwaisberg
 */
public class BackupModule extends IconModule {

	/**
	 * Backup events listener.
	 */
	public static interface BackupListener extends IconModuleListener {
		/**
		 * The store is about to be backed up.
		 * 
		 * @param module
		 *            the backup module.
		 * @param store
		 *            the store.
		 */
		public void backupStore(BackupModule module, Store store);

		/**
		 * The application is about to be backed up.
		 * 
		 * @param module
		 *            the backup module.
		 * @param store
		 *            the store.
		 * @param appName
		 *            the application.
		 */
		public void backupApplication(BackupModule module, Store store, AppName appName);

		/**
		 * The soup is about to be backed up.
		 * 
		 * @param module
		 *            the backup module.
		 * @param store
		 *            the store.
		 * @param appName
		 *            the application.
		 * @param soup
		 *            the soup.
		 */
		public void backupSoup(BackupModule module, Store store, AppName appName, Soup soup);
	}

	protected enum State {
		/** None. */
		NONE,
		/** Request from the Newton that we want to backup. */
		INITIALISED,
		/** Ask the Newton for its options. */
		GET_OPTIONS,
		/** Newton sent us its options, or user selected options from dialog. */
		OPTIONS,
		/** Doing the backup. */
		BACKUP,
		/** Doing the backup for the store. */
		BACKUP_STORE,
		/** Doing the backup for the store's applications. */
		BACKUP_APPS,
		/** Doing the backup for the application. */
		BACKUP_APP,
		/** Doing the backup for the store's soups. */
		BACKUP_SOUPS,
		/** Doing the backup for the soup. */
		BACKUP_SOUP,
		/** Cancelled. */
		CANCELLED,
		/** Finished. */
		FINISHED
	}

	private State state = State.NONE;
	private BackupDialog dialog;
	private SyncOptions options;
	private BackupHandler writer;
	private Collection<Store> stores;
	private List<AppName> appNames;
	private Iterator<Store> storesIter;
	private Store store;
	private Iterator<AppName> appsIter;
	private AppName appName;
	private Iterator<Soup> soupsIter;
	private Soup soup;
	private Map<AppName, Collection<Soup>> soupsToBackup;
	private final List<BackupListener> listeners = new ArrayList<BackupListener>();

	/**
	 * Creates a new module.
	 * 
	 * @param pipe
	 *            the pipe.
	 * @param requested
	 *            backup was requested by Newton?
	 * @param owner
	 *            the owner window.
	 */
	public BackupModule(CDPipe pipe, boolean requested, Window owner) {
		super(JNCUResources.getString("backup"), pipe, owner);
		setName("BackupModule-" + getId());

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

		final String cmd = command.getCommand();

		if (DResult.COMMAND.equals(cmd)) {
			DResult result = (DResult) command;
			int code = result.getErrorCode();

			if (code == DResult.OK) {
				switch (state) {
				case INITIALISED:
					// In order to show the dialog, we need to populate it with
					// stores and applications.
					write(new DGetStoreNames());
					break;
				}
			} else {
				done();
				state = State.CANCELLED;
				showError("Error: " + code, result.getError());
			}
		} else if (DStoreNames.COMMAND.equals(cmd)) {
			DStoreNames storeNames = (DStoreNames) command;
			stores = storeNames.getStores();

			// DGetAppNames -> DAppNames -> dialog / backupStores
			DGetAppNames getApps = new DGetAppNames();
			getApps.setWhat(DGetAppNames.ALL_STORES_NAMES_SOUPS);
			write(getApps);
		} else if (DAppNames.COMMAND.equals(cmd)) {
			DAppNames names = (DAppNames) command;
			appNames = names.getNames();
			Collections.sort(appNames);

			switch (state) {
			case INITIALISED:
				// In order to show the dialog, we need to populate it with
				// stores and applications.
				showDialog();
				break;
			case BACKUP:
				backupStores();
				break;
			}
		} else if (DCurrentTime.COMMAND.equals(cmd)) {
			if (state == State.OPTIONS) {
				BackupModule.this.start();
			}
		} else if (DSoupNames.COMMAND.equals(cmd)) {
			DSoupNames soupNames = (DSoupNames) command;
			if (store != null) {
				store.setSoups(soupNames.getSoups());

				if (state == State.BACKUP_STORE) {
					backupApps(store);
				}
			}
		} else if (DSoupInfo.COMMAND.equals(cmd)) {
			DSoupInfo info = (DSoupInfo) command;
			Soup infoSoup = info.getSoup();
			if ((soup == null) || (state != State.BACKUP_SOUP)) {
				this.soup = infoSoup;
			} else {
				if (Boolean.getBoolean("debug")) {
					String thisName = this.soup.getName();
					String thatName = info.getSoup().getName();
					if ((thisName.length() == 0) || ((thatName.length() > 0) && !thisName.equals(thatName))) {
						System.err.println("Expected name [" + thisName + "], but was [" + thatName + "]");
						System.exit(1);
					}
					int thisSig = this.soup.getSignature();
					int thatSig = info.getSoup().getSignature();
					if ((thisSig != 0) && (thatSig != 0) && (thisSig != thatSig)) {
						System.err.println("Expected signature " + thisSig + ", but was " + thatSig);
						System.exit(1);
					}
				}
				this.soup.fromSoup(infoSoup);
			}

			if (state == State.BACKUP_SOUP) {
				backupSoupInfo();
			}
		} else if (DEntry.COMMAND.equals(cmd)) {
			if (state == State.BACKUP_SOUP) {
				DEntry entry = (DEntry) command;
				SoupEntry soupEntry = entry.getEntry();

				try {
					writer.soupEntry(store.getName(), soup, soupEntry);
				} catch (BackupException e) {
					showError("Soup entry", e);
					writeCancel();
					return;
				}
			}
		} else if (DBackupSoupDone.COMMAND.equals(cmd)) {
			if (state == State.BACKUP_SOUP) {
				backupSoupDone();
			} else {
				this.soup = null;
			}
		} else if (DSyncOptions.COMMAND.equals(cmd)) {
			DSyncOptions sync = (DSyncOptions) command;

			if (state == State.GET_OPTIONS) {
				state = State.OPTIONS;
				options = sync.getSyncInfo();
			}
		}
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		if (!isEnabled())
			return;

		super.commandSent(command);

		final String cmd = command.getCommand();

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
		SwingUtilities.invokeLater(new Runnable() {
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

					// DLastSyncTime -> DCurrentTime
					write(new DLastSyncTime());
				}
			}
		});
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
		this.writer = new BackupWriter(file);
		writer.startBackup();
		writer.modified(System.currentTimeMillis());
		writer.deviceInformation(DockingProtocol.getNewtonInfo());

		switch (state) {
		case INITIALISED:
			write(new DRequestToSync());
			break;
		case GET_OPTIONS:
			// DGetSyncOptions -> DSyncOptions
			write(new DGetSyncOptions());
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
		if (!isEnabled())
			return;
		if (state != State.OPTIONS)
			throw new BadPipeStateException("bad state " + state);
		if (options == null)
			throw new NullPointerException("sync options required");

		this.state = State.BACKUP;
		this.storesIter = null;
		this.store = null;
		this.soupsIter = null;
		this.soup = null;

		if (stores == null) {
			// DGetStoreNames -> DStoreNames
			write(new DGetStoreNames());
		} else {
			backupStores();
		}
	}

	/**
	 * Backup the selected stores.
	 * 
	 * @throws BadPipeStateException
	 *             if invalid state.
	 */
	protected void backupStores() throws BadPipeStateException {
		if (writer == null)
			return;
		if (!isEnabled())
			return;
		if (state != State.BACKUP)
			throw new BadPipeStateException("bad state " + state);

		// Populate the list of stores to backup.
		Collection<Store> storesToBackup = new TreeSet<Store>();
		String storeName;
		for (Store storeSelected : options.getStores()) {
			storeName = storeSelected.getName();
			for (Store store : stores) {
				if (storeName.equals(store.getName()))
					storesToBackup.add(store);
			}
		}

		if (storesToBackup.isEmpty())
			return;
		this.storesIter = storesToBackup.iterator();
		this.store = null;
		this.appsIter = null;
		this.appName = null;
		this.soupsIter = null;
		this.soup = null;

		backupNextStore();
	}

	/**
	 * Backup the next selected store.
	 * 
	 * @throws BadPipeStateException
	 *             if invalid state.
	 */
	protected void backupNextStore() throws BadPipeStateException {
		if (writer == null)
			return;
		if (!isEnabled())
			return;
		if (state != State.BACKUP)
			throw new BadPipeStateException("bad state " + state);

		if (this.store != null) {
			try {
				writer.endStore(store);
			} catch (BackupException e) {
				showError("End store", e);
				writeCancel();
				return;
			}
		}
		// No mores stores?
		if (!storesIter.hasNext()) {
			try {
				writeDone();
				writer.endBackup();
			} catch (BackupException e) {
				e.printStackTrace();
			}
			return;
		}
		this.state = State.BACKUP_STORE;
		this.store = storesIter.next();
		this.appsIter = null;
		this.appName = null;
		this.soupsIter = null;
		this.soup = null;
		// Notify listeners we are now going to backup <store>
		fireBackupStore(store);

		try {
			writer.startStore(store.getName());
			writer.storeDefinition(store);
		} catch (BackupException e) {
			e.printStackTrace();
			writeCancel();
			return;
		}

		// DSetStoreGetNames -> DSoupNames
		DSetStoreGetNames setStore = new DSetStoreGetNames();
		setStore.setStore(store);
		write(setStore);
	}

	/**
	 * Backup the store's selected applications.
	 * <p>
	 * Soups to backup := (options soups) INTERSECT (store soups) INTERSECT
	 * (appName soups)
	 * 
	 * @param store
	 *            the store.
	 * @throws BadPipeStateException
	 *             if invalid state.
	 */
	protected void backupApps(Store store) throws BadPipeStateException {
		if (writer == null)
			return;
		if (!isEnabled())
			return;
		if (state != State.BACKUP_STORE)
			throw new BadPipeStateException("bad state " + state);

		// Populate the list of soups to backup.
		soupsToBackup = new TreeMap<AppName, Collection<Soup>>();
		Collection<Soup> soups;
		Collection<Soup> soupsOptions;
		String storeNameOption, soupNameOption, soupNameApp, soupNameStore;
		NSOFArray appNameSoups;
		int appNameSoupsLength;

		for (Store storeOption : options.getStores()) {
			storeNameOption = storeOption.getName();
			if (!storeNameOption.equals(store) && !storeNameOption.equals(store.getName()))
				continue;
			soupsOptions = storeOption.getSoups();
			if ((soupsOptions == null) || soupsOptions.isEmpty())
				continue;

			for (Soup soupOptions : soupsOptions) {
				soupNameOption = soupOptions.getName();

				for (Soup soupStore : store.getSoups()) {
					soupNameStore = soupStore.getName();
					if (!soupNameOption.equals(soupNameStore))
						continue;

					for (AppName appName : appNames) {
						appNameSoups = appName.getSoups();
						appNameSoupsLength = (appNameSoups == null) ? 0 : appNameSoups.length();
						if (appNameSoupsLength == 0)
							continue;
						soups = soupsToBackup.get(appName);

						for (int i = 0; i < appNameSoupsLength; i++) {
							soupNameApp = ((NSOFString) appNameSoups.get(i)).getValue();
							if (!soupNameApp.equals(soupNameOption))
								continue;

							if (soups == null) {
								soups = new TreeSet<Soup>();
								soupsToBackup.put(appName, soups);
							}
							soups.add(soupStore);
							break;
						}
					}
					break;
				}
			}
		}

		if (soupsToBackup.isEmpty()) {
			state = State.BACKUP;
			backupNextStore();
			return;
		}

		this.state = State.BACKUP_APPS;
		this.appsIter = soupsToBackup.keySet().iterator();
		this.appName = null;
		this.soupsIter = null;
		this.soup = null;
		backupNextApp();
	}

	/**
	 * Backup the next selected application.
	 * 
	 * @throws BadPipeStateException
	 *             if invalid state.
	 */
	protected void backupNextApp() throws BadPipeStateException {
		if (writer == null)
			return;
		if (!isEnabled())
			return;
		if (state != State.BACKUP_APPS)
			throw new BadPipeStateException("bad state " + state);

		if (!appsIter.hasNext()) {
			state = State.BACKUP;
			backupNextStore();
			return;
		}
		this.state = State.BACKUP_APP;
		this.appName = appsIter.next();
		this.soupsIter = null;
		this.soup = null;
		// Notify listeners we are now going to backup <store, appName>
		fireBackupApp(store, appName);

		backupSoups(store, appName);
	}

	/**
	 * Backup the store's selected soups.
	 * 
	 * @param store
	 *            the store.
	 * @param appName
	 *            the application name.
	 * @throws BadPipeStateException
	 *             if invalid state.
	 */
	protected void backupSoups(Store store, AppName appName) throws BadPipeStateException {
		if (writer == null)
			return;
		if (!isEnabled())
			return;
		if (state != State.BACKUP_APP)
			throw new BadPipeStateException("bad state " + state);

		final Collection<Soup> soups = soupsToBackup.get(appName);

		this.state = State.BACKUP_SOUPS;
		this.soupsIter = soups.iterator();
		this.soup = null;
		backupNextSoup();
	}

	/**
	 * Backup the next selected soup.
	 * 
	 * @throws BadPipeStateException
	 *             if invalid state.
	 */
	protected void backupNextSoup() throws BadPipeStateException {
		if (writer == null)
			return;
		if (!isEnabled())
			return;
		if (state != State.BACKUP_SOUPS)
			throw new BadPipeStateException("bad state " + state);

		if (!soupsIter.hasNext()) {
			state = State.BACKUP_APPS;
			backupNextApp();
			return;
		}
		this.state = State.BACKUP_SOUP;
		this.soup = soupsIter.next();
		// Notify listeners we are now going to backup <store, appName, soup>
		fireBackupSoup(store, appName, soup);

		// DSetSoupGetInfo -> DSoupInfo -> DSendSoup -> DEntry* ->
		// DBackupSoupDone
		DSetSoupGetInfo setSoup = new DSetSoupGetInfo();
		setSoup.setSoup(soup);
		write(setSoup);
	}

	/**
	 * Backup the soup information.
	 * 
	 * @throws BadPipeStateException
	 *             if invalid state.
	 */
	protected void backupSoupInfo() throws BadPipeStateException {
		if (writer == null)
			return;
		if (!isEnabled())
			return;
		if (state != State.BACKUP_SOUP)
			throw new BadPipeStateException("bad state " + state);

		try {
			writer.startSoup(store.getName(), soup.getName());
			writer.soupDefinition(store.getName(), soup);
		} catch (BackupException e) {
			showError("Soup info", e);
			writeCancel();
			return;
		}

		// DSendSoup -> DEntry* -> DBackupSoupDone
		write(new DSendSoup());
	}

	/**
	 * Backing up the soup is finished.
	 * 
	 * @throws BadPipeStateException
	 *             if invalid state.
	 */
	protected void backupSoupDone() throws BadPipeStateException {
		if (writer == null)
			return;
		if (!isEnabled())
			return;
		if (state != State.BACKUP_SOUP)
			throw new BadPipeStateException("bad state " + state);

		try {
			writer.endSoup(store.getName(), soup);
		} catch (BackupException e) {
			showError("End soup", e);
			writeCancel();
			return;
		}
		state = State.BACKUP_SOUPS;
		backupNextSoup();
	}

	@Override
	protected ProgressMonitor getProgress() {
		return null;// Never show the progress.
	}

	/**
	 * Add a backup listener.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addListener(BackupListener listener) {
		super.addListener(listener);
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Remove a backup listener.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeListener(BackupListener listener) {
		super.removeListener(listener);
		listeners.remove(listener);
	}

	/**
	 * Notify all the listeners that the store will be backed up.
	 * 
	 * @param store
	 *            the store.
	 */
	protected void fireBackupStore(Store store) {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<BackupListener> listenersCopy = new ArrayList<BackupListener>(listeners);
		for (BackupListener listener : listenersCopy) {
			listener.backupStore(this, store);
		}
	}

	/**
	 * Notify all the listeners that the application will be backed up.
	 * 
	 * @param store
	 *            the store.
	 * @param appName
	 *            the application.
	 */
	protected void fireBackupApp(Store store, AppName appName) {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<BackupListener> listenersCopy = new ArrayList<BackupListener>(listeners);
		for (BackupListener listener : listenersCopy) {
			listener.backupApplication(this, store, appName);
		}
	}

	/**
	 * Notify all the listeners that the soup will be backed up.
	 * 
	 * @param store
	 *            the store.
	 * @param appName
	 *            the application.
	 * @param soup
	 *            the soup.
	 */
	protected void fireBackupSoup(Store store, AppName appName, Soup soup) {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<BackupListener> listenersCopy = new ArrayList<BackupListener>(listeners);
		for (BackupListener listener : listenersCopy) {
			listener.backupSoup(this, store, appName, soup);
		}
	}
}
