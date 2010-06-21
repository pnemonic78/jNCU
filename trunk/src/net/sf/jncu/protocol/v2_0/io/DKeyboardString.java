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
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * This command sends a string of characters to the Newton for processing. The
 * characters are 2 byte Unicode characters. If there are an odd number of
 * characters the command should be padded, as usual.
 * 
 * <pre>
 * 'kbds'
 * length
 * "string"
 * </pre>
 * 
 * @author moshew
 */
public class DKeyboardString extends DockCommandToNewton {

	/** <tt>kDKeyboardString</tt> */
	public static final String COMMAND = "kbds";

	private String s;

	/**
	 * Creates a new command.
	 */
	public DKeyboardString() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		writeString(getString(), data);
	}

	/**
	 * Get the string.
	 * 
	 * @return the character.
	 */
	public String getString() {
		return s;
	}

	/**
	 * Set the string.
	 * 
	 * @param s
	 *            the string.
	 */
	public void setString(String s) {
		this.s = s;
	}

}
