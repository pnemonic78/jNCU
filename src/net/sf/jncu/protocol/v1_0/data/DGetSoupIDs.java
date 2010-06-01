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

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDGetSoupIDs</tt><br>
 * This command is sent to request a list of entry IDs for the current soup. It
 * expects to receive a <tt>kDSoupIDs</tt> command in response.
 * 
 * <pre>
 * 'gids'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetSoupIDs extends DockCommandToNewtonBlank {

	public static final String COMMAND = "gids";

	/**
	 * Creates a new command.
	 */
	public DGetSoupIDs(String cmd) {
		super(COMMAND);
	}

}