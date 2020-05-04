/*
 * Copyright 2010, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jncu.sync;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NewtonStreamedObjectFormat;
import net.sf.jncu.newton.os.ApplicationPackage;
import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.newton.os.Store;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Reads a backup archive file.
 *
 * @author mwaisberg
 */
public class BackupReader {

    /**
     * Index of the modified time stamp.
     */
    protected static final int MODIFED = 0;
    /**
     * Index of the device information.
     */
    protected static final int DEVICE = 0;
    /**
     * Index of the stores folder.
     */
    protected static final int STORES = 0;
    /**
     * Index of the store folder.
     */
    protected static final int STORE_NAME = 1;
    /**
     * Index of the store definition.
     */
    protected static final int STORE = 2;
    /**
     * Index of the packages folder.
     */
    protected static final int PACKAGES = 2;
    /**
     * Index of the package.
     */
    protected static final int PACKAGE = 3;
    /**
     * Index of the soups folder.
     */
    protected static final int SOUPS = 2;
    /**
     * Index of the soup folder.
     */
    protected static final int SOUP_NAME = 3;
    /**
     * Index of the soup definition.
     */
    protected static final int SOUP = 4;
    /**
     * Index of the soup entries.
     */
    protected static final int ENTRIES = 4;

    /**
     * Creates a new reader.
     */
    public BackupReader() {
    }

    /**
     * Reads the archive file.
     *
     * @param file    the source file.
     * @param handler the backup handler.
     * @throws BackupException if an I/O error occurs.
     */
    public void read(File file, BackupHandler handler) throws BackupException {
        InputStream fin = null;
        try {
            fin = new BufferedInputStream(new FileInputStream(file));
            read(fin, handler);
        } catch (BackupException be) {
            throw be;
        } catch (IOException e) {
            throw new BackupException(e);
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Reads the archive file.
     *
     * @param in      the input.
     * @param handler the backup handler.
     * @throws BackupException if a backup error occurs.
     */
    public void read(InputStream in, BackupHandler handler) throws BackupException {
        ZipInputStream zin = null;
        ZipEntry zipEntry;
        String entryName;
        String[] path = null;
        Store store = null;
        ApplicationPackage pkg = null;
        Soup soup = null;
        String storeName;
        String soupName;
        String pkgName;

        try {
            zin = new ZipInputStream(in);
            handler.startBackup();

            while (true) {
                try {
                    zipEntry = zin.getNextEntry();
                } catch (IOException e) {
                    throw new BackupException(e);
                }
                if (zipEntry == null)
                    break;
                entryName = zipEntry.getName();
                path = entryName.split(Archive.DIRECTORY);
                if (path.length == 0)
                    continue;

                pkgName = null;
                pkg = null;

                if (path.length == 1) {
                    storeName = null;
                    store = null;
                    soupName = null;
                    soup = null;

                    if (Archive.ENTRY_DEVICE.equals(path[DEVICE])) {
                        readDevice(handler, zin);
                        continue;
                    }
                    if (Archive.ENTRY_MODIFIED.equals(path[MODIFED])) {
                        readModified(handler, zin);
                        continue;
                    }
                } else if (path.length == 2) {
                    soupName = null;
                    soup = null;

                    if (Archive.ENTRY_STORES.equals(path[STORES])) {
                        storeName = path[STORE_NAME];
                        if ((store != null) && !storeName.equals(store.getName()))
                            handler.endStore(store);
                        handler.startStore(storeName);
                        store = new Store(storeName);
                        continue;
                    }

                    storeName = null;
                    store = null;
                } else if (path.length >= 3) {
                    if (Archive.ENTRY_STORES.equals(path[STORES])) {
                        storeName = path[STORE_NAME];
                        if ((store == null) || !storeName.equals(store.getName())) {
                            if (store != null)
                                handler.endStore(store);
                            handler.startStore(storeName);
                            store = new Store(storeName);
                        }

                        if (Archive.ENTRY_PACKAGES.equals(path[PACKAGES])) {
                            if (soup != null)
                                handler.endSoup(storeName, soup);
                            soupName = null;
                            soup = null;

                            if (path.length >= 4) {
                                pkgName = path[PACKAGE];
                                handler.startPackage(storeName, pkgName);
                                pkg = new ApplicationPackage(pkgName);
                                // TODO readPackage(handler, zin, pkg);
                                handler.endPackage(storeName, pkg);
                                continue;
                            }

                            continue;
                        }

                        if (Archive.ENTRY_SOUPS.equals(path[SOUPS])) {
                            if (path.length >= 4) {
                                soupName = path[SOUP_NAME];
                                if ((soup == null) || !soupName.equals(soup.getName())) {
                                    if (soup != null)
                                        handler.endSoup(storeName, soup);
                                    handler.startSoup(storeName, soupName);
                                    soup = new Soup(soupName);
                                }

                                if (path.length >= 5) {
                                    if (Archive.ENTRY_SOUP.equals(path[SOUP])) {
                                        readSoup(handler, zin, store, soup);
                                        continue;
                                    }
                                    if (Archive.ENTRY_ENTRIES.equals(path[ENTRIES])) {
                                        readSoupEntries(handler, zin, store, soup);
                                        continue;
                                    }
                                    if (path[ENTRIES].startsWith(Archive.ENTRY_ENTRY)) {
                                        readSoupEntry(handler, zin, store, soup);
                                        continue;
                                    }
                                }
                            }
                            continue;
                        } else if (Archive.ENTRY_STORE.equals(path[STORE])) {
                            readStore(handler, zin, store);
                            continue;
                        }
                    }

                    soupName = null;
                    soup = null;
                }
            }

            if (store != null) {
                if (soup != null)
                    handler.endSoup(store.getName(), soup);
                handler.endStore(store);
            }
            handler.endBackup();
        } finally {
            if (zin != null) {
                try {
                    zin.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Read the modified time stamp.
     *
     * @param handler the handler.
     * @param in      the input.
     * @throws BackupException if an I/O error occurs.
     */
    protected void readModified(BackupHandler handler, ZipInputStream in) throws BackupException {
        long hi, lo;
        try {
            hi = NewtonStreamedObjectFormat.ntohl(in) & 0xFFFFFFFFL;
            lo = NewtonStreamedObjectFormat.ntohl(in) & 0xFFFFFFFFL;
        } catch (IOException e) {
            throw new BackupException(e);
        }
        long time = (hi << 32) | lo;

        handler.modified(time);
    }

    /**
     * Read the device information.
     *
     * @param handler the handler.
     * @param in      the input.
     * @throws BackupException if an I/O error occurs.
     */
    protected void readDevice(BackupHandler handler, ZipInputStream in) throws BackupException {
        NSOFDecoder decoder = new NSOFDecoder();
        NSOFFrame frame;
        try {
            frame = (NSOFFrame) decoder.inflate(in);
        } catch (IOException e) {
            throw new BackupException(e);
        }

        NewtonInfo info = new NewtonInfo();
        info.fromFrame(frame);

        handler.deviceInformation(info);
    }

    /**
     * Read a store.
     *
     * @param handler the handler.
     * @param in      the input.
     * @param store   the store to populate.
     * @throws BackupException if an I/O error occurs.
     */
    protected void readStore(BackupHandler handler, ZipInputStream in, Store store) throws BackupException {
        NSOFDecoder decoder = new NSOFDecoder();
        NSOFFrame frame;
        try {
            frame = (NSOFFrame) decoder.inflate(in);
        } catch (IOException e) {
            throw new BackupException(e);
        }
        store.fromFrame(frame);
        handler.storeDefinition(store);
    }

    /**
     * Read a soup.
     *
     * @param handler the handler.
     * @param in      the input.
     * @param store   the store of the soup.
     * @param soup    the soup to populate.
     * @throws BackupException if an I/O error occurs.
     */
    protected void readSoup(BackupHandler handler, ZipInputStream in, Store store, Soup soup) throws BackupException {
        NSOFDecoder decoder = new NSOFDecoder();
        NSOFFrame frame;
        try {
            frame = (NSOFFrame) decoder.inflate(in);
        } catch (IOException e) {
            throw new BackupException(e);
        }
        soup.fromFrame(frame);
        handler.soupDefinition(store.getName(), soup);
    }

    /**
     * Read soup entries.
     *
     * @param handler the handler.
     * @param in      the input.
     * @param store   the store of the soup.
     * @param soup    the soup to populate.
     * @throws BackupException if an I/O error occurs.
     */
    protected void readSoupEntries(BackupHandler handler, ZipInputStream in, Store store, Soup soup) throws BackupException {
        final String storeName = store.getName();
        NSOFDecoder decoder = new NSOFDecoder();
        NSOFArray arr;
        try {
            arr = (NSOFArray) decoder.inflate(in);
        } catch (IOException e) {
            throw new BackupException(e);
        }

        SoupEntry entry;
        int size = arr.length();
        NSOFFrame frame;
        for (int i = 0; i < size; i++) {
            frame = (NSOFFrame) arr.get(i);
            entry = new SoupEntry(frame);
            handler.soupEntry(storeName, soup, entry);
        }
    }

    /**
     * Read a soup entry.
     *
     * @param handler the handler.
     * @param in      the input.
     * @param store   the store of the soup.
     * @param soup    the soup to populate.
     * @throws BackupException if an I/O error occurs.
     */
    protected void readSoupEntry(BackupHandler handler, ZipInputStream in, Store store, Soup soup) throws BackupException {
        final String storeName = store.getName();
        NSOFDecoder decoder = new NSOFDecoder();
        NSOFFrame frame;
        try {
            frame = (NSOFFrame) decoder.inflate(in);
        } catch (IOException e) {
            throw new BackupException(e);
        }

        SoupEntry entry = new SoupEntry(frame);
        handler.soupEntry(storeName, soup, entry);
    }
}
