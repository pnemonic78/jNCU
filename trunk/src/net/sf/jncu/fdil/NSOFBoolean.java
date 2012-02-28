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
 * Newton Streamed Object Format - Boolean.
 * <p>
 * There are two Boolean objects: the true object, and the false object. The nil
 * object <tt>kFD_NIL</tt> is not the same as the false object.
 * 
 * @author moshew
 */
public class NSOFBoolean extends NSOFImmediate {

	/**
	 * Default boolean class.<br>
	 * <tt>kFD_SymBoolean</tt>
	 */
	public static final NSOFSymbol CLASS_BOOLEAN = new NSOFSymbol("boolean");

	/**
	 * The true object.<br>
	 * <tt>kFD_True</tt>
	 */
	public static final NSOFBoolean TRUE = new NSOFBoolean(true);
	/**
	 * The false object.<br>
	 * <tt>kFD_False</tt>
	 */
	public static final NSOFBoolean FALSE = new NSOFBoolean(false);

	/**
	 * Creates a new boolean.
	 * 
	 * @param value
	 *            the value.
	 */
	protected NSOFBoolean(boolean value) {
		super();
		setObjectClass(CLASS_BOOLEAN);
		setValue(value ? 0x1A : 0);
	}

	@Override
	protected void setValue(int value) {
		super.setValue(value);
		setType((value == 0) ? IMMEDIATE_NIL : IMMEDIATE_TRUE);
	}

	@Override
	public String toString() {
		return isTrue() ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
	}

	@Override
	public boolean isNil() {
		return getValue() == 0;
	}

	@Override
	public boolean isTrue() {
		return getValue() != 0;
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		return isTrue() ? TRUE : FALSE;
	}
}
