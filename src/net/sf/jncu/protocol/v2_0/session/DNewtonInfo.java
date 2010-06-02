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

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * This command is used to negotiate the real protocol version. See
 * <tt>kDDesktopInfo</tt> for more info. The password key is used as part of the
 * password verification.
 * 
 * <pre>
 * 'ninf'
 * length
 * protocol version
 * encrypted key
 * </pre>
 */
public class DNewtonInfo extends DockCommandFromNewton {

	/** <tt>kDNewtonInfo</tt> */
	public static final String COMMAND = "ninf";

	private int protocolVersion;
	private long encryptedKey;

	/**
	 * Creates a new command.
	 */
	public DNewtonInfo() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		setProtocolVersion(ntohl(data));
		long keyHi = ntohl(data) & 0xFFFFFFFFL;
		long keyLo = ntohl(data) & 0xFFFFFFFFL;
		setEncryptedKey((keyHi << 32) | keyLo);
	}

	/**
	 * Get the protocol version.
	 * 
	 * @return the protocol version.
	 */
	public int getProtocolVersion() {
		return protocolVersion;
	}

	/**
	 * Set the protocol version.
	 * 
	 * @param protocolVersion
	 *            the protocol version.
	 */
	protected void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	/**
	 * Get the encrypted key.
	 * 
	 * @return the encrypted key.
	 */
	public long getEncryptedKey() {
		return encryptedKey;
	}

	/**
	 * Set the encrypted key.
	 * 
	 * @param encryptedKey
	 *            the encrypted key.
	 */
	protected void setEncryptedKey(long encryptedKey) {
		this.encryptedKey = encryptedKey;
	}

}
