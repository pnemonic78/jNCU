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
package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.newton.stream.NSOFDecoder;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * This command specifies the indexes that should be created for the current
 * soup.
 * 
 * <pre>
 * 'didx'
 * length
 * indexes
 * </pre>
 */
public class DIndexDescription extends DockCommandFromNewton {

	/** <tt>kDIndexDescription</tt> */
	public static final String COMMAND = "indx";

	private NSOFObject indexes;

	/**
	 * Creates a new command.
	 */
	public DIndexDescription() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		setIndexes(decoder.decode(data));
	}

	/**
	 * Get the indexes.
	 * 
	 * @return the indexes.
	 */
	public NSOFObject getIndexes() {
		return indexes;
	}

	/**
	 * Set the indexes.
	 * 
	 * @param indexes
	 *            the indexes.
	 */
	public void setIndexes(NSOFObject indexes) {
		this.indexes = indexes;
	}

}
