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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.newton.os.ApplicationPackage;
import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.Store;

/**
 * Archive file for backup from the Newton device, and restore to the Newton
 * device.
 * <p>
 * The contents of a jNCU archive typically have the following structure:
 * <ul>
 * <li><tt>device</tt></li>
 * <li><tt>stores</tt>
 * <ul>
 * <li>store #1
 * <ul>
 * <li><tt>store</tt></li>
 * <li><tt>packages</tt>
 * <ul>
 * <li>package1<tt>.pkg</tt></li>
 * <li>...</li>
 * </ul>
 * </li>
 * <li><tt>soups</tt>
 * <ul>
 * <li>soup name #1
 * <ul>
 * <li><tt>soup</tt></li>
 * <li><tt>entries</tt></li>
 * </ul>
 * </li>
 * <li>...</li>
 * <li>soup name #n</li>
 * </ul>
 * </ul>
 * </li>
 * <li>...</li>
 * <li>store #n</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author mwaisberg
 * 
 */
public class Archive {

	/** Character to mark entry as directory or folder. */
	protected static final String DIRECTORY = "/";

	/** Entry name for the device information. */
	public static final String ENTRY_DEVICE = "device";
	/** Entry name for the stores folder. */
	public static final String ENTRY_STORES = "stores" + DIRECTORY;
	/** Entry name for the store information. */
	public static final String ENTRY_STORE = "store";
	/** Entry name for the packages folder. */
	public static final String ENTRY_PACKAGES = "packages" + DIRECTORY;
	/** Entry name for the soups folder. */
	public static final String ENTRY_SOUPS = "soups" + DIRECTORY;
	/** Entry name for the soup information. */
	public static final String ENTRY_SOUP = "soup";
	/** Entry name for the entries database. */
	public static final String ENTRY_ENTRIES = "entries";

	private NewtonInfo deviceInfo;
	private final List<Store> stores = new ArrayList<Store>();

	/**
	 * Creates a new archive.
	 */
	public Archive() {
	}

	/**
	 * Get the device information.
	 * 
	 * @return the the information.
	 */
	public NewtonInfo getDeviceInfo() {
		return deviceInfo;
	}

	/**
	 * Set the device information.
	 * 
	 * @param info
	 *            the information.
	 */
	public void setDeviceInfo(NewtonInfo info) {
		this.deviceInfo = info;
	}

	/**
	 * Get the stores.
	 * 
	 * @return the list of stores.
	 */
	public List<Store> getStores() {
		return stores;
	}

	/**
	 * Set the stores.
	 * 
	 * @param stores
	 *            the list of stores.
	 */
	public void setStores(List<Store> stores) {
		this.stores.clear();
		if (stores != null)
			this.stores.addAll(stores);
	}

	/**
	 * Save the archive.
	 * 
	 * @param file
	 *            the destination file.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void save(File file) throws IOException {
		File parent = file.getParentFile();
		File tmp;
		OutputStream fout = null;
		ZipOutputStream out = null;

		try {
			parent.mkdirs();
			tmp = File.createTempFile("jncu", null, parent);
			tmp.deleteOnExit();
			fout = new BufferedOutputStream(new FileOutputStream(tmp));
			out = new ZipOutputStream(fout);

			writeDevice(out);
			writeStores(out);

			out.finish();
			out.close();
			fout = null;
			out = null;

			tmp.renameTo(file);
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (Exception e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Write the device information.
	 * 
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeDevice(ZipOutputStream out) throws IOException {
		NewtonInfo info = getDeviceInfo();
		if (info == null)
			return;
		ZipEntry entry = new ZipEntry(ENTRY_DEVICE);
		out.putNextEntry(entry);
		NSOFFrame frame = info.toFrame();
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(frame, out);
	}

	/**
	 * Write the stores.
	 * 
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeStores(ZipOutputStream out) throws IOException {
		ZipEntry entry = new ZipEntry(ENTRY_STORES);
		out.putNextEntry(entry);

		List<Store> stores = getStores();
		int size = stores.size();
		Store store;
		for (int i = 0; i < size; i++) {
			store = stores.get(i);
			writeStore(out, entry, store, String.valueOf(i));
		}
	}

	/**
	 * Write a store.
	 * 
	 * @param out
	 *            the output.
	 * @param parent
	 *            the parent entry.
	 * @param store
	 *            the store.
	 * @param id
	 *            the file-system-friendly id.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeStore(ZipOutputStream out, ZipEntry parent, Store store, String id) throws IOException {
		ZipEntry entry = new ZipEntry(parent.getName() + id + DIRECTORY);
		out.putNextEntry(entry);

		ZipEntry entryStore = new ZipEntry(entry.getName() + ENTRY_STORE);
		out.putNextEntry(entryStore);
		NSOFFrame frame = store.toFrame();
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(frame, out);

		writePackages(out, entry, store);
		writeSoups(out, entry, store);
	}

	/**
	 * Write the packages.
	 * 
	 * @param out
	 *            the output.
	 * @param parent
	 *            the parent entry.
	 * @param store
	 *            the store.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writePackages(ZipOutputStream out, ZipEntry parent, Store store) throws IOException {
		ZipEntry entry = new ZipEntry(parent.getName() + ENTRY_PACKAGES);
		out.putNextEntry(entry);

		for (ApplicationPackage pkg : store.getPackages()) {
			writePackage(out, entry, pkg);
		}
	}

	/**
	 * Write a soup.
	 * 
	 * @param out
	 *            the output.
	 * @param parent
	 *            the parent entry.
	 * @param pkg
	 *            the package.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writePackage(ZipOutputStream out, ZipEntry parent, ApplicationPackage pkg) throws IOException {
	}

	/**
	 * Write the soups.
	 * 
	 * @param out
	 *            the output.
	 * @param parent
	 *            the parent entry.
	 * @param store
	 *            the store.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeSoups(ZipOutputStream out, ZipEntry parent, Store store) throws IOException {
		ZipEntry entry = new ZipEntry(parent.getName() + ENTRY_SOUPS);
		out.putNextEntry(entry);

		List<Soup> soups = store.getSoups();
		int size = soups.size();
		Soup soup;
		for (int i = 0; i < size; i++) {
			soup = soups.get(i);
			writeSoup(out, entry, soup, String.valueOf(i));
		}
	}

	/**
	 * Write a soup.
	 * 
	 * @param out
	 *            the output.
	 * @param parent
	 *            the parent entry.
	 * @param soup
	 *            the soup.
	 * @param id
	 *            the file-system-friendly id.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeSoup(ZipOutputStream out, ZipEntry parent, Soup soup, String id) throws IOException {
		ZipEntry entry = new ZipEntry(parent.getName() + id + DIRECTORY);
		out.putNextEntry(entry);

		ZipEntry entryStore = new ZipEntry(entry.getName() + ENTRY_SOUP);
		out.putNextEntry(entryStore);
		NSOFFrame frame = soup.toFrame();
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(frame, out);

		writeSoupEntries(out, entry, soup);
	}

	/**
	 * Write soup entries.
	 * 
	 * @param out
	 *            the output.
	 * @param parent
	 *            the parent entry.
	 * @param soup
	 *            the soup.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeSoupEntries(ZipOutputStream out, ZipEntry parent, Soup soup) throws IOException {
		ZipEntry entry = new ZipEntry(parent.getName() + ENTRY_ENTRIES);
		out.putNextEntry(entry);
	}
}
