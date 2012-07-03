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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.newton.os.ApplicationPackage;
import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.newton.os.Store;

/**
 * Restore from an archive file to the Newton device.
 * 
 * @author mwaisberg
 * 
 */
public class ArchiveReader {

	private final File file;

	/**
	 * Creates a new archive reader.
	 * 
	 * @param file
	 *            the source file.
	 */
	public ArchiveReader(File file) {
		this.file = file;
	}

	/**
	 * Reads the archive from the file.
	 * 
	 * @return the archive.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public Archive read() throws IOException {
		InputStream fin = null;
		ZipInputStream in = null;
		Archive archive = new Archive();
		ZipEntry entry;
		String entryName;
		String[] path = null;
		Map<String, Store> stores = new HashMap<String, Store>();
		Store store = null;
		ApplicationPackage pkg = null;
		Soup soup = null;
		boolean isDirectory;
		String storeName;
		String soupName;
		String pkgName;

		try {
			fin = new BufferedInputStream(new FileInputStream(file));
			in = new ZipInputStream(fin);

			while (true) {
				entry = in.getNextEntry();
				if (entry == null)
					break;
				entryName = entry.getName();
				isDirectory = entryName.endsWith(Archive.DIRECTORY);
				if (isDirectory)
					continue;
				path = entryName.split(Archive.DIRECTORY);
				if (path.length == 0)
					continue;

				store = null;
				pkg = null;
				soup = null;
				storeName = null;
				soupName = null;

				if (path.length == 1) {
					if (Archive.ENTRY_DEVICE.equals(path[0])) {
						readDevice(archive, in, entry);
						continue;
					}
				} else if (path.length >= 3) {
					if (Archive.ENTRY_STORES.equals(path[0])) {
						storeName = path[1];
						store = stores.get(storeName);
						if (store == null) {
							store = new Store(storeName);
							stores.put(storeName, store);
							archive.getStores().add(store);
						}

						if (path.length >= 4) {
							if (Archive.ENTRY_PACKAGES.equals(path[2])) {
								pkgName = path[3];
								pkg = new ApplicationPackage(pkgName);
								readPackage(archive, in, entry, pkg);
								continue;
							}

							if (path.length >= 5) {
								if (Archive.ENTRY_SOUPS.equals(path[2]) && (store != null)) {
									soupName = path[3];
									soup = store.findSoup(soupName);
									if (soup == null) {
										soup = new Soup(soupName);
										store.getSoups().add(soup);
									}

									if (Archive.ENTRY_SOUP.equals(path[4])) {
										readSoup(archive, in, entry, soup);
										continue;
									}
									if (Archive.ENTRY_ENTRIES.equals(path[4])) {
										readSoupEntries(archive, in, entry, soup);
										continue;
									}
								}
							}
						} else if (Archive.ENTRY_STORE.equals(path[2])) {
							readStore(archive, in, entry, store);
							continue;
						}
					}
				}
			}
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (Exception e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}

		return archive;
	}

	/**
	 * Read the device information.
	 * 
	 * @param archive
	 *            the archive to read.
	 * @param in
	 *            the input.
	 * @param entry
	 *            the entry.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readDevice(Archive archive, ZipInputStream in, ZipEntry entry) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFFrame frame = (NSOFFrame) decoder.inflate(in);

		NewtonInfo info = new NewtonInfo();
		info.fromFrame(frame);

		archive.setDeviceInfo(info);
	}

	/**
	 * Read the stores.
	 * 
	 * @param archive
	 *            the archive to read.
	 * @param in
	 *            the input.
	 * @param entry
	 *            the entry.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readStores(Archive archive, ZipInputStream in, ZipEntry entry) throws IOException {
	}

	/**
	 * Read a store.
	 * 
	 * @param archive
	 *            the archive to read.
	 * @param in
	 *            the input.
	 * @param entry
	 *            the entry.
	 * @param store
	 *            the store to populate.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readStore(Archive archive, ZipInputStream in, ZipEntry entry, Store store) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFFrame frame = (NSOFFrame) decoder.inflate(in);
		store.decodeFrame(frame);
	}

	/**
	 * Read a package.
	 * 
	 * @param archive
	 *            the archive to read.
	 * @param in
	 *            the input.
	 * @param entry
	 *            the entry.
	 * @param pkg
	 *            the package to populate.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readPackage(Archive archive, ZipInputStream in, ZipEntry entry, ApplicationPackage pkg) throws IOException {
		// TODO implement me!
	}

	/**
	 * Read a soup.
	 * 
	 * @param archive
	 *            the archive to read.
	 * @param in
	 *            the input.
	 * @param entry
	 *            the entry.
	 * @param soup
	 *            the soup to populate.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readSoup(Archive archive, ZipInputStream in, ZipEntry entry, Soup soup) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFFrame frame = (NSOFFrame) decoder.inflate(in);
		soup.decodeFrame(frame);
	}

	/**
	 * Read soup entries.
	 * 
	 * @param archive
	 *            the archive to read.
	 * @param in
	 *            the input.
	 * @param entry
	 *            the entry.
	 * @param soup
	 *            the soup to populate.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readSoupEntries(Archive archive, ZipInputStream in, ZipEntry entry, Soup soup) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFArray arr = (NSOFArray) decoder.inflate(in);

		List<SoupEntry> entries = new ArrayList<SoupEntry>();
		int size = arr.getLength();
		NSOFFrame frame;
		for (int i = 0; i < size; i++) {
			frame = (NSOFFrame) arr.get(i);
			entries.add(new SoupEntry(frame));
		}
		soup.setEntries(entries);
	}
}
