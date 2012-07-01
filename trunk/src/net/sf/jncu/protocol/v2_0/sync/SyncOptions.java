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
package net.sf.jncu.protocol.v2_0.sync;

import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFNil;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.fdil.NSOFTrue;
import net.sf.jncu.newton.os.Store;

/**
 * Which information is to be synchronised.
 * 
 * @author moshew
 */
public class SyncOptions {

	/** Sync packages? */
	public static final NSOFSymbol SLOT_PACKAGES = new NSOFSymbol("packages");
	/** Sync all? */
	public static final NSOFSymbol SLOT_ALL = new NSOFSymbol("syncAll");
	/** Array of stores to sync. */
	public static final NSOFSymbol SLOT_STORES = new NSOFSymbol("stores");

	private boolean packages;
	private boolean syncAll;
	private List<Store> stores;

	/**
	 * Creates new options.
	 */
	public SyncOptions() {
	}

	/**
	 * Synchronise packages?
	 * 
	 * @return the packages.
	 */
	public boolean isPackages() {
		return packages;
	}

	/**
	 * Set synchronise packages.
	 * 
	 * @param packages
	 *            the packages.
	 */
	public void setPackages(boolean packages) {
		this.packages = packages;
	}

	/**
	 * Synchronise all?
	 * 
	 * @return true if sync all.
	 */
	public boolean isSyncAll() {
		return syncAll;
	}

	/**
	 * Set synchronise all.
	 * 
	 * @param syncAll
	 *            sync all?
	 */
	public void setSyncAll(boolean syncAll) {
		this.syncAll = syncAll;
	}

	/**
	 * Get the stores to synchronise.
	 * 
	 * @return the list of stores.
	 */
	public List<Store> getStores() {
		return stores;
	}

	/**
	 * Set the stores to synchronise.
	 * 
	 * @param stores
	 *            the list of stores.
	 */
	public void setStores(List<Store> stores) {
		this.stores = stores;
	}

	/**
	 * Get the frame.
	 * 
	 * @return the frame.
	 */
	public NSOFFrame toFrame() {
		NSOFFrame frame = new NSOFFrame();
		frame.put(SLOT_PACKAGES, isPackages() ? new NSOFTrue() : new NSOFNil());
		frame.put(SLOT_ALL, isSyncAll() ? new NSOFTrue() : new NSOFNil());
		final List<Store> stores = getStores();
		if (stores != null) {
			NSOFFrame[] entries = new NSOFFrame[stores.size()];
			int i = 0;
			for (Store store : stores) {
				entries[i++] = store.toFrame();
			}
			frame.put(SLOT_STORES, new NSOFPlainArray(entries));
		}
		return frame;
	}

	/**
	 * Decode the frame.
	 * 
	 * @param frame
	 *            the frame.
	 */
	public void decode(NSOFFrame frame) {
		NSOFObject value;

		value = frame.get(SLOT_ALL);
		setSyncAll(false);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setSyncAll(imm.isTrue());
		}

		value = frame.get(SLOT_PACKAGES);
		setPackages(false);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setPackages(imm.isTrue());
		}

		value = frame.get(SLOT_STORES);
		setStores(null);
		if (value != null) {
			NSOFArray arr = (NSOFArray) value;
			List<Store> stores = new ArrayList<Store>();
			NSOFObject[] entries = arr.getValue();
			Store store;
			for (NSOFObject entry : entries) {
				store = new Store(null);
				store.decodeFrame((NSOFFrame) entry);
			}
			setStores(stores);
		}
	}
}
