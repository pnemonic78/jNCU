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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NewtonStreamedObjectFormat;
import net.sf.jncu.newton.os.ApplicationPackage;
import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.newton.os.Store;

/**
 * Backup from the Newton device to an archive file.
 * 
 * @author mwaisberg
 */
public class BackupWriter implements BackupHandler {

	private File file;
	private File temp;
	private ZipOutputStream out;
	private String storeWriting;
	private String soupWriting;

	/**
	 * Creates a new archive writer.
	 * 
	 * @param file
	 *            the destination file.
	 */
	public BackupWriter(File file) {
		super();
		if (file == null)
			throw new IllegalArgumentException("file required");
		this.file = file;
	}

	/**
	 * Creates a new archive writer.
	 * 
	 * @param out
	 *            the output.
	 */
	public BackupWriter(OutputStream out) {
		super();
		if (out == null)
			throw new IllegalArgumentException("output required");
		this.out = new ZipOutputStream(out);
	}

	/**
	 * Put zip entry.
	 * 
	 * @param entryName
	 *            the entry name.
	 * @throws BackupException
	 *             if a backup error occurs.
	 */
	protected void putEntry(String entryName) throws BackupException {
		ZipEntry entry = new ZipEntry(entryName);
		try {
			out.putNextEntry(entry);
			out.flush();
		} catch (IOException e) {
			throw new BackupException(e);
		}
	}

	/**
	 * Flatten the NSOF object.
	 * 
	 * @param object
	 *            the object to encode.
	 * @throws BackupException
	 *             if a backup error occurs.
	 */
	protected void flatten(NSOFObject object) throws BackupException {
		NSOFEncoder encoder = new NSOFEncoder();
		try {
			encoder.flatten(object, out);
			out.flush();
		} catch (IOException e) {
			throw new BackupException(e);
		}
	}

	@Override
	public void startBackup() throws BackupException {
		if (out == null) {
			File parent = file.getParentFile();
			parent.mkdirs();

			try {
				temp = File.createTempFile("jncu", null, parent);
				temp.deleteOnExit();
			} catch (IOException e) {
				throw new BackupException(e);
			}

			try {
				this.out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(temp)));
			} catch (FileNotFoundException e) {
				throw new BackupException(e);
			}
		}
	}

	@Override
	public void endBackup() throws BackupException {
		if (out != null) {
			try {
				out.finish();
			} catch (IOException e) {
				throw new BackupException(e);
			}
			try {
				out.close();
			} catch (Exception e) {
			}
		}

		if (file != null) {
			file.delete();
			temp.renameTo(file);
		}
	}

	@Override
	public void modified(long time) throws BackupException {
		String name = Archive.ENTRY_MODIFIED;
		putEntry(name);
		try {
			NewtonStreamedObjectFormat.htonl(System.currentTimeMillis(), out);
		} catch (IOException e) {
			throw new BackupException(e);
		}
	}

	@Override
	public void deviceInformation(NewtonInfo info) throws BackupException {
		if (info == null)
			return;
		putEntry(Archive.ENTRY_DEVICE);
		flatten(info.toFrame());
	}

	@Override
	public void startStore(String storeName) throws BackupException {
		// Keep track of the current store for orphaned soups.
		if (storeWriting != null)
			throw new BackupException("store already started");
		this.storeWriting = storeName;
		this.soupWriting = null;

		String name = Archive.ENTRY_STORES + Archive.DIRECTORY;
		name = name + storeName + Archive.DIRECTORY;
		putEntry(name);
	}

	@Override
	public void storeDefinition(Store store) throws BackupException {
		String name = Archive.ENTRY_STORES + Archive.DIRECTORY;
		name = name + store.getName() + Archive.DIRECTORY;
		name = name + Archive.ENTRY_STORE;
		putEntry(name);
		flatten(store.toFrame());
	}

	@Override
	public void endStore(Store store) throws BackupException {
		if (!storeWriting.equals(store.getName()))
			throw new BackupException("wrong store");
		this.storeWriting = null;
		this.soupWriting = null;

		// Free up some memory heap.
		if (allowClearStore()) {
			store.setPackages(null);
			store.setSoups(null);
		}
	}

	/**
	 * Allow the store to be cleared?
	 * 
	 * @return allow?
	 */
	protected boolean allowClearStore() {
		return true;
	}

	@Override
	public void startPackage(String storeName, String pkgName) throws BackupException {
	}

	@Override
	public void endPackage(String storeName, ApplicationPackage pkg) throws BackupException {
	}

	@Override
	public void startSoup(String storeName, String soupName) throws BackupException {
		// Keep track of the current soup for orphaned entries.
		if (!storeWriting.equals(storeName))
			throw new BackupException("wrong store");
		if (soupWriting != null)
			throw new BackupException("soup already started");
		this.soupWriting = soupName;

		String name = Archive.ENTRY_STORES + Archive.DIRECTORY;
		name = name + storeName + Archive.DIRECTORY;
		name = name + Archive.ENTRY_SOUPS + Archive.DIRECTORY;
		name = name + soupName + Archive.DIRECTORY;
		putEntry(name);
	}

	@Override
	public void soupDefinition(String storeName, Soup soup) throws BackupException {
		String name = Archive.ENTRY_STORES + Archive.DIRECTORY;
		name = name + storeName + Archive.DIRECTORY;
		name = name + Archive.ENTRY_SOUPS + Archive.DIRECTORY;
		name = name + soup.getName() + Archive.DIRECTORY;
		name = name + Archive.ENTRY_SOUP;
		putEntry(name);
		flatten(soup.toFrame());
	}

	@Override
	public void endSoup(String storeName, Soup soup) throws BackupException {
		if (!storeWriting.equals(storeName))
			throw new BackupException("wrong store");
		if (!soupWriting.equals(soup.getName()))
			throw new BackupException("wrong soup");
		this.soupWriting = null;

		// Write the soup entries.
		String name = Archive.ENTRY_STORES + Archive.DIRECTORY;
		name = name + storeName + Archive.DIRECTORY;
		name = name + Archive.ENTRY_SOUPS + Archive.DIRECTORY;
		name = name + soup.getName() + Archive.DIRECTORY;
		name = name + Archive.ENTRY_ENTRIES;
		putEntry(name);

		// Copy the list to avoid concurrency problems.
		final Collection<SoupEntry> entries = new ArrayList<SoupEntry>(soup.getEntries());
		NSOFArray arr = new NSOFPlainArray(entries.size());
		int i = 0;
		for (SoupEntry item : entries)
			arr.set(i++, item);
		flatten(arr);

		// Free up some memory heap.
		if (allowClearSoup()) {
			soup.setEntries(null);
		}
	}

	/**
	 * Allow the soup to be cleared?
	 * 
	 * @return allow?
	 */
	protected boolean allowClearSoup() {
		return true;
	}

	@Override
	public void soupEntry(String storeName, Soup soup, SoupEntry entry) throws BackupException {
		if (!storeWriting.equals(storeName))
			throw new BackupException("wrong store");
		if (!soupWriting.equals(soup.getName()))
			throw new BackupException("wrong soup");

		// Cache the soup entry.
		soup.addEntry(entry);
	}
}
