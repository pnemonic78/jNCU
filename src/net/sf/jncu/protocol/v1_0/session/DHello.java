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

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;
import net.sf.jncu.protocol.DockCommandToNewtonBlank;
import net.sf.jncu.protocol.IDockCommandToNewton;

/**
 * This command is sent during long operations to let the Newton or desktop know
 * that the connection hasn't been dropped.
 * 
 * <pre>
 * 'helo'
 * length = 0
 * </pre>
 */
public class DHello extends DockCommandFromNewtonBlank implements IDockCommandToNewton {

	/** <tt>kDHello</tt> */
	public static final String COMMAND = "helo";

	/**
	 * Creates a new command.
	 */
	public DHello() {
		super(COMMAND);
	}

	@Override
	public InputStream getCommandPayload() throws IOException {
		IDockCommandToNewton cmd = new DockCommandToNewtonBlank(COMMAND) {
		};
		return cmd.getCommandPayload();
	}

	@Override
	public int getCommandPayloadLength() throws IOException {
		return LENGTH_HEADER + getLength();
	}
}
