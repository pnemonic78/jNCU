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
import java.io.OutputStream;

import net.sf.jncu.fdil.contrib.NSOFSoupName;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * This command creates a soup on the current store. It uses a registered
 * <tt>soupdef</tt> to create the soup meaning that the indexes, etc. are
 * determined by the ROM. If no <tt>soupdef</tt> exists for the specified soup
 * an error is returned. A <tt>kDResult</tt> is returned.
 * 
 * <pre>
 * 'cdsp'
 * length
 * soup name
 * </pre>
 * 
 * @author moshew
 */
public class DCreateDefaultSoup extends DockCommandToNewton {

	/** <tt>kDCreateDefaultSoup</tt> */
	public static final String COMMAND = "cdsp";

	private String name;

	/**
	 * Creates a new command.
	 */
	public DCreateDefaultSoup() {
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
		NSOFSoupName.encode(getName(), data);
	}

}
