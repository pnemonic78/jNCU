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
 * This command is like a combination of <tt>kDSetCurrentSoup</tt> and
 * <tt>kDGetChangedInfo</tt>. It sets the current soup -- see
 * <tt>kDSetCurrentSoup</tt> for details. A <tt>kDSoupInfo</tt> or
 * <tt>kDRes</tt> command is sent by the Newton in response.
 * 
 * <pre>
 * 'ssgi'
 * length
 * soup name
 * </pre>
 * 
 * @author moshew
 */
public class DSetSoupGetInfo extends DockCommandToNewton {

	/** <tt>kDSetSoupGetInfo</tt> */
	public static final String COMMAND = "ssgi";

	private String name;

	/**
	 * Creates a new command.
	 */
	public DSetSoupGetInfo() {
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
