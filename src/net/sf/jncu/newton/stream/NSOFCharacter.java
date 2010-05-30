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
package net.sf.jncu.newton.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Character.
 * 
 * @author Moshe
 */
public class NSOFCharacter extends NSOFObject {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("character");

	protected static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private char value;

	/**
	 * Constructs a new character.
	 */
	public NSOFCharacter() {
		super();
		setNSClass(NS_CLASS);
	}

	/**
	 * Constructs a new character.
	 * 
	 * @param value
	 *            the character.
	 */
	public NSOFCharacter(char value) {
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
		setValue((char) 0);
		// Character code (byte)
		int c = in.read();
		if (c == -1) {
			throw new EOFException();
		}
		setValue((char) (c & 0xFF));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(CHARACTER);
		// Character code (byte)
		out.write(getValue() & 0xFF);
	}

	/**
	 * Get the value.
	 * 
	 * @return the value
	 */
	public char getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(char value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NSOFCharacter) {
			return this.getValue() == ((NSOFCharacter) obj).getValue();
		}
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		int value = getValue();
		return "$\\" + HEX[value & 0x000F] + HEX[(value >> 4) & 0x000F];
	}
}
