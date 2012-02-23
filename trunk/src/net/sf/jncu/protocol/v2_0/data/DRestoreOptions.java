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

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * This command is sent to the Newton to specify which applications and packages
 * can be restored. It is sent in response to a <tt>kDRestoreFile</tt> command
 * from the Newton. If the user elects to do a selective restore the Newton
 * returns a similar command to the desktop indicating what should be restored.
 * <p>
 * Example: <tt>restoreWhich</tt> = <code>{storeType: kRestoreToNewton,
 * 	packages: ["pkg1", ...],
 * 	applications: ["app1", ...]}</code> <br>
 * <tt>storeType</tt> slot indicates whether the data will be restored to a card
 * (<tt>kRestoreToCard = 1</tt>) or the Newton ( <tt>kRestoreToNewton = 0</tt>).
 * 
 * <pre>
 * 'ropt'
 * length
 * restoreWhich
 * </pre>
 * 
 * @author moshew
 */
public class DRestoreOptions extends DockCommandToNewtonScript<NSOFFrame> {

	/** <tt>kDRestoreOptions</tt> */
	public static final String COMMAND = "ropt";

	/**
	 * Creates a new command.
	 */
	public DRestoreOptions() {
		super(COMMAND);
	}

}
