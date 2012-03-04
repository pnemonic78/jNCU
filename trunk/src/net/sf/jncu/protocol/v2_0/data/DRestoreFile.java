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
package net.sf.jncu.protocol.v2_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * This command asks the desktop to restore the file specified by the last path
 * command and the filename. If the selected item is at the Desktop level, a
 * frame <code>{name: "Business", whichVol:-1}</code> is sent. Otherwise, a
 * string is sent.
 * 
 * <pre>
 * 'rsfl'
 * length
 * filename
 * </pre>
 * 
 * @author moshew
 */
public class DRestoreFile extends DockCommandFromNewton {

	/** <tt>kDRestoreFile</tt> */
	public static final String COMMAND = "rsfl";

	private String filename;

	/**
	 * Creates a new command.
	 */
	public DRestoreFile() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		setFilename((String) null);
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFString name = (NSOFString) decoder.inflate(data);
		setFilename(name);
	}

	/**
	 * Get the file name.
	 * 
	 * @return the file name.
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Set the file name.
	 * 
	 * @param filename
	 *            the file name.
	 */
	protected void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Set the file name.
	 * 
	 * @param filename
	 *            the file name.
	 */
	protected void setFilename(NSOFString filename) {
		if (filename == null)
			setFilename((String) null);
		else
			setFilename(filename.getValue());
	}
}
