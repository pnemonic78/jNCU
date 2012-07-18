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

import java.util.Collection;
import java.util.HashSet;

import net.sf.jncu.newton.os.NewtonInfo;
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
	private final Collection<Store> stores = new HashSet<Store>();

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
	public Collection<Store> getStores() {
		return stores;
	}

	/**
	 * Set the stores.
	 * 
	 * @param stores
	 *            the list of stores.
	 */
	public void setStores(Collection<Store> stores) {
		this.stores.clear();
		if (stores != null)
			this.stores.addAll(stores);
	}

	/**
	 * Add the store.
	 * 
	 * @param store
	 *            the store.
	 */
	public void addStore(Store store) {
		this.stores.add(store);
	}
}
