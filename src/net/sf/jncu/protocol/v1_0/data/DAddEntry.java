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
import java.io.OutputStream;

import net.sf.jncu.newton.stream.NSOFBinaryObject;
import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDAddEntry</tt><br>
 * This command is sent when the desktop wants to add an entry to the current
 * soup.
 * 
 * <pre>
 * 'adde'
 * length
 * entry ref
 * </pre>
 * 
 * @author moshew
 */
public class DAddEntry extends DockCommandToNewton {

	public static final String COMMAND = "adde";

	private NSOFBinaryObject entry;

	/**
	 * Creates a new command.
	 */
	public DAddEntry(String cmd) {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(getEntry(), data);
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
	public void setEntry(NSOFBinaryObject entry) {
		this.entry = entry;
	}

}
