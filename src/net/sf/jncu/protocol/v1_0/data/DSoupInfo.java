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

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * <tt>kDSoupInfo</tt><br>
 * This command is used to send a soup info frame. When received the info for
 * the current soup is set to the specified frame.
 * 
 * <pre>
 * 'sinf'
 * length
 * soup info frame
 * </pre>
 */
public class DSoupInfo extends DockCommandFromNewtonBlank {

	public static final String COMMAND = "sinf";

	/**
	 * Creates a new command.
	 */
	public DSoupInfo() {
		super(COMMAND);
	}

}
