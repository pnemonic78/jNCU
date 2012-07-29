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
import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.newton.os.SoupIndex;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * This command specifies the indexes that should be created for the current
 * soup.
 * 
 * <pre>
 * 'indx'
 * length
 * indexes
 * </pre>
 */
public class DIndexDescription extends DockCommandFromNewton {

	/** <tt>kDIndexDescription</tt> */
	public static final String COMMAND = "indx";

	private final List<SoupIndex> indexes = new ArrayList<SoupIndex>();

	/**
	 * Creates a new command.
	 */
	public DIndexDescription() {
		super(COMMAND);
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
		setIndexes(null);

		NSOFDecoder decoder = new NSOFDecoder();
		NSOFObject o = decoder.inflate(data);

		if (!NSOFImmediate.isNil(o)) {
			NSOFArray arr = (NSOFArray) o;
			int size = arr.length();
			SoupIndex index;
			NSOFFrame frame;

			for (int i = 0; i < size; i++) {
				frame = (NSOFFrame) arr.get(i);
				index = new SoupIndex();
				index.fromFrame(frame);
				indexes.add(index);
			}
		}
	}

	/**
	 * Get the soup indexes.
	 * 
	 * @return the array of indexes.
	 */
	public List<SoupIndex> getIndexes() {
		return indexes;
	}

	/**
	 * Set the soup indexes.
	 * 
	 * @param indexes
	 *            the array of indexes.
	 */
	protected void setIndexes(List<SoupIndex> indexes) {
		this.indexes.clear();
		if (indexes != null)
			this.indexes.addAll(indexes);
	}
}
