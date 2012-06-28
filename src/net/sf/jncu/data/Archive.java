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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

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
 * <li><tt>Internal</tt>
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
 * <li>external store #1</li>
 * <li>...</li>
 * <li>external store #n</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author mwaisberg
 * 
 */
public class Archive {

	/** Entry name for the device information. */
	public static final String ENTRY_DEVICE = "device";
	/** Entry name for the stores folder. */
	public static final String ENTRY_STORES = "stores";
	/** Entry name for the store information. */
	public static final String ENTRY_STORE = "store";
	/** Entry name for the packages folder. */
	public static final String ENTRY_PACKAGES = "packages";
	/** Entry name for the soups folder. */
	public static final String ENTRY_SOUPS = "soups";
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
		FileOutputStream fout = null;
		ZipOutputStream out = null;
		// ZipEntry entryDevice = new

		try {
			fout = new FileOutputStream(file);
			out = new ZipOutputStream(fout);

			// out.putNextEntry(entry);
			out.closeEntry();

			out.finish();
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
	}

	/**
	 * Write a store.
	 * 
	 * @param out
	 *            the output.
	 * @param store
	 *            the store.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeStore(ZipOutputStream out, Store store) throws IOException {
	}

	/**
	 * Write the packages.
	 * 
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writePackages(ZipOutputStream out) throws IOException {
	}

	/**
	 * Write the soups.
	 * 
	 * @param out
	 *            the output.
	 * @param store
	 *            the store.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeSoups(ZipOutputStream out, Store store) throws IOException {
	}

	/**
	 * Write a soup.
	 * 
	 * @param out
	 *            the output.
	 * @param soup
	 *            the soup.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeSoup(ZipOutputStream out, Soup soup) throws IOException {
	}

	/**
	 * Write soup entries.
	 * 
	 * @param out
	 *            the output.
	 * @param soup
	 *            the soup.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeSoupEntries(ZipOutputStream out, Soup soup) throws IOException {
	}
}
