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
package net.sf.jncu.protocol.v2_0.sync;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * This command is like <tt>kDGetSoupInfo</tt> except that it only returns the
 * soup info if it has been changed since the time set by the
 * <tt>kDLastSyncTime</tt> command. If the info hasn't changed a <tt>kDRes</tt>
 * with {@code 0} is returned.
 * 
 * <pre>
 * 'cinf'
 * length
 * </pre>
 * 
 * @author moshew
 */
public class DGetChangedInfo extends DockCommandToNewtonBlank {

	/** <tt>kDGetChangedInfo</tt> */
	public static final String COMMAND = "cinf";

	/**
	 * Creates a new command.
	 */
	public DGetChangedInfo() {
		super(COMMAND);
	}

}
