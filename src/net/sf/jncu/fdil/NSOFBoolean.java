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
 * 
 * @author moshew
 */
public class NSOFBoolean extends NSOFImmediate {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("boolean");

	/**
	 * Creates a new boolean.
	 */
	public NSOFBoolean() {
		super();
		setNSClass(NS_CLASS);
	}

	@Override
	public void setValue(int value) {
		super.setValue(value);
		setType((value == 0) ? IMMEDIATE_NIL : IMMEDIATE_TRUE);
	}

	@Override
	public String toString() {
		return (getValue() == 0) ? Boolean.FALSE.toString() : Boolean.TRUE.toString();
	}

	@Override
	public boolean isNil() {
		return getValue() == 0;
	}

	@Override
	public boolean isTrue() {
		return getValue() != 0;
	}
}
