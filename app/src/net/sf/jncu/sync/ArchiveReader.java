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

import net.sf.jncu.newton.os.ApplicationPackage;
import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.newton.os.Store;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Restore from an archive file to the Newton device.
 *
 * @author mwaisberg
 */
public class ArchiveReader implements BackupHandler {

    private Archive archive;
    private InputStream in;

    /**
     * Creates a new archive reader.
     *
     * @param file the source file.
     * @throws FileNotFoundException if an I/O error occurs.
     */
    public ArchiveReader(File file) throws FileNotFoundException {
        this.in = new BufferedInputStream(new FileInputStream(file));
    }

    /**
     * Creates a new archive reader.
     *
     * @param in the source input.
     */
    public ArchiveReader(InputStream in) {
        this.in = in;
    }

    /**
     * Reads the archive.
     *
     * @return the archive - {@code null} otherwise.
     * @throws BackupException if a backup error occurs.
     */
    public Archive read() throws BackupException {
        this.archive = null;
        BackupReader reader = new BackupReader();
        reader.read(in, this);
        return archive;
    }

    @Override
    public void startBackup() throws BackupException {
        this.archive = new Archive();
    }

    @Override
    public void endBackup() throws BackupException {
    }

    @Override
    public void modified(long time) throws BackupException {
        archive.setModified(time);
    }

    @Override
    public void deviceInformation(NewtonInfo info) throws BackupException {
        archive.setDeviceInfo(info);
    }

    @Override
    public void startStore(String storeName) throws BackupException {
        Store store = new Store(storeName);
        archive.addStore(store);
    }

    @Override
    public void storeDefinition(Store store) throws BackupException {
        Store storeArchive = archive.findStore(store.getName());
        storeArchive.fromFrame(store.toFrame());
    }

    @Override
    public void endStore(Store store) throws BackupException {
    }

    @Override
    public void startPackage(String storeName, String pkgName) throws BackupException {
    }

    @Override
    public void endPackage(String storeName, ApplicationPackage pkg) throws BackupException {
        Store store = archive.findStore(storeName);
        store.addPackage(pkg);
    }

    @Override
    public void startSoup(String storeName, String soupName) throws BackupException {
        Store store = archive.findStore(storeName);
        Soup soup = new Soup(soupName);
        store.addSoup(soup);
    }

    @Override
    public void soupDefinition(String storeName, Soup soup) throws BackupException {
        Store store = archive.findStore(storeName);
        Soup soupArchive = store.findSoup(soup.getName());
        soupArchive.fromFrame(soup.toFrame());
    }

    @Override
    public void endSoup(String storeName, Soup soup) throws BackupException {
    }

    @Override
    public void soupEntry(String storeName, Soup soup, SoupEntry entry) throws BackupException {
        Store store = archive.findStore(storeName);
        Soup soupArchive = store.findSoup(soup.getName(), soup.getSignature());
        if (soupArchive == null) {
            soupArchive = store.findSoup(soup.getName());
            if (soupArchive == null)
                throw new BackupException("soup is orphan");
        }
        soupArchive.addEntry(entry);
    }
}
