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
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - NIL.
 * <p>
 * There is only one special FDIL immediate object that you encounter, the nil
 * object. This object, which you can refer to with the constant
 * <tt>kFD_NIL</tt>, is used to signify the lack of information.
 * 
 * @author Moshe
 */
public class NSOFNil extends NSOFImmediate {

	/**
	 * Default nil class.
	 */
	public static final NSOFSymbol CLASS_NIL = new NSOFSymbol("NIL");

	/**
	 * The nil object.<br>
	 * <tt>kFD_NIL</tt>
	 */
	public static final NSOFNil NIL = new NSOFNil();

	/**
	 * Constructs a new Nil.
	 */
	public NSOFNil() {
		super();
		setObjectClass(CLASS_NIL);
		setType(IMMEDIATE_NIL);
	}

	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(NSOF_NIL);
	}

	@Override
	public String toString() {
		return "nil";
	}

	@Override
	public boolean isNil() {
		return true;
	}
}
