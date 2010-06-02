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
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * This command asks the desktop to import the file specified by the last path
 * command and the filename string. The response to this can be either a list of
 * translators (if there is more than one applicable translator) or an
 * indication that importing is in progress. If the selected item is at the
 * Desktop level, a frame <code>{Name: "Business", whichVol: -1}</code> is sent.
 * Otherwise, a string is sent.
 * 
 * <pre>
 * 'impt'
 * length
 * filename string
 * </pre>
 * 
 * @author moshew
 */
public class DImportFile extends DockCommandFromNewton {

	/** <tt>kDImportFile</tt> */
	public static final String COMMAND = "impt";

	private NSOFObject filename;

	public DImportFile() {
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
		setFilename(decoder.decode(data));
	}

	/**
	 * Get the file name.
	 * 
	 * @return the file name.
	 */
	public NSOFObject getFilename() {
		return filename;
	}

	/**
	 * Set the file name.
	 * 
	 * @param filename
	 *            the file name.
	 */
	protected void setFilename(NSOFObject filename) {
		this.filename = filename;
	}

}
