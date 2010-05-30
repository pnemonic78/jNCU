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
 * A magic pointer object contains a pointer to objects in a Newton devices ROM.
 * You should only need to create magic pointer objects if you are writing a
 * Newton development environment. The only likely way to run into a magic
 * pointer object in your code is reading an NTK stream file with the
 * <tt>FD_Unflatten</tt> function. You should never see a magic pointer object
 * from data sent from a Newton device, through a CDIL pipe. Magic pointers are
 * resolved before being sent from a Newton device.
 * 
 * @author moshew
 */
public class FDMagicPointer extends FDObject {

	private final int value;

	/**
	 * Creates a new magic pointer.
	 * 
	 * @param value
	 *            the value.
	 */
	public FDMagicPointer(int value) {
		super();
		this.value = value;
	}

	/**
	 * Get the value.
	 * 
	 * @return the value.
	 */
	public int getValue() {
		return value;
	}
}
