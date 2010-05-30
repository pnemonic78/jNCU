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
 * There is only one special immediate object that you encounter, the nil
 * object. This object, which you can refer to with the constant
 * <tt>kFD_NIL</tt>, is used to signify the lack of information.
 * 
 * @author moshew
 */
public class FDNil extends FDImmediate {

	/** The nil object. */
	public static final FDNil kFD_NIL = new FDNil();

	/**
	 * Creates a new nil.
	 */
	private FDNil() {
		super();
	}

}
