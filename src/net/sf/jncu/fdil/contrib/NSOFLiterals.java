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
package net.sf.jncu.fdil.contrib;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Newton Streamed Object Format - Literals.
 * 
 * @author Moshe
 */
public class NSOFLiterals extends NSOFArray {

	/** Default literals class. */
	public static final NSOFSymbol CLASS_LITERALS = new NSOFSymbol("literals");

	/**
	 * Constructs a new literals array.
	 */
	public NSOFLiterals() {
		super();
		setObjectClass(CLASS_LITERALS);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		NSOFLiterals copy = new NSOFLiterals();
		copy.setValue(this.getValue());
		return copy;
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		NSOFLiterals copy = new NSOFLiterals();
		int length = this.getLength();
		for (int i = 0; i < length; i++)
			copy.add(this.get(i).deepClone());
		return copy;
	}
}
