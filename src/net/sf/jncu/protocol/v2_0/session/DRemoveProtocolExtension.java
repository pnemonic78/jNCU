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

import net.sf.jncu.protocol.BaseDockCommandToNewton;

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
public class DRemoveProtocolExtension extends BaseDockCommandToNewton {

	/** <tt>kDRemoveProtocolExtension</tt> */
	public static final String COMMAND = "rpex";

	private String extension;

	/**
	 * Creates a new command.
	 */
	public DRemoveProtocolExtension() {
		super(COMMAND);
	}

	/**
	 * Get the extension command.
	 * 
	 * @return the command.
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Set the extension command.
	 * 
	 * @param extension
	 *            the command.
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		char[] cmdName = getExtension().toCharArray();
		data.write(cmdName[0] & 0xFF);
		data.write(cmdName[1] & 0xFF);
		data.write(cmdName[2] & 0xFF);
		data.write(cmdName[3] & 0xFF);
	}
}
