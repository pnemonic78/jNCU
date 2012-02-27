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
 * FDIL object.
 * 
 * @author moshew
 */
public abstract class FDObject implements Cloneable {

	/**
	 * Creates a new object.
	 */
	public FDObject() {
		super();
	}

	/**
	 * Shallow copy.
	 * <p>
	 * The <tt>FD_Clone</tt> creates a duplicate of the FDIL object. If the
	 * object is an aggregate object, that is an array or frame,
	 * <tt>FD_Clone</tt> only copies the top level objects.
	 * 
	 * @return the clone.
	 */
	@Override
	public Object clone() {
		return this;
	}

	/**
	 * Deep copy.
	 * <p>
	 * The <tt>FD_DeepClone</tt> create duplicates of the FDIL object.
	 * <tt>FD_DeepClone</tt> also makes copies of any nested objects,
	 * recursively.
	 * 
	 * @return the clone.
	 */
	public FDObject deepClone() {
		return this;
	}

}
