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
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * Ask desktop to start docking process.<br>
 * This command is sent to a docker that the junior wishes to connect with (on
 * the network, serial, etc.). The Newt expects a <tt>kDInitiateDocking</tt>
 * command in response. The protocol version is the version of the messaging
 * protocol that's being used.
 * 
 * <pre>
 * 'rtdk'
 * length
 * protocol version
 * </pre>
 */
public class DRequestToDock extends DockCommandFromNewton {

	/** <tt>kDRequestToDock</tt> */
	public static final String COMMAND = "rtdk";

	private int protocol;

	/**
	 * Creates a new command.
	 */
	public DRequestToDock() {
		super(COMMAND);
	}

	/**
	 * Get the protocol version.
	 * 
	 * @return the protocol version.
	 */
	public int getProtocol() {
		return protocol;
	}

	/**
	 * Set the protocol version.
	 * 
	 * @param protocol
	 *            the protocol version.
	 */
	protected void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
		int protocol = ntohl(data);
		setProtocol(protocol);
	}

}
