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

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.contrib.NSOFSoupName;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * Create a soup.
 * 
 * <pre>
 * 'csop'
 * length
 * soup name
 * index description
 * </pre>
 * 
 * @author moshew
 */
public class DCreateSoup extends DockCommandToNewton {

	/** <tt>kDCreateSoup</tt> */
	public static final String COMMAND = "csop";

	private Soup soup;

	/**
	 * Creates a new command.
	 */
	public DCreateSoup() {
		super(COMMAND);

	}

	/**
	 * Get the soup.
	 * 
	 * @return the soup.
	 */
	public Soup getSoup() {
		return soup;
	}

	/**
	 * Set the soup.
	 * 
	 * @param soup
	 *            the soup.
	 */
	public void setSoup(Soup soup) {
		this.soup = soup;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		NSOFSoupName.flatten(getSoup().getName(), data);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(getSoup().getIndexes(), data);
	}

}
