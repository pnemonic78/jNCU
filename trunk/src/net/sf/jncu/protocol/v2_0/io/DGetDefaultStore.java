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
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * This command returns info about the default store. This info is the same as
 * the info returned by the <tt>kDGetStoreNames</tt> command (see
 * <tt>kDStoreNames</tt> for details). The default store is the one used by
 * LoadPackage.
 * <p>
 * Returns a <tt>kDDefaultStore</tt> command.
 * 
 * <pre>
 * 'gdfs'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetDefaultStore extends DockCommandToNewtonBlank {

	/** <tt>kDGetDefaultStore</tt> */
	public static final String COMMAND = "gdfs";

	/**
	 * Creates a new command.
	 */
	public DGetDefaultStore() {
		super(COMMAND);
	}

}
