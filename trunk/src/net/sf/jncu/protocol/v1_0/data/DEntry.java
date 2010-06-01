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

import net.sf.jncu.newton.stream.NSOFBinaryObject;
import net.sf.jncu.newton.stream.NSOFDecoder;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDEntry</tt><br>
 * This command is sent in response to a <tt>kDReturnEntry</tt> command. The
 * entry in the current soup specified by the ID in the <tt>kDReturnEntry</tt>
 * command is returned.
 * 
 * <pre>
 * 'entr'
 * length
 * entry  // binary data
 * </pre>
 */
public class DEntry extends DockCommandFromNewton {

	public static final String COMMAND = "entr";

	private NSOFBinaryObject entry;

	/**
	 * Creates a new command.
	 */
	public DEntry() {
		super(COMMAND);
	}

	/**
	 * Get the entry.
	 * 
	 * @return the entry.
	 */
	public NSOFObject getEntry() {
		return entry;
	}

	/**
	 * Set the entry.
	 * 
	 * @param entry
	 *            the entry.
	 */
	protected void setEntry(NSOFBinaryObject entry) {
		this.entry = entry;
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		setEntry((NSOFBinaryObject) decoder.decode(data));
	}
}
