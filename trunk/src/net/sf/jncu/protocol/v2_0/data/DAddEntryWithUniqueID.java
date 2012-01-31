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

import net.sf.jncu.newton.stream.NSOFFrame;
import net.sf.jncu.protocol.v1_0.data.DAddEntry;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * This command is sent when the desktop wants to add an entry to the current
 * soup. The entry is added with the ID specified in the data frame. If the id
 * already exists an error is returned.
 * <p>
 * <em>Warning! This function should only be used during a restore operation. In other situations there's no way of knowing whether the entrie's id is unique. If an entry is added with this command and the entry isn't unique an error is returned.</em>
 * 
 * <pre>
 * 'auni'
 * length
 * data ref
 * </pre>
 * 
 * @author moshew
 * @see DAddEntry
 */
public class DAddEntryWithUniqueID extends DockCommandToNewtonScript<NSOFFrame> {

	/** <tt>kDAddEntryWithUniqueID</tt> */
	public static final String COMMAND = "auni";

	/**
	 * Creates a new command.
	 */
	public DAddEntryWithUniqueID() {
		super(COMMAND);
	}
}
