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

import net.sf.jncu.protocol.v1_0.session.DTest;

/**
 * This command is first sent from the desktop to the Newton. The Newton
 * immediately echos the object back to the desktop. The object can be any
 * NewtonScript object (anything that can be sent through the object
 * read/write).
 * <p>
 * This command can also be sent with no ref attached. If the length is 0 the
 * command is echoed back to the desktop with no ref included.
 * 
 * <pre>
 * 'rtst'
 * length
 * object
 * </pre>
 * 
 * @author moshew
 */
public class DRefTest extends DTest {

	/** <tt>kDRefTest</tt> */
	public static final String COMMAND = "rtst";

	/**
	 * Creates a new command.
	 */
	public DRefTest() {
		super(COMMAND);
	}
}
