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

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * This command can optionally be sent at the end of synchronization. If it is
 * sent, the results are displayed on the Newton. The array looks like this: <br>
 * <code>[["store name", restored, "soup name", count, "soup name" count],<br>
 * &nbsp;["store name", restored, ...]]</code> <br>
 * Restored is true if the desktop detected that the Newton had been restore to
 * since the last sync. Count is the number of conflicting entries that were
 * found for each soup. Soups are only in the list if they had a conflict. When
 * a conflict is detected, the Newton version is saved and the desktop version
 * is moved to the archive file.
 * 
 * <pre>
 * 'sres'
 * length
 * results array
 * </pre>
 * 
 * @author moshew
 */
public class DSyncResults extends DockCommandToNewtonScript<NSOFArray> {

	/** <tt>kDSyncResults</tt> */
	public static final String COMMAND = "sres";

	/**
	 * Creates a new command.
	 */
	public DSyncResults() {
		super(COMMAND);
	}

}
