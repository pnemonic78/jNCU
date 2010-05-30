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
package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSetStoreSignature</tt><br>
 * This commands sets the signature of the current store to the specified value.
 * A <tt>kDResult</tt> with value <tt>0</tt> (or the error value if an error
 * occurred) is sent to the desktop in response.
 * 
 * <pre>
 * 'ssig'
 * length
 * new signature
 * </pre>
 * 
 * @author moshew
 */
public class DSetStoreSignature extends DockCommandToNewton {

	public static final String COMMAND = "ssig";

	private int signature;

	/**
	 * Creates a new command.
	 */
	public DSetStoreSignature() {
		super(COMMAND);
	}

	/**
	 * Get the soup signature.
	 * 
	 * @return the signature.
	 */
	public int getSignature() {
		return signature;
	}

	/**
	 * Set the soup signature.
	 * 
	 * @param signature
	 *            the signature.
	 */
	public void setSignature(int signature) {
		this.signature = signature;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		htonl(getSignature(), data);
	}

}
