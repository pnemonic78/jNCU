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

/**
 * Character objects are immediate objects which contain a 16 bit Unicode
 * character.
 * 
 * @author moshew
 */
public class FDCharacter extends FDImmediate {

	protected static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private final char value;

	/**
	 * Creates a new character.
	 * 
	 * @param value
	 *            the value.
	 */
	public FDCharacter(char value) {
		super();
		this.value = value;
	}

	/**
	 * Get the value.<br>
	 * <tt>char FD_GetChar(FD_Handle obj)</tt>
	 * 
	 * @return the value
	 */
	public char getChar() {
		return getWideChar();
	}

	/**
	 * Get the value.<br>
	 * <tt>DIL_WideChar FD_GetWideChar(FD_Handle obj)</tt>
	 * 
	 * @return the value
	 */
	public char getWideChar() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		char hex0 = HEX[value & 0x000F];
		char hex1 = HEX[(value >> 4) & 0x000F];
		char hex2 = HEX[(value >> 8) & 0x000F];
		char hex3 = HEX[(value >> 12) & 0x000F];
		return "$\\" + hex3 + hex2 + hex1 + hex0;
	}
}
