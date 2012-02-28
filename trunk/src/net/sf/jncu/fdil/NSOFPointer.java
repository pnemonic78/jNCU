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
 * FDIL pointer object.
 * <p>
 * <em>Pointers are only used internally by the FDIL. You should never
 * see such an object.</em>
 * <p>
 * A pointer object is an object whose <tt>FD_Handle</tt> contains a reference
 * to the data comprising the object.
 * 
 * @author moshew
 */
public abstract class NSOFPointer extends NSOFObject implements Precedent {

	/**
	 * Creates a new pointer.
	 */
	public NSOFPointer() {
		super();
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
