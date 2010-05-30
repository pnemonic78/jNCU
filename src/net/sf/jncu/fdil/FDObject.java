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

import java.io.InputStream;

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

	/**
	 * The <tt>FD_Flatten</tt> function converts any FDIL object, including
	 * aggregate objects such as frames and arrays, to a flat stream of bytes in
	 * Newton Stream Object Format (NSOF). You could, for instance, send the
	 * data to a Newton device over a CDIL pipe with the <tt>CD_Write</tt>
	 * function, or store it to disk.<br>
	 * <tt>DIL_Error FD_Flatten(FD_Handle obj, DIL_WriteProc writeFn, void* userData)</tt>
	 * 
	 * @return the NSOF bytes.
	 */
	public byte[] flatten() {
		return null;
	}

	/**
	 * The <tt>FD_UnFlatten</tt> function converts from an Newton Stream Object
	 * Format (NSOF) byte stream to an FDIL object.<br>
	 * <tt>FD_Handle FD_Unflatten(DIL_ReadProc readFn, void* userData)</tt>
	 * 
	 * @param stream
	 *            the NSOF bytes.
	 * @see #unflatten(byte[], int)
	 */
	public static FDObject unflatten(byte[] stream) {
		return unflatten(stream, 0);
	}

	/**
	 * The <tt>FD_UnFlatten</tt> function converts from an Newton Stream Object
	 * Format (NSOF) byte stream to an FDIL object.
	 * 
	 * @param stream
	 *            the NSOF bytes.
	 * @param offset
	 *            the array offset.
	 */
	public static FDObject unflatten(byte[] stream, int offset) {
		return null;
	}

	/**
	 * The <tt>FD_UnFlatten</tt> function converts from an Newton Stream Object
	 * Format (NSOF) byte stream to an FDIL object.<br>
	 * <tt>FD_Handle FD_Unflatten(DIL_ReadProc readFn, void* userData)</tt>
	 * 
	 * @param stream
	 *            the NSOF bytes.
	 */
	public static FDObject unflatten(InputStream stream) {
		return null;
	}

}
