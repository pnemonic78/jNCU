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
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * Desktop is in control while connected.
 * 
 * <pre>
 * 'dsnc' 
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DDesktopInControl extends DockCommandToNewtonBlank {

	/** <tt>kDDesktopInControl</tt> */
	public static final String COMMAND = "dsnc";

	/**
	 * Creates a new command.
	 */
	public DDesktopInControl() {
		super(COMMAND);
	}
}
