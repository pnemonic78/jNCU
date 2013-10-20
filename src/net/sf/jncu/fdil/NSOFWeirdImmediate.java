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
 * Newton Streamed Object Format - Weird Immediate.
 * 
 * @author moshew
 */
public class NSOFWeirdImmediate extends NSOFImmediate {

	/**
	 * Default weird immediate class.<br>
	 * <tt>kFD_SymWeird_Immediate</tt>
	 */
	public static final NSOFSymbol CLASS_WEIRD_IMMEDIATE = new NSOFSymbol("weird_immediate");

	/**
	 * Creates a new weird immediate.
	 * 
	 * @param value
	 *            the value.
	 */
	public NSOFWeirdImmediate(int value) {
		super(value, IMMEDIATE_WEIRD);
		setObjectClass(CLASS_WEIRD_IMMEDIATE);
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		return new NSOFWeirdImmediate(this.getValue());
	}
}
