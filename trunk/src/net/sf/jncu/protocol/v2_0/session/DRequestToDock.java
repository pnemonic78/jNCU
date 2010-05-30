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
import java.io.InputStream;
import java.net.ProtocolException;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDRequestToDock</tt><br>
 * This command is sent to the desktop after the connection is established using
 * AppleTalk, serial, etc. (when the user taps the "connect" button). The
 * protocol version is the version of the messaging protocol that's being used
 * and should always be set to the number 9 for the version of the protocol
 * defined here.
 * 
 * <pre>
 * 'rtdk'
 * length = 4
 * protocol version = 9
 * </pre>
 */
public class DRequestToDock extends DockCommandFromNewton {

	public static final String COMMAND = "rtdk";

	/** The protocol version. */
	public static final int kProtocolVersion = 9;

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
	protected void decodeData(InputStream data) throws IOException {
		int protocol = htonl(data);
		setProtocol(protocol);
		if (protocol != kProtocolVersion) {
			throw new ProtocolException();
		}
	}

}
