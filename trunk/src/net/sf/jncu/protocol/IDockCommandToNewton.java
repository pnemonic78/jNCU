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
package net.sf.jncu.protocol;

import java.io.IOException;

/**
 * Docking command interface from Newton to desktop.
 * 
 * @author moshew
 */
public interface IDockCommandToNewton extends IDockCommand {

	/**
	 * Get the payload to send.
	 * 
	 * @return the payload.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public byte[] getPayload() throws IOException;

	// /** TODO for extra-long commands like DLoadPackage where we write files.
	// * Get the payload to send.
	// *
	// * @return the payload.
	// * @throws IOException
	// * if an I/O error occurs.
	// */
	// public InputStream getPayloadStream() throws IOException;
}