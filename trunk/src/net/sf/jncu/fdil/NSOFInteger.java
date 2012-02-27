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
 * Newton Streamed Object Format - Integer.
 * <p>
 * Integer objects are just that: objects containing integral values. The
 * integers are stored in a 30-bit field, allowing them a range of
 * <tt>-536,870,912...536,870,911</tt>.
 * 
 * @author moshew
 */
public class NSOFInteger extends NSOFImmediate {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("integer");

	/**
	 * A constant holding the minimum value an <code>integer</code> can have,
	 * -2<sup>29</sup>.
	 */
	public static final int MIN_VALUE = -536870912;
	/**
	 * A constant holding the maximum value an <code>integer</code> can have,
	 * 2<sup>29</sup>-1.
	 */
	public static final int MAX_VALUE = 536870911;

	/**
	 * Creates a new integer with value {@code 0}.
	 */
	public NSOFInteger() {
		this(0);
	}

	/**
	 * Creates a new integer.
	 * 
	 * @param value
	 *            the integer value.
	 */
	public NSOFInteger(int value) {
		super(value);
		setNSClass(NS_CLASS);
		setType(IMMEDIATE_INTEGER);
	}

	@Override
	public int getValue() {
		return super.getValue();
	}

	@Override
	public void setValue(int value) {
		if (value < MIN_VALUE)
			throw new IndexOutOfBoundsException();
		if (value > MAX_VALUE)
			throw new IndexOutOfBoundsException();
		super.setValue(value);
	}
}