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

import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;

/**
 * <tt>kDSyncOptions</tt><br>
 * This command is sent whenever the user on the Newton has selected selective
 * sync. The frame sent completely specifies which information is to be
 * synchronised.<br>
 * <code>
 * {<br>
 * &nbsp;&nbsp;packages: TRUEREF,<br>
 * &nbsp;&nbsp;syncAll: TRUEREF,<br>
 * &nbsp;&nbsp;stores: [{store info}, {store info}]<br>
 * }</code><br>
 * Each store frame in the stores array contains the same information returned
 * by the kDStoreNames command with the addition of soup information. It looks
 * like this: <br>
 * <code>{<br>
 * &nbsp;&nbsp;name: "",<br>
 * &nbsp;&nbsp;signature: 1234,<br>
 * &nbsp;&nbsp;totalsize: 1234,<br>
 * &nbsp;&nbsp;usedsize: 1234,<br>
 * &nbsp;&nbsp;kind: "",<br>
 * &nbsp;&nbsp;soups: [soup names],<br>
 * &nbsp;&nbsp;signatures: [soup signatures]<br>
 * &nbsp;&nbsp;info: {store info frame},<br>
 * }</code><br>
 * If the user has specified to sync all information the frame will look the
 * same except there won't be a soups slot--all soups are assumed.
 * <p>
 * Note that the user can specify which stores to sync while specifying that all
 * soups should be synchronised.
 * <p>
 * If the user specifies that packages should be synchronised the packages flag
 * will be true and the packages soup will be specified in the store frame(s).
 * 
 * <pre>
 * 'sopt'
 * length
 * frame of info
 * </pre>
 * 
 * @author moshew
 */
public class DSyncOptions extends DockCommandFromNewtonScript {

	public static final String COMMAND = "sopt";

	/**
	 * Creates a new command.
	 */
	public DSyncOptions() {
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
		// TODO Auto-generated method stub
	}

}
