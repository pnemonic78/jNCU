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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Newton Streamed Object Format - Array.
 * <p>
 * An array object is a variable-size object whose contents are divided into a
 * series of other objects. Each division is called a "slot". Each slot consists
 * of an FDIL object. Objects can be inserted into an array or appended to the
 * end of an array.
 * <p>
 * The array's slots are initialised to <tt>kFD_NIL</tt>.
 * 
 * @author Moshe
 */
public class NSOFArray extends NSOFPointer {

	/**
	 * Default array class.<br>
	 * <tt>kFD_SymArray</tt>
	 */
	public static final NSOFSymbol CLASS_ARRAY = new NSOFSymbol("array");

	private final List<NSOFObject> value;

	/**
	 * Creates a new array of size {@code 0}.
	 */
	public NSOFArray() {
		this(0);
	}

	/**
	 * Constructs a new array.
	 * 
	 * @param size
	 *            the initial size, number of slots, of the array.
	 */
	public NSOFArray(int size) {
		super();
		setObjectClass(CLASS_ARRAY);
		this.value = new ArrayList<NSOFObject>(size);
	}

	/**
	 * Constructs a new array.
	 * 
	 * @param value
	 *            the value.
	 */
	public NSOFArray(NSOFObject[] value) {
		this((value == null) ? 0 : value.length);
		setValue(value);
	}

	@Override
	public String toString() {
		return (value == null) ? null : value.toString();
	}

	@Override
	public int hashCode() {
		return (value == null) ? 0 : value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NSOFArray) {
			return this.getValue().equals(((NSOFArray) obj).getValue());
		}
		return super.equals(obj);
	}

	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of slots (xlong)
		int length = XLong.decodeValue(in);
		List<NSOFObject> slots = new ArrayList<NSOFObject>(length);

		// Class (object)
		setObjectClass((NSOFSymbol) decoder.decode(in));

		// Slot values in ascending order (objects)
		for (int i = 0; i < length; i++) {
			slots.add(decoder.decode(in));
		}
		setValue(slots);
	}

	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(NSOF_ARRAY);

		NSOFObject[] slots = getValue();
		int length = (slots == null) ? 0 : slots.length;

		// Number of slots (xlong)
		XLong.encode(length, out);

		// Class (object)
		encoder.encode(getObjectClass(), out);

		// Slot values in ascending order (objects)
		if (slots != null) {
			for (int i = 0; i < length; i++) {
				encoder.encode(slots[i], out);
			}
		}
	}

	/**
	 * Get the value.
	 * 
	 * @return the value
	 */
	public NSOFObject[] getValue() {
		NSOFObject[] arr = new NSOFObject[getLength()];
		if (value != null)
			arr = value.toArray(arr);
		return arr;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(NSOFObject[] value) {
		this.value.clear();
		if (value != null) {
			for (NSOFObject slot : value)
				add(slot);
		}
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(List<NSOFObject> value) {
		this.value.clear();
		if (value != null) {
			for (NSOFObject slot : value)
				add(slot);
		}
	}

	/**
	 * Get the array length.
	 * 
	 * @return the number of slots.
	 */
	public int getLength() {
		return value.size();
	}

	/**
	 * Set the array size.
	 * <p>
	 * This function adds slots at the end of an array initialised to
	 * <tt>kFD_NIL</tt>, or removes slots from the end of the array.
	 * 
	 * @param newSize
	 *            the array length.
	 */
	public void setLength(int newSize) {
		int lengthOld = value.size();
		int lengthNew = newSize;

		if (lengthOld == lengthNew) {
			// Nothing to change.
			return;
		}
		if (lengthOld < lengthNew) {
			// Append blank slots.
			for (int i = lengthOld; i < lengthNew; i++) {
				add(null);
			}
		} else {
			// Remove slots.
			for (int i = lengthOld; i < lengthNew; i++) {
				remove(lengthOld);
			}
		}
	}

	/**
	 * Returns the object in the given slot of the array.
	 * 
	 * @param pos
	 *            the slot position.
	 * @return the item in that array slot.
	 */
	public NSOFObject get(int pos) {
		return value.get(pos);
	}

	/**
	 * Sets the array slot at the given position to contain the specified new
	 * element.
	 * 
	 * @param pos
	 *            the slot position.
	 * @param item
	 *            the new value of that array slot.
	 * @return the object that used to be in the array slot.
	 */
	public NSOFObject set(int pos, NSOFObject item) {
		if (item == null)
			item = NSOFNil.NIL;
		return value.set(pos, item);
	}

	/**
	 * Appends the given element to the end of the array.
	 * 
	 * @param item
	 *            the item to insert.
	 */
	public void add(NSOFObject item) {
		if (item == null)
			item = NSOFNil.NIL;
		value.add(item);
	}

	/**
	 * Inserts the given object into the array at the specified position.
	 * 
	 * @param pos
	 *            the slot position.
	 * @param item
	 *            the item to insert.
	 */
	public void insert(int pos, NSOFObject item) {
		value.add(pos, item);
	}

	/**
	 * Removes the object at the given position in the array.
	 * 
	 * @param pos
	 *            the slot position.
	 * @return the element removed.
	 */
	public NSOFObject remove(int pos) {
		return value.remove(pos);
	}
}
