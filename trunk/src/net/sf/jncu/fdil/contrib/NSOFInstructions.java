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

import net.sf.jncu.fdil.NSOFBinaryObject;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Newton Streamed Object Format - Instructions.
 * 
 * @author Moshe
 */
public class NSOFInstructions extends NSOFBinaryObject {

	/** Default instructions class. */
	public static final NSOFSymbol CLASS_INSTRUCTIONS = new NSOFSymbol("instructions");

	/**
	 * Constructs a new instructions.
	 */
	public NSOFInstructions() {
		super();
		setObjectClass(CLASS_INSTRUCTIONS);
	}

}
