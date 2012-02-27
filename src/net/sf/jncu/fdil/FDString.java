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

import net.sf.lang.ControlCharacter;

/**
 * An FDIL string object.
 * 
 * @author moshew
 */
public class FDString extends FDBinaryObject {

	/**
	 * Default string class.<br>
	 * <tt>kFD_SymString</tt>
	 */
	public static final CharSequence CLASS = "string";

	/** Character in place of the embedded ink for 16-bit strings. */
	protected static final char INK = 0xF700;
	/** Character in place of the embedded ink for 8-bit strings. */
	protected static final char INK8 = ControlCharacter.SUB;

	private final String value;

	/**
	 * Creates a new string.
	 * 
	 * @param value
	 *            the value to represent.
	 */
	public FDString(String value) {
		super();
		this.value = value;
	}

	/**
	 * Creates a new string.
	 * 
	 * @param value
	 *            the value to represent.
	 */
	public FDString(char[] value) {
		this(new String(value));
	}

	/**
	 * Creates a new string.
	 * 
	 * @param value
	 *            the value to represent.
	 */
	public FDString(byte[] value) {
		super(value);
		this.value = new String(value);
	}

	/**
	 * Is this a rich string?
	 * <p>
	 * You may receive a rich string from a Newton device. A rich string is a
	 * string with embedded ink data. You cannot create a rich string, nor
	 * interpret the data in the ink portion of a rich string. When translating
	 * rich strings, a {@code 0xF700} or {@code 0x1A} character is inserted in
	 * the place of the embedded ink, depending on whether you are extracting
	 * 16-bit or 8-bit characters.
	 * 
	 * @return true if rich string.
	 */
	public boolean isRich() {
		return (value.indexOf(INK) >= 0);
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return value.equals(obj);
	}
}
