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
 * Newton Streamed Object Format - Character.
 * 
 * @author Moshe
 */
public class NSOFAsciiCharacter extends NSOFCharacter {

	/**
	 * Constructs a new character.<br>
	 * <em>Reserved for use by decoder!</em>
	 */
	public NSOFAsciiCharacter() {
		super();
	}

	/**
	 * Constructs a new character.
	 * 
	 * @param value
	 *            the character.
	 */
	public NSOFAsciiCharacter(byte value) {
		super((char) (value & 0xFF));
	}

	/**
	 * Constructs a new character.
	 * 
	 * @param value
	 *            the character.
	 */
	public NSOFAsciiCharacter(char value) {
		super(value);
	}

	@Override
	public String toString() {
		final int value = getValue();
		return "$\\" + HEX[value & 0x000F] + HEX[(value >> 4) & 0x000F];
	}

	@Override
	public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
		// Character code (byte)
		int c = in.read();
		if (c == -1) {
			throw new EOFException();
		}
		setValue((char) (c & 0xFF));
	}

	@Override
	public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(NSOF_CHARACTER);
		// Character code (byte)
		out.write(getValue() & 0xFF);
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		return new NSOFAsciiCharacter(this.getChar());
	}
}
