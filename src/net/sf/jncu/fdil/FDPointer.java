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
 * A pointer object is an object whose <tt>FD_Handle</tt> contains a reference
 * to the data comprising the object.
 * 
 * @author moshew
 */
public abstract class FDPointer extends FDObject {

	/**
	 * Creates a new pointer object.
	 */
	public FDPointer() {
		super();
	}

}
