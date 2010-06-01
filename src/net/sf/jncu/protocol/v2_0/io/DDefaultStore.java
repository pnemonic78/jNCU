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
package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.newton.stream.NSOFDecoder;
import net.sf.jncu.newton.stream.NSOFFrame;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.v1_0.io.Store;

/**
 * <tt>kDDefaultStore</tt><br>
 * This command returns a store info frame describing the default store. This
 * frame contains the same info returned for all stores by the
 * <tt>kDStoreNames</tt> command except that it doesn't include the store info.
 * It contains the name, signature, total size, used size and kind.
 * 
 * <pre>
 * 'dfst'
 * length
 * store frame
 * </pre>
 * 
 * @author moshew
 */
public class DDefaultStore extends DockCommandFromNewton {

	public static final String COMMAND = "dfst";

	private Store store;

	/**
	 * Creates a new command.
	 */
	public DDefaultStore() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.protocol.DockCommandFromNewton#decodeData(java.io.InputStream
	 * )
	 */
	@Override
	protected void decodeData(InputStream data) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFFrame frame = (NSOFFrame) decoder.decode(data);
		setStore(frame);
	}

	/**
	 * Get the store information.
	 * 
	 * @return the store.
	 */
	public Store getStore() {
		return store;
	}

	/**
	 * Set the store information.
	 * 
	 * @param store
	 *            the store.
	 */
	protected void setStore(Store store) {
		this.store = store;
	}

	/**
	 * Set the store information.
	 * 
	 * @param frame
	 *            the store frame.
	 */
	protected void setStore(NSOFFrame frame) {
		Store store = new Store();
		store.decodeFrame(frame);
		setStore(store);
	}

}