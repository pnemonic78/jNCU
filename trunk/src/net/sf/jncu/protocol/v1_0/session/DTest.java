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
package net.sf.jncu.protocol.v1_0.session;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * Test.
 * 
 * <pre>
 * 'test'
 * length
 * data
 * </pre>
 */
public class DTest extends DockCommandToNewtonScript implements IDockCommandFromNewton {

	/** <tt>kDTest</tt> */
	public static final String COMMAND = "test";

	/**
	 * Creates a new command.
	 */
	public DTest() {
		super(COMMAND);
	}

	@Override
	public void decode(InputStream data) throws IOException {
		DockCommandFromNewtonScript cmd = new DockCommandFromNewtonScript(COMMAND) {
		};
		cmd.decode(data);
		setLength(cmd.getLength());
		setObject(cmd.getResult());
	}
}
