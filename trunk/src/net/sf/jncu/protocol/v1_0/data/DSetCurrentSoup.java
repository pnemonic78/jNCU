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

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * This command sets the current soup. Most of the other commands pertain to
 * this soup so this command must precede any command that uses the current
 * soup. If the soup doesn't exist a <tt>kDSoupNotFound</tt> error is returned
 * but the connection is left alive so the desktop can create the soup if
 * necessary. Soup names must be < 25 characters.
 * 
 * <pre>
 * 'ssou'
 * length
 * soup name
 * </pre>
 * 
 * @author moshew
 */
public class DSetCurrentSoup extends DockCommandToNewton {

	/** <tt>kDSetCurrentSoup</tt> */
	public static final String COMMAND = "ssou";

	/** Soup not found. */
	public static final int ERROR_NOT_FOUND = -28015;

	private String name;

	/**
	 * Creates a new command.
	 */
	public DSetCurrentSoup() {
		super(COMMAND);
	}

	/**
	 * Get the soup name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the soup name.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		writeString(getName(), data);
	}

}
