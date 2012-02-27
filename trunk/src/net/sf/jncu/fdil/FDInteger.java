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
 * Integer objects are just that: objects containing integral values. The
 * integers are stored in a 30-bit field, allowing them a range of
 * <tt>-536,870,912...536,870,911</tt>.
 * 
 * @author moshew
 */
public class FDInteger extends FDObject {

	/**
	 * Default integer class.<br>
	 * <tt>kFD_SymInteger</tt>
	 */
	public static final CharSequence CLASS = "integer";

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

	private final int value;

	/**
	 * Creates a new integer.
	 * 
	 * @param value
	 *            the value.
	 */
	public FDInteger(int value) {
		super();
		this.value = value & 0x1FFFFFFF;
	}

	/**
	 * Get the value.<br>
	 * <tt>long FD_GetInt(FD_Handle obj)</tt>
	 * 
	 * @return the value.
	 */
	public int getInt() {
		return value;
	}
}
