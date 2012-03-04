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
public class NSOFUnicodeCharacter extends NSOFCharacter {

	/** Default Unicode character class. */
	public static final NSOFSymbol CLASS_UNICODE = new NSOFSymbol("uniChar");

	/**
	 * Constructs a new Unicode character.
	 */
	public NSOFUnicodeCharacter() {
		super();
		setObjectClass(CLASS_UNICODE);
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

	/**
	 * Constructs a new Unicode character.
	 * 
	 * @param value
	 *            the code point.
	 */
	public NSOFUnicodeCharacter(int value) {
		this();
		setValue(value);
	}

	@Override
	public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
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

	@Override
	public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(NSOF_UNICODE_CHARACTER);
		int val = getValue() & 0xFFFF;
		// High byte of character code (byte)
		out.write((val >> 8) & 0xFF);
		// Low byte of character code (byte)
		out.write((val >> 0) & 0xFF);
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		return new NSOFUnicodeCharacter(this.getChar());
	}
}
