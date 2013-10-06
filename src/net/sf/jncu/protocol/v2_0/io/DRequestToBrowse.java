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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.TreeSet;

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

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
public class DRequestToBrowse extends BaseDockCommandFromNewton {

	/** <tt>kDRequestToBrowse</tt> */
	public static final String COMMAND = "rtbr";

	/** List of files to import. */
	public static final NSOFSymbol IMPORT = new NSOFSymbol("Import");
	/** List of packages to install. */
	public static final NSOFSymbol PACKAGES = new NSOFSymbol("packages");
	/** List of files to synchronise. */
	public static final NSOFSymbol SYNC_FILES = new NSOFSymbol("syncFiles");

	private final Collection<NSOFString> types = new TreeSet<NSOFString>();

	/**
	 * Constructs a new command.
	 */
	public DRequestToBrowse() {
		super(COMMAND);
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
		types.clear();
		NSOFDecoder decoder = new NSOFDecoder();

		int length = getLength();
		byte[] buf = new byte[length];
		readAll(data, buf);
		ByteArrayInputStream in = new ByteArrayInputStream(buf);
		int start, end;
		NSOFObject next;

		while (length > 0) {
			start = in.available();
			next = decoder.inflate(in);
			end = in.available();
			length -= (start - end);

			if (next instanceof NSOFString) {
				NSOFString filter = (NSOFString) next;
				types.add(filter);
			} else {
				throw new ClassCastException("invalid filter: " + next);
			}
		}
	}

	/**
	 * Get the file types for filtering.
	 * 
	 * @return the list of types.
	 */
	public Collection<NSOFString> getTypes() {
		return types;
	}

}
