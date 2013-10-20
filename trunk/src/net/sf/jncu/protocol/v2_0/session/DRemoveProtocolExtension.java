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

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command removes a previously installed protocol extension.
 * 
 * <pre>
 * 'prex'
 * length
 * command
 * </pre>
 * 
 * @author moshew
 */
public class DRemoveProtocolExtension extends DockCommandToNewtonLong {

	/** <tt>kDRemoveProtocolExtension</tt> */
	public static final String COMMAND = "rpex";

	/**
	 * Creates a new command.
	 */
	public DRemoveProtocolExtension() {
		super(COMMAND);
	}

	/**
	 * Get the extension id.
	 * 
	 * @return the command.
	 */
	public int getExtension() {
		return getValue();
	}

	/**
	 * Set the extension id.
	 * 
	 * @param extension
	 *            the command.
	 */
	public void setExtension(int extension) {
		setValue(extension);
	}
}
