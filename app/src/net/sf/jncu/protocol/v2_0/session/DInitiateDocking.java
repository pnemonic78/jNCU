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
package net.sf.jncu.protocol.v2_0.session;

/**
 * Ask Newton to start docking process.<br>
 * This command should be sent to the Newton in response to a
 * <tt>kDRequestToDock</tt> command. Session type should be 4 to load a package.
 * 
 * <pre>
 * 'dock'
 * length = 4
 * session type
 * </pre>
 */
public class DInitiateDocking extends net.sf.jncu.protocol.v1_0.session.DInitiateDocking {

	/** <tt>kDInitiateDocking</tt> */
	public static final String COMMAND = net.sf.jncu.protocol.v1_0.session.DInitiateDocking.COMMAND;

	public static final int SESSION_NONE = net.sf.jncu.protocol.v1_0.session.DInitiateDocking.SESSION_NONE;
	public static final int SESSION_SETTING_UP = net.sf.jncu.protocol.v1_0.session.DInitiateDocking.SESSION_SETTING_UP;
	public static final int SESSION_SYNCHRONIZE = net.sf.jncu.protocol.v1_0.session.DInitiateDocking.SESSION_SYNCHRONIZE;
	public static final int SESSION_RESTORE = net.sf.jncu.protocol.v1_0.session.DInitiateDocking.SESSION_RESTORE;
	public static final int SESSION_LOAD_PACKAGE = net.sf.jncu.protocol.v1_0.session.DInitiateDocking.SESSION_LOAD_PACKAGE;
	public static final int SESSION_TEST_COMM = net.sf.jncu.protocol.v1_0.session.DInitiateDocking.SESSION_TEST_COMM;
	public static final int SESSION_LOAD_PATCH = net.sf.jncu.protocol.v1_0.session.DInitiateDocking.SESSION_LOAD_PATCH;
	public static final int SESSION_UPDATING_STORES = net.sf.jncu.protocol.v1_0.session.DInitiateDocking.SESSION_UPDATING_STORES;

	/**
	 * Creates a new command.
	 */
	public DInitiateDocking() {
		super();
	}

}
