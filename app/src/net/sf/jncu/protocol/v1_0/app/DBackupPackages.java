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
 * This command is sent to backup any packages that are installed on the Newton.
 * It expects a <tt>kDPackage</tt> command or a <tt>kDResponse</tt> with an error of
 * {@code 0} (to indicate that there are no more packages) in response.
 * 
 * <pre>
 * 'bpkg'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DBackupPackages extends DockCommandToNewtonBlank {

	/** <tt>kDBackupPackages</tt> */
	public static final String COMMAND = "bpkg";

	/**
	 * Creates a new command.
	 */
	public DBackupPackages() {
		super(COMMAND);
	}

}
