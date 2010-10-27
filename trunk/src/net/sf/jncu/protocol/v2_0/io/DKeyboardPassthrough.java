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

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewtonBlank;
import net.sf.jncu.protocol.IDockCommandFromNewton;

/**
 * This command is sent to enter keyboard pass-through mode. It can be followed
 * by <tt>kDKeyboardChar</tt>, <tt>kDKeyboardString</tt>, <tt>kDHello</tt> and
 * <tt>kDOperationDone</tt> commands.
 * 
 * <pre>
 * 'kybd'
 * length
 * </pre>
 * 
 * @author moshew
 */
public class DKeyboardPassthrough extends DockCommandToNewtonBlank implements IDockCommandFromNewton {

	/** <tt>kDStartKeyboardPassthrough</tt> */
	public static final String COMMAND = "kybd";

	/**
	 * Creates a new command.
	 */
	public DKeyboardPassthrough() {
		super(COMMAND);
	}

	@Override
	public void decode(InputStream data) throws IOException {
		// nothing to decode.
		setLength(DockCommandFromNewton.ntohl(data));
	}

}
