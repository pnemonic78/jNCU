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

import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;

/**
 * <tt>kDEntry</tt><br>
 * This command is sent in response to a <tt>kDReturnEntry</tt> command. The
 * entry in the current soup specified by the ID in the <tt>kDReturnEntry</tt>
 * command is returned.
 * 
 * <pre>
 * 'entr'
 * length
 * entry  // binary data
 * </pre>
 */
public class DEntry extends DockCommandFromNewtonScript {

	public static final String COMMAND = "entr";

	/**
	 * Creates a new command.
	 */
	public DEntry() {
		super(COMMAND);
	}

}
