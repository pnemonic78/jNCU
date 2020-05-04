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
package net.sf.jncu.sync;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.newton.os.ApplicationPackage;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.newton.os.Store;

/**
 * Backup from the Newton device to an archive file.<br>
 * The entire archive is written at once.
 *
 * @author mwaisberg
 */
public class ArchiveWriter extends BackupWriter {

    /**
     * Creates a new archive writer.
     *
     * @param file the destination file.
     */
    public ArchiveWriter(File file) {
        super(file);
    }

    /**
     * Creates a new archive writer.
     *
     * @param out the output.
     */
    public ArchiveWriter(OutputStream out) {
        super(out);
    }

    /**
     * Write the archive to the file.
     *
     * @param archive the archive to write.
     * @throws IOException if an I/O error occurs.
     */
    public void write(Archive archive) throws IOException {
        startBackup();

        modified(archive.getModified());
        deviceInformation(archive.getDeviceInfo());
        for (Store store : archive.getStores()) {
            writeStore(archive, store);
        }

        endBackup();
    }

    /**
     * Write a store with its definition, packages, and soups.
     *
     * @param archive the archive to write.
     * @param store   the store.
     * @throws IOException if an I/O error occurs.
     */
    protected void writeStore(Archive archive, Store store) throws IOException {
        startStore(store.getName());

        writePackages(archive, store);
        writeSoups(archive, store);

        endStore(store);
    }

    /**
     * Write the packages.
     *
     * @param archive the archive to write.
     * @param store   the store.
     * @throws IOException if an I/O error occurs.
     */
    protected void writePackages(Archive archive, Store store) throws IOException {
        for (ApplicationPackage pkg : store.getPackages()) {
            writePackage(archive, store, pkg);
        }
    }

    /**
     * Write a package.
     *
     * @param archive the archive to write.
     * @param store   the owner store.
     * @param pkg     the package.
     * @throws IOException if an I/O error occurs.
     */
    protected void writePackage(Archive archive, Store store, ApplicationPackage pkg) throws IOException {
        startPackage(store.getName(), pkg.getName());
        endPackage(store.getName(), pkg);
    }

    /**
     * Write the soups.
     *
     * @param archive the archive to write.
     * @param store   the store.
     * @throws IOException if an I/O error occurs.
     */
    protected void writeSoups(Archive archive, Store store) throws IOException {
        for (Soup soup : store.getSoups()) {
            writeSoup(archive, store, soup);
        }
    }

    /**
     * Write a soup with its definition, and entries.
     *
     * @param archive the archive to write.
     * @param out     the output.
     * @param parent  the parent entry.
     * @param soup    the soup.
     * @throws IOException if an I/O error occurs.
     */
    protected void writeSoup(Archive archive, Store store, Soup soup) throws IOException {
        String storeName = store.getName();
        String soupName = soup.getName();

        startSoup(storeName, soupName);

        for (SoupEntry entry : soup.getEntries()) {
            soupEntry(storeName, soup, entry);
        }

        endSoup(storeName, soup);
    }

    @Override
    protected boolean allowClearStore() {
        return false;
    }

    @Override
    protected boolean allowClearSoup() {
        return false;
    }
}
