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
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.JNCUResources;
import net.sf.jncu.Preferences;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.app.DLoadPackageFile;
import net.sf.jncu.protocol.v2_0.data.DImportFile;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.jncu.protocol.v2_0.sync.DRestoreFile;
import net.sf.jncu.translate.TranslatorFactory;
import net.sf.swing.AcceptAllFileFilter;
import net.sf.swing.SwingUtils;

import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Data source for interacting with the Newton file browser.
 *
 * @author Moshe
 */
public abstract class FileChooser extends IconModule {

    /**
     * File chooser events listener.
     */
    public interface FileChooserListener extends IconModuleListener {
        /**
         * The file was selected after the user clicked an approval button.
         *
         * @param chooser the file chooser.
         * @param file    the selected file.
         * @param command the command from the Newton.
         */
	void approveSelection(FileChooser chooser, File file, DockCommandFromNewton command);

        /**
         * The file browsing was cancelled.
         *
         * @param chooser the file chooser.
         */
	void cancelSelection(FileChooser chooser);
    }

    public static final NSOFSymbol IMPORT = DRequestToBrowse.IMPORT;
    public static final NSOFSymbol PACKAGES = DRequestToBrowse.PACKAGES;
    public static final NSOFSymbol SYNC_FILES = DRequestToBrowse.SYNC_FILES;

    /**
     * Property key for default path.
     */
    protected static final String KEY_PATH = "jncu.fileChooser.path";

    private enum State {
        /**
         * None.
         */
        NONE,
        /**
         * Initialised.
         */
        INITIALISED,
        /**
         * Browsing for file.
         */
        BROWSING,
        /**
         * Cancelled.
         */
        CANCELLED,
        /**
         * File selected.
         */
        SELECTED,
        /**
         * Finished.
         */
        FINISHED
    }

    private final List<NSOFString> types = new ArrayList<NSOFString>();
    private State state = State.NONE;
    private File path;
    private File file;
    private DockCommandFromNewton command;
    private final List<FileFilter> filters = new ArrayList<FileFilter>();
    private FileFilter filter;
    private final List<FileChooserListener> listeners = new ArrayList<FileChooserListener>();
    private Store internalStore;

    /**
     * Constructs a new file chooser.
     *
     * @param pipe  the pipe.
     * @param owner the owner window.
     */
    private FileChooser(CDPipe pipe, Window owner) {
        super(JNCUResources.getString("fileChooser"), pipe, owner);
        setName("FileChooser-" + getId());
    }

    /**
     * Constructs a new file chooser.
     *
     * @param pipe  the pipe.
     * @param types the chooser types.
     * @param owner the owner window.
     */
    public FileChooser(CDPipe pipe, Collection<NSOFString> types, Window owner) {
        this(pipe, owner);

        if ((types == null) || types.isEmpty())
            throw new IllegalArgumentException("types required");
        this.types.addAll(types);

        init();
    }

    /**
     * Constructs a new file chooser.
     *
     * @param pipe  the pipe.
     * @param type  the chooser type.
     * @param owner the owner window.
     */
    public FileChooser(CDPipe pipe, NSOFString type, Window owner) {
        this(pipe, owner);

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
            write(new DGetInternalStore());
        } else {
            write(new DResult());
        }
        state = State.INITIALISED;
    }

    @Override
    public void commandReceived(DockCommandFromNewton command) {
        if (!isEnabled())
            return;

        super.commandReceived(command);

        final String cmd = command.getCommand();

        if (DInternalStore.COMMAND.equals(cmd)) {
            DInternalStore cmdStore = (DInternalStore) command;
            internalStore = cmdStore.getStore();
            DResult cmdResult = new DResult();
            write(cmdResult);
        } else if (DGetDefaultPath.COMMAND.equals(cmd)) {
            DPath cmdPath = new DPath();
            cmdPath.setPath(path);
            write(cmdPath);
        } else if (DGetFilesAndFolders.COMMAND.equals(cmd)) {
            sendFiles();
        } else if (DGetFileInfo.COMMAND.equals(cmd)) {
            DGetFileInfo cmdGet = (DGetFileInfo) command;
            File f = new File(path, cmdGet.getFilename());
            DFileInfo cmdInfo = new DFileInfo();
            cmdInfo.setFile(f);
            write(cmdInfo);
        } else if (DSetPath.COMMAND.equals(cmd)) {
            DSetPath cmdSet = (DSetPath) command;
            setPath(cmdSet.getPath());
            sendFiles();
        } else if (DImportFile.COMMAND.equals(cmd)) {
            DImportFile cmdGet = (DImportFile) command;
            String filename = cmdGet.getFilename();
            this.file = new File(path, filename);
            this.command = command;
            state = State.SELECTED;
            fireApproved(this.file, command);
            done();
        } else if (DRestoreFile.COMMAND.equals(cmd)) {
            DRestoreFile cmdGet = (DRestoreFile) command;
            String filename = cmdGet.getFilename();
            this.file = new File(path, filename);
            this.command = command;
            state = State.SELECTED;
            fireApproved(this.file, command);
            done();
        } else if (DLoadPackageFile.COMMAND.equals(cmd)) {
            DLoadPackageFile cmdGet = (DLoadPackageFile) command;
            String filename = cmdGet.getFilename();
            this.file = new File(path, filename);
            this.command = command;
            state = State.SELECTED;
            fireApproved(this.file, command);
            done();
        } else if (DOperationCanceled.COMMAND.equals(cmd)) {
            fireCancelled();
            state = State.CANCELLED;
        }
    }

    @Override
    public void commandSent(DockCommandToNewton command) {
        if (!isEnabled())
            return;

        super.commandSent(command);

        final String cmd = command.getCommand();

        if (DResult.COMMAND.equals(cmd)) {
            state = State.BROWSING;
        } else if (DOperationCanceled.COMMAND.equals(cmd)) {
            state = State.CANCELLED;
        } else if (DOperationDone.COMMAND.equals(cmd)) {
            state = State.FINISHED;
        }
    }

    @Override
    protected void done() {
        super.done();
        state = State.FINISHED;
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
        if (types.contains(IMPORT)) {
            filters.addAll(TranslatorFactory.getInstance().getFileFilters());
        }
        if (types.contains(PACKAGES)) {
            filter = new FileNameExtensionFilter(JNCUResources.getString("packages"), "pkg", "PKG");
            filters.add(filter);
        }
        if (types.contains(SYNC_FILES)) {
            filter = new FileNameExtensionFilter(JNCUResources.getString("backupFiles"), "nbk", "NBK");
            filters.add(filter);
        }
        filter = new AcceptAllFileFilter();
        filters.add(filter);

        setFilter(filters.get(0));
    }

    /**
     * Set the file filter.
     *
     * @param filter the filter.
     */
    protected void setFilter(FileFilter filter) {
        this.filter = filter;
    }

    /**
     * Get the filters.
     *
     * @return the list of filters.
     */
    protected List<FileFilter> getFilters() {
        return filters;
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
     * @param path the path.
     */
    protected void setPath(File path) {
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
    public DockCommandFromNewton getSelectedCommand() {
        return command;
    }

    /**
     * Add a browsing listener.
     *
     * @param listener the listener to add.
     */
    public void addListener(FileChooserListener listener) {
        super.addListener(listener);
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    /**
     * Remove a browsing listener.
     *
     * @param listener the listener to remove.
     */
    public void removeListener(FileChooserListener listener) {
        super.removeListener(listener);
        listeners.remove(listener);
    }

    /**
     * Notify all the listeners that a file has been approved.
     *
     * @param file    the selected file.
     * @param command the received command.
     */
    protected void fireApproved(File file, DockCommandFromNewton command) {
        // Make copy of listeners to avoid ConcurrentModificationException.
        Collection<FileChooserListener> listenersCopy = new ArrayList<FileChooserListener>(listeners);
        for (FileChooserListener listener : listenersCopy) {
            listener.approveSelection(this, file, command);
        }
    }

    /**
     * Notify all the listeners that browsing has been cancelled.
     */
    protected void fireCancelled() {
        // Make copy of listeners to avoid ConcurrentModificationException.
        Collection<FileChooserListener> listenersCopy = new ArrayList<FileChooserListener>(listeners);
        for (FileChooserListener listener : listenersCopy) {
            listener.cancelSelection(this);
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
        if (state == State.CANCELLED)
            return false;
        if (state == State.FINISHED)
            return false;
        return super.isEnabled();
    }

    @Override
    protected void runImpl() throws Exception {
    }
}
