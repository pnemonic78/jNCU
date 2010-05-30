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
package net.sf.jncu.protocol.v2_0.app;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDLoadPackageFile</tt><br>
 * This command asks the desktop to load the package specified by the last path
 * command and the filename string. If the selected item is at the Desktop
 * level, a frame <code>{Name: "Business", whichVol: -1}</code> is sent.
 * Otherwise, a string is sent.
 * 
 * <pre>
 * 'lpfl'
 * length
 * filename
 * </pre>
 * 
 * @author Moshe
 */
public class DLoadPackageFile extends DockCommandFromNewton {

	public static final String COMMAND = "lpfl";

	/**
	 * Creates a new command.
	 */
	public DLoadPackageFile() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
