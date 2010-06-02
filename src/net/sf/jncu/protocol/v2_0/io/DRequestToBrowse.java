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
import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.newton.stream.NSOFArray;
import net.sf.jncu.newton.stream.NSOFDecoder;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * This command is sent to a desktop that the Newton wishes to browse files on.
 * File types can be 'import', 'packages', 'syncFiles' or an array of strings to
 * use for filtering.
 * 
 * <pre>
 * 'rtbr'
 * length
 * file types
 * </pre>
 * 
 * @author moshew
 */
public class DRequestToBrowse extends DockCommandFromNewton {

	/** <tt>kDRequestToBrowse</tt> */
	public static final String COMMAND = "rtbr";

	/** List of files to import. */
	public static final String IMPORT = "import";
	/** List of packages. */
	public static final String PACKAGES = "packages";
	/** List of files to synchronise. */
	public static final String SYNC_FILES = "syncFiles";

	private final List<String> filters = new ArrayList<String>();

	public DRequestToBrowse() {
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
		filters.clear();
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFArray arr = (NSOFArray) decoder.decode(data);
		NSOFObject[] entries = arr.getValue();
		NSOFString filter;
		for (NSOFObject entry : entries) {
			filter = (NSOFString) entry;
			filters.add(filter.getValue());
		}
	}

	/**
	 * Get the file types for filtering.
	 * 
	 * @return the filter.
	 */
	public List<String> getFilters() {
		return filters;
	}

}
