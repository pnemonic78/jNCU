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

import net.sf.jncu.protocol.NewtonInfo;

/**
 * This command is sent in response to a correct <tt>kDInitiateDocking</tt>
 * command from the docker. The Newton's name is used to locate the proper
 * synchronise file. The version info includes things like machine type (e.g.
 * J1), ROM version, etc. Here's the full list of what the version info includes
 * (all are <code>long</code>s):
 * <ol>
 * <li>length of version info in bytes
 * <li>NewtonUniqueID - a number uniquely identifying the Newton
 * <li>manufacturer id
 * <li>machine type
 * <li>ROM version
 * <li>ROM stage
 * <li>RAM size
 * <li>screen height
 * <li>screen width
 * <li>system update version
 * <li>Newton object system version
 * <li>signature of internal store
 * <li>vertical screen resolution
 * <li>horizontal screen resolution
 * <li>screen depth
 * </ol>
 * The version info is followed by the name of the Newton sent as a Unicode
 * string including the terminating zeros at the end. The string is padded to an
 * even 4 bytes by adding zeros as necessary (the padding bytes are not included
 * in the length sent as part of the command header).
 * 
 * <pre>
 * 'name'
 * length
 * version info
 * name
 * </pre>
 */
public class DNewtonName extends net.sf.jncu.protocol.v1_0.session.DNewtonName {

	/** <tt>kDNewtonName</tt> */
	public static final String COMMAND = net.sf.jncu.protocol.v1_0.session.DNewtonName.COMMAND;

	/**
	 * Creates a new command.
	 */
	public DNewtonName() {
		super();
	}

	@Override
	protected NewtonInfo decodeInfo(InputStream data) throws IOException {
		NewtonInfo info = super.decodeInfo(data);

		final int versionSize = 14 * LENGTH_WORD;
		if (versionInfoLength > versionSize) {
			/**
			 * A bit field. The following two bits are defined:<br>
			 * 1 = has serial number <br>
			 * 2 = has target protocol
			 */
			int systemFlags = ntohl(data);

			if ((systemFlags & 0x1) == 0x1) {
				long serHi = ntohl(data) & 0xFFFFFFFFL;
				long serLo = ntohl(data) & 0xFFFFFFFFL;
				info.setSerialNumber((serHi << 32) | serLo);
			}
			if ((systemFlags & 0x2) == 0x2) {
				info.setTargetProtocol(ntohl(data));
			}
		}

		return info;
	}
}
