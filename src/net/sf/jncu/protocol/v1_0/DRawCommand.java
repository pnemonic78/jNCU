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
package net.sf.jncu.protocol.v1_0;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

/**
 * Unknown command from Newton with raw data.
 * 
 * @author Moshe
 */
public class DRawCommand extends BaseDockCommandFromNewton {

	private byte[] data;

	public DRawCommand(String cmd) {
		super(cmd);
	}

	public DRawCommand(byte[] cmd) {
		super(cmd);
	}

	/**
	 * Get the data.
	 * 
	 * @return the data.
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Set the data.
	 * 
	 * @param data
	 *            the data.
	 */
	protected void setData(byte[] data) {
		this.data = data;
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
		final int length = getLength();
		byte[] raw = new byte[length];
		readAll(data, raw);
		setData(raw);
	}

	@Override
	public String toString() {
		return "Raw command: " + getCommand();
	}
}
