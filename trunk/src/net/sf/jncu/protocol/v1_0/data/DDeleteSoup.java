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
 * This command is used by restore to delete a soup if it exists on the Newton
 * but not on the desktop.
 * 
 * <pre>
 * 'dsou'
 * length
 * soup name	// C string
 * </pre>
 * 
 * @author moshew
 */
public class DDeleteSoup extends DockCommandToNewton {

	/** <tt>kDDeleteSoup</tt> */
	public static final String COMMAND = "dsou";

	private String name;

	/**
	 * Creates a new command.
	 */
	public DDeleteSoup() {
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
