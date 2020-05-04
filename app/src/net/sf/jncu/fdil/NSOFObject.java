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
 * Newton Streamed Object Format - Object.
 * 
 * @author Moshe
 */
public abstract class NSOFObject extends NewtonStreamedObjectFormat implements Cloneable {

	private NSOFSymbol oClass;

	/**
	 * Constructs a new object.
	 */
	public NSOFObject() {
		super();
	}

	/**
	 * Set the NewtonScript object class.
	 * 
	 * @param oClass
	 *            the object class.
	 */
	protected void setObjectClass(NSOFSymbol oClass) {
		this.oClass = oClass;
	}

	/**
	 * Set the NewtonScript object class.
	 * 
	 * @param nsClassName
	 *            the object class name.
	 */
	protected void setObjectClass(String nsClassName) {
		setObjectClass(new NSOFSymbol(nsClassName));
	}

	/**
	 * Get the NewtonScript object class.
	 * 
	 * @return the object class.
	 */
	public NSOFSymbol getObjectClass() {
		return oClass;
	}

	/**
	 * Shallow copy.
	 * <p>
	 * Creates a duplicate of the FDIL object. If the object is an aggregate
	 * object, that is an array or frame, <tt>clone</tt> only copies the top
	 * level objects.
	 * 
	 * @return the clone. The default implementation returns a deep copy.
	 * @throws CloneNotSupportedException
	 *             if the object could not or should not be cloned.
	 * @see #deepClone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return deepClone();
	}

	/**
	 * Deep copy.
	 * <p>
	 * Create duplicates of the FDIL object. <tt>deepClone</tt> also makes
	 * copies of any nested objects, recursively.
	 * 
	 * @return the clone.
	 * @throws CloneNotSupportedException
	 *             if the object could not or should not be cloned.
	 */
	public NSOFObject deepClone() throws CloneNotSupportedException {
		return this;
	}

	@Override
	public String toString() {
		return getObjectClass().toString();
	}
}
