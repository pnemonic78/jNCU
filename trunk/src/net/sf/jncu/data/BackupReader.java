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
package net.sf.jncu.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NewtonStreamedObjectFormat;
import net.sf.jncu.newton.os.ApplicationPackage;
import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.newton.os.Store;

/**
 * Reads a backup archive file.
 * 
 * @author mwaisberg
 */
public class BackupReader {

	/** Index of the modified time stamp. */
	protected static final int MODIFED = 0;
	/** Index of the device information. */
	protected static final int DEVICE = 0;
	/** Index of the stores folder. */
	protected static final int STORES = 0;
	/** Index of the store folder. */
	protected static final int STORE_NAME = 1;
	/** Index of the store definition. */
	protected static final int STORE = 2;
	/** Index of the packages folder. */
	protected static final int PACKAGES = 2;
	/** Index of the package. */
	protected static final int PACKAGE = 3;
	/** Index of the soups folder. */
	protected static final int SOUPS = 2;
	/** Index of the soup folder. */
	protected static final int SOUP_NAME = 3;
	/** Index of the soup definition. */
	protected static final int SOUP = 4;
	/** Index of the soup entries. */
	protected static final int ENTRIES = 4;

	/**
	 * Creates a new reader.
	 */
	public BackupReader() {
	}

	/**
	 * Reads the archive file.
	 * 
	 * @param file
	 *            the source file.
	 * @param handler
	 *            the backup handler.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void read(File file, BackupHandler handler) throws IOException {
		InputStream fin = null;
		try {
			fin = new BufferedInputStream(new FileInputStream(file));
			read(fin, handler);
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
	 * @param in
	 *            the input.
	 * @param handler
	 *            the backup handler.
	 * @throws BackupException
	 *             if a backup error occurs.
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
						try {
							readDevice(handler, zin);
						} catch (IOException e) {
							throw new BackupException(e);
						}
						continue;
					}
					if (Archive.ENTRY_MODIFIED.equals(path[MODIFED])) {
						try {
							readModified(handler, zin);
						} catch (IOException e) {
							throw new BackupException(e);
						}
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
										try {
											readSoup(handler, zin, store, soup);
										} catch (IOException e) {
											throw new BackupException(e);
										}
										continue;
									}
									if (Archive.ENTRY_ENTRIES.equals(path[ENTRIES])) {
										try {
											readSoupEntries(handler, zin, store, soup);
										} catch (IOException e) {
											throw new BackupException(e);
										}
										continue;
									}
								}
							}
							continue;
						} else if (Archive.ENTRY_STORE.equals(path[STORE])) {
							try {
								readStore(handler, zin, store);
							} catch (IOException e) {
								throw new BackupException(e);
							}
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
	 * @param handler
	 *            the handler.
	 * @param in
	 *            the input.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readModified(BackupHandler handler, ZipInputStream in) throws IOException {
		long hi = NewtonStreamedObjectFormat.ntohl(in) & 0xFFFFFFFFL;
		long lo = NewtonStreamedObjectFormat.ntohl(in) & 0xFFFFFFFFL;
		long time = (hi << 32) | lo;

		handler.modified(time);
	}

	/**
	 * Read the device information.
	 * 
	 * @param handler
	 *            the handler.
	 * @param in
	 *            the input.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readDevice(BackupHandler handler, ZipInputStream in) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFFrame frame = (NSOFFrame) decoder.inflate(in);

		NewtonInfo info = new NewtonInfo();
		info.fromFrame(frame);

		handler.deviceInformation(info);
	}

	/**
	 * Read a store.
	 * 
	 * @param handler
	 *            the handler.
	 * @param in
	 *            the input.
	 * @param store
	 *            the store to populate.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readStore(BackupHandler handler, ZipInputStream in, Store store) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFFrame frame = (NSOFFrame) decoder.inflate(in);
		store.fromFrame(frame);
		handler.storeDefinition(store);
	}

	/**
	 * Read a soup.
	 * 
	 * @param handler
	 *            the handler.
	 * @param in
	 *            the input.
	 * @param store
	 *            the store of the soup.
	 * @param soup
	 *            the soup to populate.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readSoup(BackupHandler handler, ZipInputStream in, Store store, Soup soup) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFFrame frame = (NSOFFrame) decoder.inflate(in);
		soup.fromFrame(frame);
		handler.soupDefinition(store.getName(), soup);
	}

	/**
	 * Read soup entries.
	 * 
	 * @param handler
	 *            the handler.
	 * @param in
	 *            the input.
	 * @param store
	 *            the store of the soup.
	 * @param soup
	 *            the soup to populate.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readSoupEntries(BackupHandler handler, ZipInputStream in, Store store, Soup soup) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFArray arr = (NSOFArray) decoder.inflate(in);

		SoupEntry entry;
		int size = arr.length();
		NSOFFrame frame;
		for (int i = 0; i < size; i++) {
			frame = (NSOFFrame) arr.get(i);
			entry = new SoupEntry(frame);
			handler.soupEntry(store.getName(), soup, entry);
		}
	}
}
