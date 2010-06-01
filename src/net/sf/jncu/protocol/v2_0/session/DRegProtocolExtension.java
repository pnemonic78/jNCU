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

import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDRegProtocolExtension</tt><br>
 * This command installs a protocol extension into the Newton. The extension
 * lasts for the length of the current connection (in other words, you have to
 * install the extension every time you connect). The function is a Newton
 * script closure that would have to be compiled on the desktop. See the Dante
 * Connection (ROM) API IU document for details. A <tt>kDResult</tt> with value
 * <tt>0</tt> (or the error value if an error occurred) is sent to the desktop
 * in response.
 * 
 * <pre>
 * 'pext'
 * length
 * command
 * function
 * </pre>
 * 
 * @author moshew
 */
public class DRegProtocolExtension extends DockCommandToNewton {

	public static final String COMMAND = "pext";

	private String extension;
	private NSOFObject function;

	/**
	 * Creates a new command.
	 */
	public DRegProtocolExtension() {
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

	/**
	 * Get the function.
	 * 
	 * @return the function.
	 */
	public NSOFObject getFunction() {
		return function;
	}

	/**
	 * Set the function.
	 * 
	 * @param function
	 *            the function.
	 */
	public void setFunction(NSOFObject function) {
		this.function = function;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		char[] cmdName = getExtension().toCharArray();
		data.write(cmdName[0] & 0xFF);
		data.write(cmdName[1] & 0xFF);
		data.write(cmdName[2] & 0xFF);
		data.write(cmdName[3] & 0xFF);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(getFunction(), data);
	}
}