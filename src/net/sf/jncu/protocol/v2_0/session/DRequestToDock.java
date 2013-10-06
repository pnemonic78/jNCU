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

/**
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
public class DRequestToDock extends net.sf.jncu.protocol.v1_0.session.DRequestToDock {

	/** <tt>kDRequestToDock</tt> */
	public static final String COMMAND = net.sf.jncu.protocol.v1_0.session.DRequestToDock.COMMAND;

	/**
	 * The protocol version.<br>
	 * <tt>kProtocolVersion</tt><br>
	 * <tt>kBaseProtocolVersion</tt>
	 */
	public static final int PROTOCOL_VERSION = 9;

	/**
	 * The Dante protocol version.<br>
	 * <tt>kDanteProtocolVersion</tt>
	 */
	public static final int DANTE_PROTOCOL_VERSION = 10;

	/**
	 * Creates a new command.
	 */
	public DRequestToDock() {
		super(COMMAND);
	}

	/**
	 * Creates a new command.
	 * 
	 * @param cmd
	 *            the command.
	 */
	protected DRequestToDock(String cmd) {
		super(cmd);
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
		int protocol = ntohl(data);
		setProtocol(protocol);
		if (protocol < PROTOCOL_VERSION) {
			throw new ProtocolException();
		}
	}

}
