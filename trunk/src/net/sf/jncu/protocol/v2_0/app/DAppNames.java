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

import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;

/**
 * <tt>kDAppNames</tt><br>
 * This command returns the names of the applications present on the Newton. It
 * also, optionally, returns the names of the soups associated with each
 * application. The array looks like this:
 * <code>[{name: "app name", soups: ["soup1", "soup2"]},<br/>
 * &nbsp;{name: "another app name", ...}, ...]</code>
 * <p>
 * Some built-in names are included. "System information" includes the system
 * and directory soups. If there are packages installed, a "Packages" item is
 * listed. If a card is present and has a backup there will be a "Card backup"
 * item. If there are soups that don't have an associated application (or whose
 * application I can't figure out) there's an "Other information" entry.
 * <p>
 * The soup names are optionally returned depending on the value received with
 * <tt>kDGetAppNames</tt>.
 * 
 * <pre>
 * 'appn'
 * length
 * result frame
 * </pre>
 * 
 * @author Moshe
 */
public class DAppNames extends DockCommandFromNewtonScript {

	public static final String COMMAND = "appn";

	/**
	 * Creates a new command.
	 */
	public DAppNames() {
		super(COMMAND);
	}

}
