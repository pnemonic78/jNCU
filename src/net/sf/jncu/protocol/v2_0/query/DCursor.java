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
 * Cursor command.
 * 
 * @author moshew
 */
public abstract class DCursor extends DockCommandToNewton {

	private int cursorId;

	/**
	 * Creates a new cursor command.
	 * 
	 * @param cmd
	 *            the command.
	 */
	public DCursor(String cmd) {
		super(cmd);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		htonl(getCursorId(), data);
	}

	/**
	 * Get the cursor id.
	 * 
	 * @return the cursor id.
	 */
	public int getCursorId() {
		return cursorId;
	}

	/**
	 * Set the cursor id.
	 * 
	 * @param cursorId
	 *            the cursor id.
	 */
	public void setCursorId(int cursorId) {
		this.cursorId = cursorId;
	}

}
