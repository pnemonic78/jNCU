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

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSetTimeout</tt><br>
 * This command sets the timeout for the connection (the time the Newton will
 * wait to receive data for it disconnects). This time is usually set to 30
 * seconds.
 * 
 * <pre>
 * 'stim' 
 * length
 * timeout in seconds
 * </pre>
 * 
 * @author moshew
 */
public class DSetTimeout extends DockCommandToNewton {

	public static final String COMMAND = "stim";

	private int timeout = 30;

	/**
	 * Creates a new command.
	 */
	public DSetTimeout() {
		super(COMMAND);
	}

	/**
	 * Get the timeout.
	 * 
	 * @return the timeout in seconds.
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Set the timeout.
	 * 
	 * @param timeout
	 *            the timeout in seconds.
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		htonl(getTimeout(), data);
	}

}