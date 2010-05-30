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

import java.util.ArrayList;
import java.util.List;

/**
 * An array object is a variable-size object whose contents are divided into a
 * series of other objects. Each division is called a �slot�. Each slot consists
 * of an FDIL object. Objects can be inserted into an array or appended to the
 * end of an array.
 * <p>
 * The array�s slots are initialised to <tt>kFD_NIL</tt>.
 * <p>
 * Array objects are limited to a size of 16 MB.
 * 
 * @author moshew
 */
public class FDArray extends FDPointer {

	private final List<FDObject> slots;

	/**
	 * Creates a new array of size <tt>0</tt>.
	 */
	public FDArray() {
		this(0);
	}

	/**
	 * Creates a new array.<br>
	 * <tt>FD_Handle FD_MakeArray(long size, const char* cls)</tt>
	 * 
	 * @param size
	 *            the initial size, number of slots, of the array.
	 */
	public FDArray(int size) {
		super();
		this.slots = new ArrayList<FDObject>(size);
	}

	/**
	 * Returns the object in the given slot of the array.<br>
	 * <tt>FD_Handle FD_GetArraySlot(FD_Handle array, long pos)</tt>
	 * 
	 * @param pos
	 *            the slot position. Which array slot to access.
	 * @return the item in that array slot.
	 */
	public FDObject get(int pos) {
		return slots.get(pos);
	}

	/**
	 * Sets the array slot at the given position to contain the specified new
	 * element.<br>
	 * <tt>FD_Handle FD_SetArraySlot(FD_Handle array, long pos, FD_Handle item)</tt>
	 * <p>
	 * The object being replaced in the array is returned to the caller so that
	 * it can dispose of the object. No other array elements are affected, and
	 * the size of the array remains unchanged.
	 * 
	 * @param pos
	 *            the slot position. Which array slot to set.
	 * @param item
	 *            the new value of that array slot.
	 * @return the object that used to be in the array slot.
	 */
	public FDObject set(int pos, FDObject item) {
		return slots.set(pos, item);
	}

	/**
	 * Appends the given element to the end of the array.<br>
	 * <tt>DIL_Error FD_AppendArraySlot(FD_Handle array, FD_Handle item)</tt>
	 * 
	 * @param item
	 *            the item to insert.
	 */
	public void append(FDObject item) {
		slots.add(item);
	}

	/**
	 * Inserts the given object into the array at the specified position.<br>
	 * <tt>DIL_Error FD_InsertArraySlot(FD_Handle array, long pos, FD_Handle item)</tt>
	 * <p>
	 * Any objects between that position and the end of the array are moved down
	 * in the array to make room.
	 * 
	 * @param pos
	 *            the slot position. Where to insert the item.
	 * @param item
	 *            the item to insert.
	 */
	public void insert(int pos, FDObject item) {
		slots.add(pos, item);
	}

	/**
	 * Removes the object at the given position in the array.<br>
	 * <tt>FD_Handle FD_RemoveArraySlot(FD_Handle array, long pos)</tt>
	 * <p>
	 * Any objects between that position and the end of the array are moved
	 * forward in the array to fill in the vacated slot. The removed object is
	 * returned to the caller so that the caller can dispose of it, if desired.
	 * 
	 * @param pos
	 *            the slot position. Which item to remove.
	 * @return the element removed.
	 */
	public FDObject remove(int pos) {
		return slots.remove(pos);
	}

	/**
	 * Returns the length of the array.<br>
	 * <tt>long FD_GetLength(FD_Handle obj)</tt>
	 * 
	 * @return the number of slots.
	 */
	public int getLength() {
		return slots.size();
	}

	/**
	 * Set the array size.<br>
	 * <tt>DIL_Error FD_SetLength(FD_Handle obj, long newSize)</tt>
	 * <p>
	 * The length of the array can be directly manipulated with
	 * <tt>FD_SetLength</tt>; this function adds slots at the end of an array
	 * initialised to <tt>kFD_NIL</tt>, or removes slots from the end of the
	 * array.
	 * 
	 * @param newSize
	 *            the array length.
	 */
	public void setLength(int newSize) {
		int lengthOld = slots.size();
		int lengthNew = newSize;

		if (lengthOld == lengthNew) {
			// Nothing to change.
			return;
		}

		if (lengthOld < lengthNew) {
			// Append blank slots.
			for (int i = lengthOld; i < lengthNew; i++) {
				slots.add(FDNil.kFD_NIL);
			}
		} else {
			// Remove slots.
			for (int i = lengthOld; i < lengthNew; i++) {
				slots.remove(lengthOld);
			}
		}
	}
}
