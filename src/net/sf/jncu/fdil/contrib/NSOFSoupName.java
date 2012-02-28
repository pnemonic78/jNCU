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
package net.sf.jncu.fdil.contrib;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.SymbolTooLongException;

/**
 * Soup name.
 * <p>
 * A soup name must be shorter than 25 characters.
 * 
 * @author Moshe
 */
public class NSOFSoupName extends NSOFString {

	/**
	 * Constructs a new name.
	 */
	public NSOFSoupName() {
		super();
	}

	/**
	 * Constructs a new name.
	 * 
	 * @param value
	 *            the name.
	 */
	public NSOFSoupName(String value) {
		super(value);
	}

	@Override
	public void setValue(String value) {
		if ((value != null) && (value.length() >= 25))
			throw new SymbolTooLongException();
		super.setValue(value);
	}

	/**
	 * Encode a soup name without using an encoder.
	 * 
	 * @param name
	 *            the name.
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static void encode(String name, OutputStream out) throws IOException {
		try {
			byte[] buf = name.getBytes(CHARSET_UTF16);
			int len = name.length();
			// Bytes [0] and [1] are 0xFE and 0xFF
			if (buf.length >= 2)
				out.write(buf, 2, buf.length - 2);
			// Word align.
			switch (len & 3) {
			case 1:
				out.write(0);
			case 2:
				out.write(0);
			case 3:
				out.write(0);
				break;
			}
		} catch (UnsupportedEncodingException uee) {
			throw new IOException(uee);
		}
	}
}
