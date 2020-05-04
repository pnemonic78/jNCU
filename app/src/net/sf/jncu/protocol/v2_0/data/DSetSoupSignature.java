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
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This commands sets the signature of the current soup to the specified value.
 * A <tt>kDResult</tt> with value {@code 0} (or the error value if an error
 * occurred) is sent to the desktop in response.
 * 
 * <pre>
 * 'ssos'
 * length
 * new signature
 * </pre>
 * 
 * @author moshew
 */
public class DSetSoupSignature extends DockCommandToNewtonLong {

	/** <tt>kDSetSoupSignature</tt> */
	public static final String COMMAND = "ssos";

	/**
	 * Creates a new command.
	 */
	public DSetSoupSignature() {
		super(COMMAND);
	}

	/**
	 * Get the soup signature.
	 * 
	 * @return the signature.
	 */
	public int getSignature() {
		return getValue();
	}

	/**
	 * Set the soup signature.
	 * 
	 * @param signature
	 *            the signature.
	 */
	public void setSignature(int signature) {
		setValue(signature);
	}
}
