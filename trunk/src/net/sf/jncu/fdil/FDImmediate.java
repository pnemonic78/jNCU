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
 * Immediate object.
 * <p>
 * Other than characters, the only immediate objects that you need are the true
 * object and the nil object, which are specified by the <tt>kFD_True</tt> and
 * <tt>kFD_NIL</tt> constants.
 * 
 * @author moshew
 */
public abstract class FDImmediate extends FDObject {

	/**
	 * Default immediate class.<br>
	 * <tt>kFD_SymWeird_Immediate</tt>
	 */
	public static final CharSequence CLASS = "immediate";

	/**
	 * A special immediate.<br>
	 * <tt>kImmedSpecial</tt>
	 */
	public static final int SPECIAL = 0x00;
	/**
	 * A character.<br>
	 * <tt>kImmedCharacter</tt>
	 */
	public static final int CHARACTER = 0x01;
	/**
	 * A Boolean.<br>
	 * <tt>kImmedBoolean</tt>
	 */
	public static final int BOOLEAN = 0x02;
	/**
	 * A reserved immediate.<br>
	 * <tt>kImmedReserved</tt>
	 */
	public static final int RESERVED = 0x03;

	/**
	 * Creates a new immediate.
	 */
	public FDImmediate() {
		super();
	}

}
