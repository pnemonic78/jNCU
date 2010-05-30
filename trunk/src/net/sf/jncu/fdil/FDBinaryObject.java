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
 * A binary object consist of a series of raw bytes. You may store any data you
 * wish in a binary object. The object may also contain a class symbol
 * identifying the data.
 * <p>
 * Binary objects are limited to a size of 16 MB.
 * 
 * @author moshew
 */
public class FDBinaryObject extends FDPointer {

	private byte[] value;

	/**
	 * Creates a new binary object.
	 * 
	 * @param value
	 *            the value.
	 */
	public FDBinaryObject(byte[] value) {
		super();
		this.value = value;
	}

	/**
	 * Creates a new binary object.
	 */
	protected FDBinaryObject() {
		this(null);
	}

	/**
	 * Get the value.<br>
	 * <tt>void* FD_GetBinaryData(FD_Handle obj)</tt>
	 * 
	 * @return the value.
	 */
	public byte[] getData() {
		return value;
	}

	/**
	 * Returns the length of the array.<br>
	 * <tt>long FD_GetLength(FD_Handle obj)</tt>
	 * 
	 * @return the length.
	 */
	public int getLength() {
		return value.length;
	}

	/**
	 * Grow or shrink the data.<br>
	 * <tt>DIL_Error FD_SetLength(FD_Handle obj, long newSize)</tt>
	 * <p>
	 * You can change the size of a binary object with the <tt>FD_SetLength</tt>
	 * function. However, any pointers to a binary object’s contents are
	 * invalidated by calling <tt>FD_SetLength</tt>, since the data might have
	 * been moved.
	 * 
	 * @param length
	 *            the array length.
	 */
	public void setLength(int length) {
		byte[] value2 = new byte[length];
		System.arraycopy(value, 0, value2, 0, Math.min(value.length, length));
		this.value = value2;
	}
}
