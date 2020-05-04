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

import net.sf.jncu.JNCUResources;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v1_0.data.DEntry;
import net.sf.jncu.protocol.v1_0.data.DSoupInfo;
import net.sf.jncu.protocol.v1_0.data.DSoupNames;
import net.sf.jncu.protocol.v1_0.io.DGetStoreNames;
import net.sf.jncu.protocol.v1_0.io.DSetCurrentStore;
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
import net.sf.jncu.sync.BackupException;
import net.sf.jncu.sync.BackupHandler;
import net.sf.jncu.sync.BackupWriter;

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

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
         * @param module the backup module.
         * @param store  the store.
         */
        public void backupStore(BackupModule module, Store store);

        /**
         * The application is about to be backed up.
         *
         * @param module  the backup module.
         * @param store   the store.
         * @param appName the application.
         */
        public void backupApplication(BackupModule module, Store store, AppName appName);

        /**
         * The soup is about to be backed up.
         *
         * @param module  the backup module.
         * @param store   the store.
         * @param appName the application.
         * @param soup    the soup.
         */
        public void backupSoup(BackupModule module, Store store, AppName appName, Soup soup);
    }

    /**
     * The default file extension for backup files.
     */
    public static final String EXTENSION = ".zip";

    protected enum State {
        /**
         * None.
         */
        NONE,
        /**
         * Request from the Newton that we want to backup.
         */
        INITIALISED,
        /**
         * Ask the Newton for its options.
         */
        GET_OPTIONS,
        /**
         * Newton sent us its options, or user selected options from dialog.
         */
        OPTIONS,
        /**
         * Populating the list of items to backup.
         */
        METADATA,
        /**
         * Doing the backup.
         */
        BACKUP,
        /**
         * Doing the backup for the store.
         */
        BACKUP_STORE,
        /**
         * Doing the backup for the application.
         */
        BACKUP_APP,
        /**
         * Doing the backup for the soup.
         */
        BACKUP_SOUP,
        /**
         * Cancelled.
         */
        CANCELLED,
        /**
         * Finished.
         */
        FINISHED
    }

    private State state = State.NONE;
    private BackupDialog optionsDialog;
    private SyncOptions options;
    private BackupHandler writer;
    private Collection<Store> stores;
    private List<AppName> appNames;
    private Iterator<BackupItem> itemsIter;
    private BackupItem item;
    private final List<BackupListener> listeners = new ArrayList<BackupListener>();
    private NewtonInfo deviceInfo;

    private static boolean DEBUG = false;// TODO

    /**
     * Creates a new module.
     *
     * @param pipe      the pipe.
     * @param requested was backup requested by Newton?
     * @param owner     the owner window.
     */
    public BackupModule(CDPipe pipe, boolean requested, Window owner) {
        this(pipe, requested, owner, null);
    }

    /**
     * Creates a new module.
     *
     * @param pipe       the pipe.
     * @param requested  was backup requested by Newton?
     * @param owner      the owner window.
     * @param deviceInfo the device information.
     */
    public BackupModule(CDPipe pipe, boolean requested, Window owner, NewtonInfo deviceInfo) {
        super(JNCUResources.getString("backup"), pipe, owner);
        setName("BackupModule-" + getId());

        state = State.INITIALISED;

        // Newton wants to load package?.
        if (requested) {
            state = State.GET_OPTIONS;
        }
    }

    /**
     * Set the device information.
     *
     * @param deviceInfo the information.
     */
    public void setNewtonInfo(NewtonInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    @Override
    public void commandReceived(DockCommandFromNewton command) {
        if (DEBUG)
            System.out.println("B R " + command + " " + state);
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
                    case GET_OPTIONS:
                        // In order to show the dialog, we need to populate it with
                        // stores and applications.
                        write(new DGetStoreNames());
                        break;
                    case BACKUP_STORE:
                        Store store = item.getStore();
                        try {
                            backupStore(store);
                        } catch (BackupException e) {
                            showBackupError("Backup store", e);
                        }
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
            if (DEBUG) {
                for (Store s : stores) {
                    System.out.println("\t\t" + s);
                    for (Soup p : s.getSoups())
                        System.out.println("\t\t\t" + p);
                }
            }

            switch (state) {
                case INITIALISED:
                case GET_OPTIONS:
                    // In order to show the dialog, we need to populate it with
                    // stores and applications.
                    if (optionsDialog != null)
                        optionsDialog.setStores(stores);
                    break;
            }

            // DGetAppNames -> DAppNames -> dialog / populateItems
            DGetAppNames getApps = new DGetAppNames();
            getApps.setWhat(DGetAppNames.ALL_STORES_NAMES_SOUPS);
            write(getApps);
        } else if (DAppNames.COMMAND.equals(cmd)) {
            DAppNames names = (DAppNames) command;
            appNames = names.getNames();
            Collections.sort(appNames);

            switch (state) {
                case INITIALISED:
                case GET_OPTIONS:
                    // In order to show the dialog, we need to populate it with
                    // stores and applications.
                    if (optionsDialog != null)
                        optionsDialog.setApplications(appNames);
                    // showDialog();
                    break;
                case METADATA:
                    try {
                        populateItems();
                    } catch (BackupException e) {
                        showBackupError("Backup populate items", e);
                    }
                    break;
            }
        } else if (DCurrentTime.COMMAND.equals(cmd)) {
            if (state == State.OPTIONS) {
                BackupModule.this.start();
            }
        } else if (DSoupNames.COMMAND.equals(cmd)) {
            DSoupNames soupNames = (DSoupNames) command;
            if (item != null) {
                Store store = item.getStore();
                store.setSoups(soupNames.getSoups());

                if (state == State.METADATA) {
                    try {
                        populateNextStore();
                    } catch (BackupException e) {
                        showBackupError("Backup store", e);
                    }
                }
            }
        } else if (DSoupInfo.COMMAND.equals(cmd)) {
            DSoupInfo info = (DSoupInfo) command;
            Soup soup = info.getSoup();

            try {
                if (Boolean.getBoolean("debug")) {
                    BackupItem item = this.item;

                    String thisName = item.getSoup().getName();
                    String thatName = soup.getName();
                    if ((thisName.length() == 0) || ((thatName.length() > 0) && !thisName.equals(thatName))) {
                        System.err.println("Expected name [" + thisName + "], but was [" + thatName + "]");
                        System.exit(1);
                    }
                    int thisSig = item.getSoup().getSignature();
                    int thatSig = soup.getSignature();
                    if ((thisSig != 0) && (thatSig != 0) && (thisSig != thatSig)) {
                        throw new BackupException("Expected soup signature " + thisSig + ", but was " + thatSig);
                    }
                }

                if (state == State.BACKUP_SOUP) {
                    backupSoup(soup);
                }
            } catch (BackupException e) {
                showBackupError("Backup soup", e);
            }
        } else if (DEntry.COMMAND.equals(cmd)) {
            DEntry entry = (DEntry) command;
            SoupEntry soupEntry = entry.getEntry();

            if (state == State.BACKUP_SOUP) {
                try {
                    backupSoupEntry(soupEntry);
                } catch (BackupException e) {
                    showBackupError("Backup soup entry", e);
                }
            }
        } else if (DBackupSoupDone.COMMAND.equals(cmd)) {
            if (state == State.BACKUP_SOUP) {
                try {
                    backupSoupDone();
                } catch (BackupException e) {
                    showBackupError("Backup soup done", e);
                }
            }
        } else if (DSyncOptions.COMMAND.equals(cmd)) {
            DSyncOptions sync = (DSyncOptions) command;

            if (state == State.GET_OPTIONS) {
                state = State.OPTIONS;
            }
            if (state == State.OPTIONS) {
                options = sync.getSyncInfo();
                if (options == null) {
                    cancel();
                } else {
                    if (DEBUG) {
                        System.out.println("\toptions stores:");
                        for (Store s : options.getStores()) {
                            System.out.println("\t\t" + s);
                            for (Soup p : s.getSoups())
                                System.out.println("\t\t\t" + p);
                        }
                    }

                    // DLastSyncTime -> DCurrentTime
                    write(new DLastSyncTime());
                }
            }
        }
    }

    @Override
    public void commandSent(DockCommandToNewton command) {
        if (DEBUG)
            System.out.println("B S " + command + " " + state);
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
        if (optionsDialog != null) {
            optionsDialog.close();
            optionsDialog = null;
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

    /**
     * Show the options dialog.
     */
    protected void showDialog() {
        state = State.GET_OPTIONS;

        // Run this in thread to release the commandReceived call.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                optionsDialog = new BackupDialog();
                optionsDialog.setStores(stores);
                optionsDialog.setApplications(appNames);

                // Ping the Newton to avoid connection timeout.
                pipe.ping();
                optionsDialog.setVisible(true);
                pipe.stopPing();

                options = optionsDialog.getSyncOptions();
                if (options == null) {
                    cancel();
                } else {
                    state = State.OPTIONS;

                    // DLastSyncTime -> DCurrentTime
                    write(new DLastSyncTime());
                }
            }
        });
    }

    /**
     * Backup to a file.
     *
     * @param file the file.
     * @throws IOException     if an I/O error occurs.
     * @throws BackupException if a backup error occurs.
     */
    public void backup(File file) throws IOException, BackupException {
        if (DEBUG)
            System.out.println("B 01 backup " + file + " " + state);
        this.writer = new BackupWriter(file);
        writer.startBackup();
        writer.modified(System.currentTimeMillis());
        writer.deviceInformation(deviceInfo);

        switch (state) {
            case INITIALISED:
                showDialog();
                // DRequestToSync -> DResult
                write(new DRequestToSync());
                break;
            case GET_OPTIONS:
                // DGetSyncOptions -> DSyncOptions
                write(new DGetSyncOptions());
                break;
            default:
                throw new BackupException("bad state " + state);
        }
    }

    /**
     * Start the actual backup of data.
     */
    @Override
    protected void runImpl() throws Exception {
        if (DEBUG)
            System.out.println("B 02 runImpl " + state);
        if (state != State.OPTIONS)
            throw new BackupException("Backup run - bad state " + state);
        if (options == null)
            throw new NullPointerException("Backup run - sync options required");

        this.state = State.METADATA;
        this.itemsIter = null;
        this.item = null;

        if (stores == null) {
            // DGetStoreNames -> DStoreNames
            write(new DGetStoreNames());
        } else {
            populateItems();
        }
    }

    /**
     * Populate the metadata before actually fetching the data to backup.
     *
     * @throws BackupException if a backup error occurs.
     */
    protected void populateItems() throws BackupException {
        if (DEBUG)
            System.out.println("B 03 populateItems " + state);
        if (!isEnabled())
            return;
        if (state != State.METADATA)
            throw new BackupException("bad state " + state);

        this.itemsIter = null;
        this.item = null;

        // Filter the list of stores to backup.
        // The stores contained in the options are missing slots.
        Collection<Store> storesToBackup = new TreeSet<Store>();
        String storeName;
        if (DEBUG) {
            System.out.println("\toptions stores:");
            for (Store s : options.getStores()) {
                System.out.println("\t\t" + s);
                for (Soup p : s.getSoups())
                    System.out.println("\t\t\t" + p);
            }
            System.out.println("\tstores newton:");
            for (Store s : stores) {
                System.out.println("\t\t" + s);
                for (Soup p : s.getSoups())
                    System.out.println("\t\t\t" + p);
            }
        }
        for (Store storeOptions : options.getStores()) {
            storeName = storeOptions.getName();
            for (Store storeNewton : stores) {
                if (storeName.equals(storeNewton.getName()))
                    storesToBackup.add(storeNewton);
            }
        }
        this.stores = storesToBackup;
        if (DEBUG) {
            System.out.println("\tstores backup:");
            for (Store s : storesToBackup) {
                System.out.println("\t\t" + s);
                for (Soup p : s.getSoups())
                    System.out.println("\t\t\t" + p);
            }
            System.out.println("\tapps:");
            for (AppName a : appNames) {
                System.out.println("\t\t" + a);
            }
        }
        if (storesToBackup.isEmpty())
            return;

        Collection<BackupItem> items = new ArrayList<BackupItem>();
        for (Store storeOptions : storesToBackup) {
            item = new BackupItem(storeOptions, null, null);
            items.add(item);
        }
        if (DEBUG) {
            System.out.println("\titems:");
            for (BackupItem i : items) {
                System.out.println("\t\t" + i);
            }
        }
        this.itemsIter = items.iterator();
        this.item = null;
        populateNextStore();
    }

    /**
     * Populate the store metadata before actually fetching the data to backup.
     *
     * @throws BackupException if a backup error occurs.
     */
    protected void populateNextStore() throws BackupException {
        if (DEBUG)
            System.out.println("B 04 populateNextStore " + state);
        if (!isEnabled())
            return;
        if (state != State.METADATA)
            throw new BackupException("bad state " + state);

        // No items?
        if (itemsIter == null)
            return;
        // No more items?
        if (!itemsIter.hasNext()) {
            populateAllItems();
            return;
        }

        BackupItem itemNext = itemsIter.next();
        Store store = itemNext.getStore();
        this.item = itemNext;

        // DSetStoreGetNames -> DSoupNames
        DSetStoreGetNames setStoreGetNames = new DSetStoreGetNames();
        setStoreGetNames.setStore(store);
        write(setStoreGetNames);
    }

    /**
     * Populate the metadata before actually fetching the data to backup.
     *
     * @throws BackupException if a backup error occurs.
     */
    private void populateAllItems() throws BackupException {
        if (DEBUG)
            System.out.println("B 05 populateAllItems " + state);
        if (!isEnabled())
            return;
        if (state != State.METADATA)
            throw new BackupException("bad state " + state);

        this.itemsIter = null;
        this.item = null;

        // Re-build the list of applications to backup based on the stores
        // and soups selected.
        Collection<BackupItem> items = new ArrayList<BackupItem>();
        Collection<AppName> appNamesSelected = new TreeSet<AppName>();
        NSOFArray appNameSoups;
        int appNameSoupsLength;
        NSOFString soupAppName;
        BackupItem item;
        boolean added;

        if (DEBUG) {
            System.out.println("\tstores:");
            for (Store s : stores) {
                System.out.println("\t\t" + s);
                for (Soup p : s.getSoups())
                    System.out.println("\t\t\t" + p);
            }
        }
        // Populate the list of applications that were selected in the options
        // dialog.
        if (!options.isSyncAll()) {
            for (AppName appNameNewton : appNames) {
                appNameSoups = appNameNewton.getSoups();
                appNameSoupsLength = (appNameSoups == null) ? 0 : appNameSoups.length();
                added = false;

                for (int i = 0; i < appNameSoupsLength; i++) {
                    soupAppName = (NSOFString) appNameSoups.get(i);

                    for (Store storeOptions : options.getStores()) {
                        for (Soup soupOptions : storeOptions.getSoups()) {
                            if (soupOptions.equals(soupAppName)) {
                                appNamesSelected.add(appNameNewton);
                                added = true;
                                break;
                            }
                        }
                        if (added)
                            break;
                    }
                    if (added)
                        break;
                }
            }
            appNames.clear();
            appNames.addAll(appNamesSelected);
        }
        if (DEBUG) {
            System.out.println("\tapps selected:");
            for (AppName a : appNamesSelected) {
                System.out.println("\t\t" + a);
            }
        }

        for (Store storeNewton : stores) {
            item = new BackupItem(storeNewton, null, null);
            items.add(item);

            for (AppName appNameNewton : appNamesSelected) {
                item = new BackupItem(storeNewton, appNameNewton, null);
                items.add(item);

                appNameSoups = appNameNewton.getSoups();
                appNameSoupsLength = (appNameSoups == null) ? 0 : appNameSoups.length();

                for (Soup soupNewton : storeNewton.getSoups()) {
                    for (int i = 0; i < appNameSoupsLength; i++) {
                        soupAppName = (NSOFString) appNameSoups.get(i);
                        if (soupNewton.equals(soupAppName)) {
                            item = new BackupItem(storeNewton, appNameNewton, soupNewton);
                            items.add(item);
                            break;
                        }
                    }
                }
            }
        }
        if (DEBUG) {
            System.out.println("\titems:");
            for (BackupItem i : items) {
                System.out.println("\t\t" + i);
            }
        }

        this.state = State.BACKUP;
        this.itemsIter = items.iterator();
        this.item = null;
        backupNextItem();
    }

    /**
     * Backup the next item.
     *
     * @throws BackupException if a backup error occurs.
     */
    protected void backupNextItem() throws BackupException {
        if (DEBUG)
            System.out.println("B 10 backupNextItem " + state);
        if (writer == null)
            return;
        if (!isEnabled())
            return;
        if (state != State.BACKUP)
            throw new BackupException("bad state " + state);

        // No items?
        if (itemsIter == null)
            return;
        // No more items?
        if (!itemsIter.hasNext()) {
            backupNextStore(item, null);
            writer.endBackup();
            writeDone();
            return;
        }

        BackupItem item = this.item;
        BackupItem itemNext = itemsIter.next();
        AppName appNameNext = itemNext.getAppName();
        Soup soupNext = itemNext.getSoup();
        this.item = itemNext;

        if ((item == null) || (appNameNext == null)) {
            // Backup the next store.
            backupNextStore(item, itemNext);
        } else if (soupNext == null) {
            // Backup the next application.
            backupNextApp(item, itemNext);
        } else {
            // Backup the soup.
            backupNextSoup(item, itemNext);
        }
    }

    /**
     * Backup the next selected application.
     *
     * @param itemPrev the previous item that was backed-up.
     * @param item     the next item to backup.
     * @throws BackupException if a backup error occurs.
     */
    protected void backupNextApp(BackupItem itemPrev, BackupItem item) throws BackupException {
        if (DEBUG)
            System.out.println("B 12 backupNextApp " + state + " " + itemPrev + " " + item);
        if (item == null)
            throw new IllegalArgumentException("item expected");
        if (writer == null)
            return;
        if (!isEnabled())
            return;
        if (state != State.BACKUP)
            throw new BackupException("bad state " + state);

        Store store = item.getStore();
        AppName appName = item.getAppName();

        // Notify listeners we are now going to backup <store, appName>
        fireBackupApp(store, appName);

        this.state = State.BACKUP;
        backupNextItem();
    }

    /**
     * Backup the next selected soup.
     *
     * @param itemPrev the previous item that was backed-up.
     * @param item     the next item to backup.
     * @throws BackupException if a backup error occurs.
     */
    protected void backupNextSoup(BackupItem itemPrev, BackupItem item) throws BackupException {
        if (DEBUG)
            System.out.println("B 13 backupNextSoup " + state + " " + itemPrev + " " + item);
        if (item == null)
            throw new IllegalArgumentException("item expected");
        if (writer == null)
            return;
        if (!isEnabled())
            return;
        if (state != State.BACKUP)
            throw new BackupException("bad state " + state);

        Store store = item.getStore();
        AppName appName = item.getAppName();
        Soup soup = item.getSoup();
        this.state = State.BACKUP_SOUP;

        // Notify listeners we are now going to backup <store, appName, soup>
        fireBackupSoup(store, appName, soup);

        // DSetSoupGetInfo -> DSoupInfo -> DSendSoup -> DEntry* ->
        // DBackupSoupDone
        DSetSoupGetInfo getSoupInfo = new DSetSoupGetInfo();
        getSoupInfo.setSoup(soup);
        write(getSoupInfo);
    }

    /**
     * Backup the soup.
     *
     * @param soupInfo the soup to write.
     * @throws BackupException if a backup error occurs.
     */
    protected void backupSoup(Soup soupInfo) throws BackupException {
        if (DEBUG)
            System.out.println("B 14 backupSoup " + state + " " + soupInfo);
        if (soupInfo == null)
            throw new IllegalArgumentException("soup expected");
        if (writer == null)
            return;
        if (!isEnabled())
            return;
        if (state != State.BACKUP_SOUP)
            throw new BackupException("bad state " + state);

        Store store = item.getStore();
        Soup soup = item.getSoup();

        soup.fromSoup(soupInfo);

        writer.startSoup(store.getName(), soup.getName());
        writer.soupDefinition(store.getName(), soup);

        // FIXME write DBackupSoup
        // DSendSoup -> DEntry* -> DBackupSoupDone
        write(new DSendSoup());
    }

    /**
     * Backing up the soup is finished.
     *
     * @throws BackupException if a backup error occurs.
     */
    protected void backupSoupDone() throws BackupException {
        System.out.println("B 16 backupSoupDone " + state);// TODO
        if (writer == null)
            return;
        if (!isEnabled())
            return;
        if (state != State.BACKUP_SOUP)
            throw new BackupException("bad state " + state);

        Store store = item.getStore();
        Soup soup = item.getSoup();

        writer.endSoup(store.getName(), soup);

        this.state = State.BACKUP;
        backupNextItem();
    }

    /**
     * Add a backup listener.
     *
     * @param listener the listener to add.
     */
    public void addListener(BackupListener listener) {
        super.addListener(listener);
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    /**
     * Remove a backup listener.
     *
     * @param listener the listener to remove.
     */
    public void removeListener(BackupListener listener) {
        super.removeListener(listener);
        listeners.remove(listener);
    }

    /**
     * Notify all the listeners that the store will be backed up.
     *
     * @param store the store.
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
     * @param store   the store.
     * @param appName the application.
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
     * @param store   the store.
     * @param appName the application.
     * @param soup    the soup.
     */
    protected void fireBackupSoup(Store store, AppName appName, Soup soup) {
        // Make copy of listeners to avoid ConcurrentModificationException.
        Collection<BackupListener> listenersCopy = new ArrayList<BackupListener>(listeners);
        for (BackupListener listener : listenersCopy) {
            listener.backupSoup(this, store, appName, soup);
        }
    }

    @Override
    protected boolean allowProgressCommandReceiving() {
        return false;
    }

    @Override
    protected boolean allowProgressCommandSending() {
        return false;
    }

    /**
     * Item to backup.
     *
     * @author Moshe
     */
    private static class BackupItem {
        private final Store store;
        private final AppName appName;
        private final Soup soup;

        /**
         * Constructs a new item.
         *
         * @param store   the store.
         * @param appName the application in the store.
         * @param soup    the soup in the application.
         */
        public BackupItem(Store store, AppName appName, Soup soup) {
            super();
            this.store = store;
            this.appName = appName;
            this.soup = soup;
        }

        /**
         * Get the store to backup.
         *
         * @return the store.
         */
        public Store getStore() {
            return store;
        }

        /**
         * Get the application to backup.
         *
         * @return the application.
         */
        public AppName getAppName() {
            return appName;
        }

        /**
         * Get the soup to backup.
         *
         * @return the soup.
         */
        public Soup getSoup() {
            return soup;
        }

        @Override
        public String toString() {
            return "BackupItem:{store:" + store + ", app:" + appName + ", soup:" + soup + "}";
        }
    }

    @Override
    protected Icon createDialogIcon() {
        return JNCUResources.getIcon("/dialog/backup.png");
    }

    /**
     * Backup the next selected store.
     *
     * @param itemPrev the previous item that was backed-up.
     * @param item     the next item to backup.
     * @throws BackupException if a backup error occurs.
     */
    protected void backupNextStore(BackupItem itemPrev, BackupItem item) throws BackupException {
        System.out.println("B 11 backupNextStore " + state + " " + itemPrev + " " + item);// TODO
        if (writer == null)
            return;
        if (!isEnabled())
            return;
        if (state != State.BACKUP)
            throw new BackupException("bad state " + state);

        if (itemPrev != null) {
            Store storePrev = itemPrev.getStore();
            if (storePrev != null) {
                writer.endStore(storePrev);
            }
        }

        if (item == null)
            return;
        Store store = item.getStore();
        this.state = State.BACKUP_STORE;

        // Notify listeners we are now going to backup <store>
        fireBackupStore(store);

        // DSetCurrentStore -> DResult
        DSetCurrentStore setStore = new DSetCurrentStore();
        setStore.setStore(store);
        write(setStore);
    }

    /**
     * Backup the store.
     *
     * @param store the store to write.
     * @throws BackupException if a backup error occurs.
     */
    protected void backupStore(Store store) throws BackupException {
        System.out.println("B 12 backupStore " + state + " " + store);// TODO
        if (store == null)
            throw new IllegalArgumentException("store expected");
        if (writer == null)
            return;
        if (!isEnabled())
            return;
        if (state != State.BACKUP_STORE)
            throw new BackupException("bad state " + state);

        writer.startStore(store.getName());
        writer.storeDefinition(store);

        this.state = State.BACKUP;
        backupNextItem();
    }

    /**
     * Backup the soup entry.
     *
     * @param soupEntry the soup entry to write.
     * @throws BackupException if a backup error occurs.
     */
    protected void backupSoupEntry(SoupEntry soupEntry) throws BackupException {
        System.out.println("B 15 backupSoupEntry " + state + " " + soupEntry);// TODO
        if (soupEntry == null)
            throw new IllegalArgumentException("soup entry expected");
        if (writer == null)
            return;
        if (!isEnabled())
            return;
        if (state != State.BACKUP_SOUP)
            throw new BackupException("bad state " + state);

        Store store = item.getStore();
        Soup soup = item.getSoup();

        writer.soupEntry(store.getName(), soup, soupEntry);
    }

    /**
     * Handle the backup error.
     *
     * @param msg the message.
     * @param e   the error.
     */
    protected void showBackupError(String msg, BackupException e) {
        showError(msg, e);
        writeCancel();
    }
}
