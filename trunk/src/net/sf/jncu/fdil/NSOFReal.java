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

import net.sf.jncu.util.NumberUtils;

/**
 * Newton Streamed Object Format - Real number.
 * <p>
 * Real numbers in NewtonScript are represented as IEEE 64-bit floating point
 * numbers, which are accurate to about 15 decimal digits. They are binary
 * objects of class <tt>'real</tt>.
 * 
 * @author moshew
 */
public class NSOFReal extends NSOFBinaryObject {

	/** Default real number class. */
	public static final NSOFSymbol CLASS_REAL = new NSOFSymbol("real");

	private double value;

	/**
	 * Creates a new real number.
	 */
	public NSOFReal() {
		super();
		setObjectClass(CLASS_REAL);
	}

	/**
	 * Creates a new real number.
	 * 
	 * @param value
	 *            the value.
	 */
	public NSOFReal(double value) {
		this();
		setReal(value);
	}

	/**
	 * Get the value.
	 * 
	 * @return the value
	 */
	public double getReal() {
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setReal(double value) {
		this.value = value;
		super.setValue(NumberUtils.toBytes(Double.doubleToRawLongBits(value)));
	}

	@Override
	public void setValue(byte[] value) {
		setReal(Double.longBitsToDouble(NumberUtils.toLong(value)));
		super.setValue(value);
	}

}
