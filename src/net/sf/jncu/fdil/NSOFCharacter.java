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
 * Newton Streamed Object Format - Character.
 * 
 * @author Moshe
 */
public class NSOFCharacter extends NSOFImmediate {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("character");

	protected static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Constructs a new character.
	 */
	public NSOFCharacter() {
		this('\u0000');
	}

	/**
	 * Constructs a new character.
	 * 
	 * @param value
	 *            the character.
	 */
	public NSOFCharacter(char value) {
		super();
		setNSClass(NS_CLASS);
		setType(IMMEDIATE_CHARACTER);
		setValue(value);
	}

	/**
	 * Get the character.
	 * 
	 * @return the character.
	 */
	public char getChar() {
		return (char) getValue();
	}

	/**
	 * Set the character.
	 * 
	 * @param value
	 *            the character.
	 */
	public void setChar(char value) {
		setValue(value);
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
