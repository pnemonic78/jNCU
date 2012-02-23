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

import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * This command tells the Newton to delete a package. It can be used during
 * selective restore or any other time.
 * 
 * <pre>
 * 'rmvp'
 * length
 * name ref
 * </pre>
 * 
 * @author Moshe
 */
public class DRemovePackage extends DockCommandToNewtonScript<NSOFString> {

	/** <tt>kDRemovePackage</tt> */
	public static final String COMMAND = "rmvp";

	/**
	 * Constructs a new command.
	 */
	public DRemovePackage() {
		super(COMMAND);
	}
}
