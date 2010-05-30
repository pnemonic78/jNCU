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

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDSoupIDs</tt><br>
 * This command is sent in response to a <tt>kDGetSoupIDs</tt> command. It
 * returns all the IDs from the current soup.
 * 
 * <pre>
 * 'sids'
 * length
 * count
 * array of ids for the soup
 * </pre>
 */
public class DSoupIDs extends DockCommandFromNewton {

	public static final String COMMAND = "sids";

	/**
	 * Creates a new command.
	 */
	public DSoupIDs() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
