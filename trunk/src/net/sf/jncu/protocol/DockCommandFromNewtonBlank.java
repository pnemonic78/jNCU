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
package net.sf.jncu.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * Docking command from Newton with no result.
 * 
 * @author Moshe
 */
public abstract class DockCommandFromNewtonBlank extends DockCommandFromNewton {

	/**
	 * Constructs a new command.
	 * 
	 * @param cmd
	 *            the command.
	 */
	public DockCommandFromNewtonBlank(String cmd) {
		super(cmd);
	}

	@Override
	protected final void decodeCommandData(InputStream data) throws IOException {
	}

}
