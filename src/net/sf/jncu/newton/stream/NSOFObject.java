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
package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Newton Streamed Object Format - Object.
 * 
 * @author Moshe
 */
public abstract class NSOFObject extends NewtonStreamedObjectFormat {

	private NSOFSymbol nsClass;

	/**
	 * Constructs a new object.
	 */
	public NSOFObject() {
		super();
	}

	/**
	 * Set the NewtonScript object class.
	 * 
	 * @param nsClass
	 *            the object class.
	 */
	public void setClass(NSOFSymbol nsClass) {
		setNSClass(nsClass);
	}

	/**
	 * Set the NewtonScript object class.
	 * 
	 * @param nsClassName
	 *            the object class name.
	 */
	public void setClass(String nsClassName) {
		setNSClass(nsClassName);
	}

	/**
	 * Set the NewtonScript object class.
	 * 
	 * @param nsClass
	 *            the object class.
	 */
	public void setNSClass(NSOFSymbol nsClass) {
		this.nsClass = nsClass;
	}

	/**
	 * Set the NewtonScript object class.
	 * 
	 * @param nsClassName
	 *            the object class name.
	 */
	public void setNSClass(String nsClassName) {
		setNSClass(new NSOFSymbol(nsClassName));
	}

	/**
	 * Get the NewtonScript object class.
	 * 
	 * @return the object class.
	 */
	public NSOFSymbol getNSClass() {
		return nsClass;
	}

	/**
	 * Read into the whole array.
	 * 
	 * @param in
	 *            the input.
	 * @param b
	 *            the destination buffer.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readAll(InputStream in, byte[] b) throws IOException {
		int count = 0;
		while (count < b.length)
			count += in.read(b, count, b.length - count);
	}
}
