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
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFBoolean;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.newton.os.Store;

/**
 * What information is to be synchronised?
 * <p>
 * For example:<br>
 * <tt>frame={<br>
 * &nbsp;&nbsp;packages=true,<br>
 * &nbsp;&nbsp;syncall=true,<br>
 * &nbsp;&nbsp;stores=[<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;{<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;soups=["Calendar", "Calendar Notes", "Calls", "Cities", "Countries", "DaylightSavings", "Directory", "InBox", "Library", "MathStar:SofT", "Names", "Nebula:SofT", "NewtWorks", "Notes", "OutBox", "Packages", "passKeep:leere", "Repeat Meetings", "Repeat Notes", "Set104289611:Utilities:SBM", "Set104320876:Utilities:SBM", "System", "SystemAlarmSoup", "TermLimit", "To do", "To Do List"],<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;defaultStore=true,<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;name="Internal",<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;signatures=[-23730660, 82015374, -30102884, -327307689, 117956486, 369071447, 225165878, -328896044, 496725532, 459854783, 123338611, 46314278, -441166666, -85226116, -132887324, 131798843, -359696375, -482977699, -252110557, 231200219, -77219017, 442001900, -493735094, 159604293, -236879162, 206622611],<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;kind="Internal",<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;signature=29250252,<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;info={defaultStore=true, lastrestorefromcard=-480951953}<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;},<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;{<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;soups=["Calendar", "Calendar Notes", "Eclipse:SofT", "EquSol:SofT", "Names", "NewtWorks", "Notes", "Packages", "Repeat Meetings", "Repeat Notes", "To Do List"],<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;name="2MB PCMCIA",<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;signatures=[-525833886, 278844033, 159604293, 159604293, 159604293, 262277348, 159604293, 262277348, 278636274, 85896884, 506753498],<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;kind="Flash storage card",<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;signature=185942560,<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;info={defaultStore=nil}<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br>
 * &nbsp;&nbsp;]<br>
 * }</tt>
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
	private final Collection<Store> stores = new TreeSet<Store>();

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
	public Collection<Store> getStores() {
		return stores;
	}

	/**
	 * Set the stores to synchronise.
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
	 * Get the frame.
	 * 
	 * @return the frame.
	 */
	public NSOFFrame toFrame() {
		NSOFFrame frame = new NSOFFrame();
		frame.put(SLOT_PACKAGES, isPackages() ? NSOFBoolean.TRUE : NSOFBoolean.FALSE);
		frame.put(SLOT_ALL, isSyncAll() ? NSOFBoolean.TRUE : NSOFBoolean.FALSE);
		final Collection<Store> stores = getStores();
		NSOFArray entries = new NSOFPlainArray(stores.size());
		int i = 0;
		for (Store store : stores)
			entries.set(i++, store.toFrame());
		frame.put(SLOT_STORES, entries);
		return frame;
	}

	/**
	 * Decode the frame.
	 * 
	 * @param frame
	 *            the frame.
	 */
	public void decodeFrame(NSOFFrame frame) {
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
		if (!NSOFImmediate.isNil(value)) {
			NSOFArray arr = (NSOFArray) value;
			List<Store> stores = new ArrayList<Store>();
			NSOFObject[] entries = arr.getValue();
			Store store;
			for (NSOFObject entry : entries) {
				store = new Store(null);
				store.fromFrame((NSOFFrame) entry);
			}
			setStores(stores);
		}
	}
}
