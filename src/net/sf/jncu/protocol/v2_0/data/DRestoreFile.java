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

import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;

/**
 * <tt>kDRestoreFile</tt><br>
 * This command asks the desktop to restore the file specified by the last path
 * command and the filename. If the selected item is at the Desktop level, a
 * frame <code>{name: "Business", whichVol:-1}</code> is sent. Otherwise, a
 * string is sent.
 * 
 * <pre>
 * 'rsfl'
 * length
 * filename
 * </pre>
 * 
 * @author moshew
 */
public class DRestoreFile extends DockCommandFromNewtonScript {

	public static final String COMMAND = "rsfl";

	/**
	 * Creates a new command.
	 */
	public DRestoreFile() {
		super(COMMAND);
	}

}
