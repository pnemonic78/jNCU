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

/**
 * <tt>kDRequestToBrowse</tt><br>
 * This command is sent to a desktop that the Newton wishes to browse files on.
 * File types can be 'import, 'packages, 'syncFiles' or an array of strings to
 * use for filtering.
 * 
 * <pre>
 * 'rtbr'
 * length
 * file types
 * </pre>
 * 
 * @author moshew
 */
public class DRequestToBrowse extends DockCommandFromNewton {

	public static final String COMMAND = "rtbr";

	public DRequestToBrowse() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.protocol.DockCommandFromNewton#decodeData(java.io.InputStream
	 * )
	 */
	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO implement me!
	}

}
