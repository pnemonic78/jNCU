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

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * This command terminates the sequence of commands sent in response to a
 * <tt>kDBackupSoup</tt> command.
 * 
 * <pre>
 * 'bsdn'
 * length
 * </pre>
 * 
 * @author moshew
 */
public class DBackupSoupDone extends DockCommandFromNewton {

	/** <tt>kDBackupSoupDone</tt> */
	public static final String COMMAND = "bsdn";

	/**
	 * Creates a new command.
	 */
	public DBackupSoupDone() {
		super(COMMAND);
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
	}

}
