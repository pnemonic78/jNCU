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
package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * This command sends all the entries associated with a package to the Newton in
 * a single array. Packages are made up of at least 2 entries: one for the
 * package info and one for each part in the package. All of these entries must
 * be restored at the same time to restore a working package. A
 * <tt>kDResult</tt> is returned after the package has been successfully
 * restored.
 * 
 * <pre>
 * 'rpkg'
 * length
 * package array
 * </pre>
 * 
 * @author moshew
 */
public class DRestorePackage extends DockCommandToNewtonScript {

	/** <tt>kDRestorePackage</tt> */
	public static final String COMMAND = "rpkg";

	/**
	 * Creates a new command.
	 */
	public DRestorePackage() {
		super(COMMAND);
	}

}
