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
 * <tt>kDGetInternalStore</tt><br>
 * This command requests the Newton to return info about the internal store. The
 * result is described with the <tt>KDInternalStore</tt> command.
 * 
 * <pre>
 * 'gist'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetInternalStore extends DockCommandToNewtonBlank {

	public static final String COMMAND = "gist";

	/**
	 * Creates a new command.
	 */
	public DGetInternalStore() {
		super(COMMAND);
	}

}
