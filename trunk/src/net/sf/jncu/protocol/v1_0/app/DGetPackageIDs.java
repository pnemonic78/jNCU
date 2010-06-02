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
package net.sf.jncu.protocol.v1_0.app;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * This command is sent to request a list of package ids. This list is used to
 * remove any packages from the desktop that have been deleted on the Newton.
 * 
 * <pre>
 * 'gpid'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetPackageIDs extends DockCommandToNewtonBlank {

	/** <tt>kDGetPackageIDs</tt> */
	public static final String COMMAND = "gpid";

	/**
	 * Creates a new command.
	 */
	public DGetPackageIDs(String cmd) {
		super(COMMAND);
	}

}
