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

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command is sent by the desktop in response to the
 * <tt>kDResolveAlias</tt> command. If the value is {@code 0}, the alias can't
 * be resolved. If the data is {@code 1} (or non-zero) the alias can be
 * resolved.
 * 
 * <pre>
 * 'alir'
 * length
 * resolved
 * </pre>
 * 
 * @author moshew
 */
public class DAliasResolved extends DockCommandToNewtonLong {

	/** <tt>kDAliasResolved</tt> */
	public static final String COMMAND = "alir";

	/**
	 * Creates a new command.
	 */
	public DAliasResolved() {
		super(COMMAND);
	}

	/**
	 * Is alias resolved?
	 * 
	 * @return true if resolved?
	 */
	public boolean isResolved() {
		return getValue() != FALSE;
	}

	/**
	 * Set alias resolved.
	 * 
	 * @param resolved
	 *            resolved?
	 */
	public void setResolved(boolean resolved) {
		setValue(resolved ? TRUE : FALSE);
	}
}
