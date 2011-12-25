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
 * This command is sent when the desktop wants to retrieve an entry from the
 * current soup.
 * 
 * <pre>
 * 'rete'
 * length
 * id
 * </pre>
 * 
 * @author moshew
 */
public class DReturnEntry extends DockCommandToNewton {

	/** <tt>kDReturnEntry</tt> */
	public static final String COMMAND = "rete";

	private int id;

	/**
	 * Creates a new command.
	 */
	public DReturnEntry() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		htonl(getId(), data);
	}

	/**
	 * Get the entry id.
	 * 
	 * @return the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the entry id.
	 * 
	 * @param id
	 *            the id.
	 */
	public void setId(int id) {
		this.id = id;
	}
}
