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

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * This command returns information about the internal store. The info is in the
 * form of a frame that looks like this: <br>
 * <code>{<br>
 * &nbsp;&nbsp;name: "Internal",<br>
 * &nbsp;&nbsp;signature: 1234,<br>
 * &nbsp;&nbsp;totalsize: 1234,<br>
 * &nbsp;&nbsp;usedsize: 1234,<br>
 * &nbsp;&nbsp;kind: "Internal",<br>
 * }</code><br>
 * This is the same frame returned by <tt>kDStoreNames</tt> except that the
 * store info isn't returned.
 * 
 * <pre>
 * 'isto'
 * length
 * store frame
 * </pre>
 * 
 * @author moshew
 */
public class DInternalStore extends DockCommandFromNewton {

	/** <tt>kDInternalStore</tt> */
	public static final String COMMAND = "isto";

	private Store store;

	public DInternalStore() {
		super(COMMAND);
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFFrame frame = (NSOFFrame) decoder.inflate(data);
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
		store.decode(frame);
		setStore(store);
	}

}
