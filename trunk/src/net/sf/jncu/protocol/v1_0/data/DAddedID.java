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
 * <tt>kDAddedID</tt><br>
 * This command is sent in response to a <tt>kDAddEntry</tt> command from the
 * desktop. It returns the ID that the entry was given when it was added to the
 * current soup.
 * 
 * <pre>
 * 'adid'
 * length
 * id
 * </pre>
 */
public class DAddedID extends DockCommandFromNewton {

	public static final String COMMAND = "adid";

	/**
	 * Creates a new command.
	 */
	public DAddedID() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
