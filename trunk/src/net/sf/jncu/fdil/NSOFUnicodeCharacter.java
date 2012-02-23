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
package net.sf.jncu.fdil;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Unicode character.
 * 
 * @author Moshe
 */
public class NSOFUnicodeCharacter extends NSOFAsciiCharacter {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("uniChar");

	/**
	 * Constructs a new Unicode character.
	 */
	public NSOFUnicodeCharacter() {
		super();
		setNSClass(NS_CLASS);
	}

	/**
	 * Constructs a new Unicode character.
	 * 
	 * @param value
	 *            the character.
	 */
	public NSOFUnicodeCharacter(char value) {
		this();
		setValue(value);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		setValue('\u0000');
		// High byte of character code (byte)
		int hi = in.read();
		// Low byte of character code (byte)
		int lo = in.read();
		if ((hi == -1) || (lo == -1)) {
			throw new EOFException();
		}
		int c = ((hi & 0xFF) << 8) | ((lo & 0xFF) << 0);
		setValue((char) c);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(UNICODE_CHARACTER);
		int val = getValue() & 0xFFFF;
		// High byte of character code (byte)
		out.write((val >> 8) & 0xFF);
		// Low byte of character code (byte)
		out.write((val >> 0) & 0xFF);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		int value = getValue();
		char hex0 = HEX[value & 0x000F];
		value >>= 4;
		char hex1 = HEX[value & 0x000F];
		value >>= 4;
		char hex2 = HEX[value & 0x000F];
		value >>= 4;
		char hex3 = HEX[value & 0x000F];
		return "$\\u" + hex3 + hex2 + hex1 + hex0;
	}
}
