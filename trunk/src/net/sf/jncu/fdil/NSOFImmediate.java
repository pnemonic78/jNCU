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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Immediate.
 * <p>
 * <p>
 * Other than characters, the only immediate objects that you need are the true
 * object and the nil object, which are specified by the <tt>kFD_True</tt> and
 * <tt>kFD_NIL</tt> constants.
 * <p>
 * 2 bits are used for the immediate type, leaving a maximum of 30 bits for the
 * immediate value.
 * 
 * @author Moshe
 */
public class NSOFImmediate extends NSOFObject {

	/**
	 * Default immediate class.<br>
	 * <tt>kFD_SymWeird_Immediate</tt>
	 */
	public static final NSOFSymbol CLASS_IMMEDIATE = new NSOFSymbol("immediate");

	/** Integer immediate. */
	public static final int IMMEDIATE_INTEGER = 0x0;
	/** Character immediate. */
	public static final int IMMEDIATE_CHARACTER = 0x6;
	/** TRUE immediate. */
	public static final int IMMEDIATE_TRUE = 0xA;
	/** NIL immediate. */
	public static final int IMMEDIATE_NIL = 0x2;
	/** Magic Pointer immediate. */
	public static final int IMMEDIATE_MAGIC_POINTER = 0x3;

	/**
	 * A special immediate.<br>
	 * <tt>kImmedSpecial</tt>
	 */
	public static final int FD_SPECIAL = 0x00;
	/**
	 * A character.<br>
	 * <tt>kImmedCharacter</tt>
	 */
	public static final int FD_CHARACTER = 0x01;
	/**
	 * A Boolean.<br>
	 * <tt>kImmedBoolean</tt>
	 */
	public static final int FD_BOOLEAN = 0x02;
	/**
	 * A reserved immediate.<br>
	 * <tt>kImmedReserved</tt>
	 */
	public static final int FD_RESERVED = 0x03;

	private int value;
	private int type = IMMEDIATE_INTEGER;

	/**
	 * Constructs a new immediate.
	 */
	public NSOFImmediate() {
		this(0);
	}

	/**
	 * Constructs a new immediate.
	 * 
	 * @param value
	 *            the value.
	 */
	public NSOFImmediate(int value) {
		super();
		setObjectClass(CLASS_IMMEDIATE);
		setValue(value);
	}

	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Already decoded.
	}

	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(NSOF_IMMEDIATE);

		// Immediate Ref (xlong)
		int val = getValue();
		int ref = val;
		switch (type) {
		case IMMEDIATE_CHARACTER:
			ref = (val << 4) | 0x6;
			break;
		case IMMEDIATE_INTEGER:
			ref = val << 2;
			break;
		case IMMEDIATE_MAGIC_POINTER:
			ref = (val << 2) | 0x3;
			break;
		case IMMEDIATE_NIL:
			ref = 0x2;
			break;
		case IMMEDIATE_TRUE:
			ref = 0x1A;
			break;
		}
		XLong.encode(ref, out);
	}

	/**
	 * Get the value.
	 * 
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return getValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NSOFImmediate) {
			NSOFImmediate that = (NSOFImmediate) obj;
			return (this.getType() == that.getType()) && (this.getValue() == that.getValue());
		}
		return super.equals(obj);
	}

	/**
	 * Get the type.
	 * 
	 * @return the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Set the type.
	 * 
	 * @param type
	 *            the type.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Decoder can test if the immediate is a character.
	 * 
	 * @param r
	 *            the Ref of an Immediate.
	 * @return true if a character.
	 */
	public static boolean isRefCharacter(int r) {
		return (r & 0xF) == 0x6;
	}

	/**
	 * Decoder can test if the immediate is an integer.
	 * 
	 * @param r
	 *            the Ref of an Immediate.
	 * @return true if an integer.
	 */
	public static boolean isRefInteger(int r) {
		return (r & 0x3) == 0x0;
	}

	/**
	 * Decoder can test if the immediate is an integer.
	 * 
	 * @param r
	 *            the Ref of an Immediate.
	 * @return true if an integer.
	 */
	public static boolean isRefMagicPointer(int r) {
		return (r & 0x3) == 0x3;
	}

	/**
	 * Decoder can test if the immediate is a NIL.
	 * 
	 * @param r
	 *            the Ref of an Immediate.
	 * @return true if NIL.
	 */
	public static boolean isRefNil(int r) {
		return r == 0x2;
	}

	/**
	 * Decoder can test if the immediate is TRUE.
	 * 
	 * @param r
	 *            the Ref of an Immediate.
	 * @return true if TRUE.
	 */
	public static boolean isRefTrue(int r) {
		return (r == 0x1A);
	}

	/**
	 * Decoder can test if the immediate is a character.
	 * 
	 * @return true if a character.
	 */
	public boolean isCharacter() {
		return type == IMMEDIATE_CHARACTER;
	}

	/**
	 * Decoder can test if the immediate is an integer.
	 * 
	 * @return true if an integer.
	 */
	public boolean isInteger() {
		return type == IMMEDIATE_INTEGER;
	}

	/**
	 * Decoder can test if the immediate is an integer.
	 * 
	 * @return true if an integer.
	 */
	public boolean isMagicPointer() {
		return type == IMMEDIATE_MAGIC_POINTER;
	}

	/**
	 * Decoder can test if the immediate is a NIL.
	 * 
	 * @return true if NIL.
	 */
	public boolean isNil() {
		return type == IMMEDIATE_NIL;
	}

	/**
	 * Decoder can test if the immediate is TRUE.
	 * 
	 * @return true if TRUE.
	 */
	public boolean isTrue() {
		return type == IMMEDIATE_TRUE;
	}

	@Override
	public String toString() {
		return String.valueOf(getValue());
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		NSOFImmediate copy = new NSOFImmediate(this.getValue());
		copy.setType(this.getType());
		return copy;
	}
}
