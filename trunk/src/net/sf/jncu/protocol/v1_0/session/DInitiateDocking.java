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
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDInitiateDocking</tt><br>
 * This command is sent to the newt in response to a <tt>kDRequestToDock</tt>
 * command. Session type can be one of
 * <tt>{none, settingUp, synchronize, restore, loadPackage, testComm, loadPatch, updatingStores}</tt>
 * 
 * <pre>
 * 'dock'
 * length
 * session type
 * </pre>
 */
public class DInitiateDocking extends DockCommandToNewton {

	public static final String COMMAND = "dock";

	public static final int SESSION_NONE = 0;
	public static final int SESSION_SETTING_UP = 1;
	public static final int SESSION_SYNCHRONIZE = 2;
	public static final int SESSION_RESTORE = 3;
	public static final int SESSION_LOAD_PACKAGE = 4;
	public static final int SESSION_TEST_COMM = 5;
	public static final int SESSION_LOAD_PATCH = 6;
	public static final int SESSION_UPDATING_STORES = 7;

	private int session = SESSION_NONE;

	/**
	 * Creates a new command.
	 */
	public DInitiateDocking() {
		super(COMMAND);
	}

	/**
	 * Get the session type.
	 * 
	 * @return the session.
	 */
	public int getSession() {
		return session;
	}

	/**
	 * Set the session type.
	 * 
	 * @param session
	 *            the session.
	 */
	public void setSession(int session) {
		this.session = session;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		ntohl(session, data);
	}
}
