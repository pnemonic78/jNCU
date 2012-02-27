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
 * There are two Boolean objects: the true object, and the false object. The nil
 * object <tt>kFD_NIL</tt> is not the same as the false object.
 * 
 * @author moshew
 */
public class FDBoolean extends FDImmediate {

	/**
	 * Default boolean class.<br>
	 * <tt>kFD_SymBoolean</tt>
	 */
	public static final CharSequence CLASS = "boolean";

	/**
	 * The true object.<br>
	 * <tt>kFD_True</tt>
	 */
	public static final FDBoolean TRUE = new FDBoolean(true);
	/**
	 * The false object.<br>
	 * <tt>kFD_False</tt>
	 */
	public static final FDBoolean FALSE = new FDBoolean(false);

	private final boolean value;

	/**
	 * Creates a new boolean.
	 * 
	 * @param value
	 *            the value.
	 */
	private FDBoolean(boolean value) {
		super();
		this.value = value;
	}

	/**
	 * Is the value true?
	 * 
	 * @return the value.
	 */
	public boolean isValue() {
		return value;
	}
}
