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
 * FDIL object handle.
 * 
 * @author Moshe
 */
public class FDHandle extends Object implements Comparable<FDHandle> {

	/**
	 * The {@code ref} field is the same long as in the non-debug
	 * <tt>FD_Handle</tt>.
	 * <p>
	 * The lowest two bits determine the object’s basic type, as follows:<br>
	 * {@code 00} = integer<br>
	 * {@code 01} = pointer object<br>
	 * {@code 10} = immediate object<br>
	 * {@code 11} = magic pointer
	 * <p>
	 * If the {@code ref} is an integer, the value is contained in the upper 30
	 * bits. If the object is an immediate, the next two low order bits
	 * represent the object’s type:<br>
	 * {@code 0010} = special immediate<br>
	 * {@code 0110} = character immediate<br>
	 * {@code 1010} = Boolean immediate<br>
	 * {@code 1110} = reserved immediate
	 * <p>
	 * In an immediate object, the upper 28 bits contain the object’s value. For
	 * example, these upper 28 bits hold the 16-bit Unicode character in a
	 * character object.<br>
	 * A pointer’s upper 30 bits contain an index into an internal object table.
	 * <br>
	 * A magic pointer object’s upper 30 bits contain the magic pointer value.
	 */
	private final int ref;
	/**
	 * The flags field contains a bit field describing the object. The lowest 2
	 * bits of this field specifies the object’s type, as follows:<br>
	 * {@code 00} = raw binary object<br>
	 * {@code 01} = array<br>
	 * {@code 10} = large binary object<br>
	 * {@code 11} = frame
	 */
	private int flags;
	/**
	 * The {@code size} field specifies the object’s size. For binary objects,
	 * this is the number of user bytes in the object. For arrays and frames,
	 * this is the number of elements in the object times
	 * <tt>sizeof(FD_Handle)</tt>. For large binary objects, this is
	 * <tt>sizeof(FD_LargeBinaryData)</tt>.
	 */
	private int size;
	/**
	 * The next field is the object’s class, {@code oClass}, if the object is
	 * anything but a frame, or the frame map, {@code map}, if the object is a
	 * frame. If the object is a frame it’s class is stored in a slot named "
	 * <tt>class</tt>" containing a symbol. A frame’s map is simply an array of
	 * symbols containing the slot names used in the frame. There is one
	 * difference between a frame map and a regular array. A frame map contains
	 * the value zero in its {@code oClass} field.
	 */
	private FDHandle oClass;

	/**
	 * Creates a new handle.
	 * 
	 * @param ref
	 *            the handle reference.
	 */
	public FDHandle(int ref) {
		super();
		this.ref = ref;
	}

	/**
	 * Get the handle reference.
	 * 
	 * @return the reference.
	 */
	public int getReference() {
		return ref;
	}

	/**
	 * Get the flags.
	 * 
	 * @return the flags.
	 */
	public int getFlags() {
		return flags;
	}

	/**
	 * Get the size.
	 * 
	 * @return the size.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Get the symbol object for the class.
	 * 
	 * @return the class.
	 */
	public FDHandle getObjectClass() {
		return oClass;
	}

	@Override
	public int hashCode() {
		return ref;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FDHandle) {
			return ref == ((FDHandle) obj).ref;
		}
		return false;
	}

	@Override
	public int compareTo(FDHandle that) {
		return this.ref - that.ref;
	}
}
