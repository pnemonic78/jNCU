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
 * 
 * @author Moshe
 */
public class NSOFNil extends NSOFImmediate {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("NIL");

	/** The nil object. */
	public static final NSOFNil FD_NIL = new NSOFNil();

	/**
	 * Constructs a new Nil.
	 */
	public NSOFNil() {
		super();
		setNSClass(NS_CLASS);
		setType(IMMEDIATE_NIL);
	}

	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(NIL);
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
