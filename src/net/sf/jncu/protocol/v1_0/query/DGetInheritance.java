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
package net.sf.jncu.protocol.v1_0.query;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * Get inheritance.
 * 
 * <pre>
 * 'ginh'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetInheritance extends DockCommandToNewtonBlank {

	/** <tt>kDGetInheritance</tt> */
	public static final String COMMAND = "ginh";

	/**
	 * Creates a new command.
	 */
	public DGetInheritance(String cmd) {
		super(COMMAND);
	}

}
