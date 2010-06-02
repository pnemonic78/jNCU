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
package net.sf.jncu.protocol.v2_0.query;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * The parameter frame must contain a <tt>queryspec</tt> slot and may contain a
 * <tt>soupname</tt> slot. Performs the specified query on the current store.
 * The <tt>queryspec</tt> is a full <tt>queryspec</tt> including valid test,
 * etc. functions. Soup name is a string that's used to find a soup in the
 * current store to query. If the soup name is an empty string or a
 * <tt>NILREF</tt> the query is done on the current soup. A <tt>kDLongData</tt>
 * is returned with a cursor ID that should be used with the rest of the remote
 * query commands.
 * 
 * <pre>
 * 'qury'
 * length
 * parameter frame
 * </pre>
 * 
 * @author moshew
 */
public class DQuery extends DockCommandToNewton {

	/** <tt>kDQuery</tt> */
	public static final String COMMAND = "qury";

	/**
	 * Creates a new command.
	 */
	public DQuery() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.protocol.DockCommandToNewton#getCommandData()
	 */
	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
	}

}
