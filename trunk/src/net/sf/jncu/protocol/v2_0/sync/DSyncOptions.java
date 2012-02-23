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

import java.io.IOException;
import java.io.InputStream;
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
import net.sf.jncu.protocol.v1_0.io.Store;
import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;

/**
 * This command is sent whenever the user on the Newton has selected selective
 * sync. The frame sent completely specifies which information is to be
 * synchronised.<br>
 * <code>
 * {<br>
 * &nbsp;&nbsp;packages: TRUEREF,<br>
 * &nbsp;&nbsp;syncAll: TRUEREF,<br>
 * &nbsp;&nbsp;stores: [{store info}, {store info}]<br>
 * }</code><br>
 * Each store frame in the stores array contains the same information returned
 * by the <tt>kDStoreNames</tt> command with the addition of soup information.
 * It looks like this: <br>
 * <code>{<br>
 * &nbsp;&nbsp;name: "",<br>
 * &nbsp;&nbsp;signature: 1234,<br>
 * &nbsp;&nbsp;totalsize: 1234,<br>
 * &nbsp;&nbsp;usedsize: 1234,<br>
 * &nbsp;&nbsp;kind: "",<br>
 * &nbsp;&nbsp;soups: [soup names],<br>
 * &nbsp;&nbsp;signatures: [soup signatures]<br>
 * &nbsp;&nbsp;info: {store info frame},<br>
 * }</code><br>
 * If the user has specified to sync all information the frame will look the
 * same except there won't be a soups slot--all soups are assumed.
 * <p>
 * Note that the user can specify which stores to sync while specifying that all
 * soups should be synchronised.
 * <p>
 * If the user specifies that packages should be synchronised the packages flag
 * will be true and the packages soup will be specified in the store frame(s).
 * 
 * <pre>
 * 'sopt'
 * length
 * frame of info
 * </pre>
 * 
 * @author moshew
 */
public class DSyncOptions extends DockCommandFromNewtonScript<NSOFFrame> {

	/** <tt>kDSyncOptions</tt> */
	public static final String COMMAND = "sopt";

	private SyncInfo syncInfo;

	/**
	 * Creates a new command.
	 */
	public DSyncOptions() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.jncu.protocol.DockCommandFromNewton#decodeData(java.io.InputStream
	 * )
	 */
	@Override
	protected void decodeData(InputStream data) throws IOException {
		super.decodeData(data);
		NSOFFrame frame = getResult();
		SyncInfo info = new SyncInfo();
		info.decode(frame);
	}

	/**
	 * Get the sync info.
	 * 
	 * @return the info.
	 */
	public SyncInfo getSyncInfo() {
		return syncInfo;
	}

	/**
	 * Set the sync info.
	 * 
	 * @param syncInfo
	 *            the info.
	 */
	protected void setSyncInfo(SyncInfo syncInfo) {
		this.syncInfo = syncInfo;
	}

	/**
	 * Which information is to be synchronised.
	 * 
	 * @author moshew
	 */
	public static class SyncInfo {

		protected static final NSOFSymbol SLOT_PACKAGES = new NSOFSymbol("packages");
		protected static final NSOFSymbol SLOT_ALL = new NSOFSymbol("syncAll");
		protected static final NSOFSymbol SLOT_STORES = new NSOFSymbol("stores");

		private boolean packages;
		private boolean syncAll;
		private List<Store> stores;

		/**
		 * Creates a new frame.
		 */
		public SyncInfo() {
			super();
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
			if (getStores() != null) {
				List<Store> stores = getStores();
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
					store = new Store();
					store.decode((NSOFFrame) entry);
				}
				setStores(stores);
			}
		}

	}
}
