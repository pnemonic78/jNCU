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

import net.sf.jncu.fdil.contrib.NSOFSoupName;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * This command is used by restore to remove all entries from a soup before the
 * soup data is restored.
 * 
 * <pre>
 * 'dsou'
 * length
 * soup name	// C string
 * </pre>
 * 
 * @author moshew
 */
public class DEmptySoup extends DockCommandToNewton {

	/** <tt>kDEmptySoup</tt> */
	public static final String COMMAND = "esou";

	private String name;

	/**
	 * Creates a new command.
	 */
	public DEmptySoup() {
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

	/**
	 * Set the soup name.
	 * 
	 * @param soup
	 *            the soup.
	 */
	public void setSoup(Soup soup) {
		setName(soup.getName());
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		NSOFSoupName.flatten(getName(), data);
	}

}
