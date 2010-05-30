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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * A frame is an aggregate object where each element, called a “slot,” contains
 * any FDIL object, and is indexed by name. The slot name itself is a symbol.
 * Rather than using an integer index to retrieve a value that's been added to a
 * frame (as you would with an array), you specify the slot name to get the slot
 * value.
 * <p>
 * Frame objects are limited to a size of 16 MB.
 * 
 * @author moshew
 */
public class FDFrame extends FDPointer {

	private final Map<String, FDObject> slots = new TreeMap<String, FDObject>();

	/**
	 * Creates an empty frame.
	 */
	public FDFrame() {
		super();
	}

	/**
	 * Adds a key/value pair to the frame.<br>
	 * <tt>FD_Handle FD_SetFrameSlot(FD_Handle frame, const char* slotName, FD_Handle item)</tt>
	 * <p>
	 * If a pair with the specified key already exists in the frame, its
	 * corresponding value object is replaced.
	 * 
	 * @param slotName
	 *            the slot name.
	 * @param item
	 *            the FDIL object to store in that slot.
	 * @return the replaced FDIL object or <tt>kFD_NIL</tt> if the slot does not
	 *         exist.
	 */
	public FDObject set(String slotName, FDObject item) {
		FDObject slot = slots.put(slotName, item);
		return (slot == null) ? FDNil.kFD_NIL : slot;
	}

	/**
	 * Retrieves the slot.<br>
	 * <tt>FD_Handle FD_GetFrameSlot(FD_Handle frame, const char* slotName)</tt>
	 * 
	 * @param slotName
	 *            the slot name.
	 * @return an FDIL object or <tt>kFD_NIL</tt> if the slot does not exist.
	 */
	public FDObject get(String slotName) {
		FDObject slot = slots.get(slotName);
		return (slot == null) ? FDNil.kFD_NIL : slot;
	}

	/**
	 * Returns whether or not a slot with the given name exists in the frame. <br>
	 * <tt>int FD_FrameHasSlot(FD_Handle frame, const char* slotName)</tt>
	 * 
	 * @param slotName
	 *            the slot name.
	 * @return true if slot found.
	 */
	public boolean hasSlot(String slotName) {
		return slots.containsKey(slotName);
	}

	/**
	 * Removes the slot/value pair.<br>
	 * <tt>FD_Handle FD_RemoveFrameSlot(FD_Handle frame, const char* slotName)</tt>
	 * 
	 * @param slotName
	 *            the slot name.
	 * @return the FDIL object in the slot, if the slot exists, <tt>kFD_NIL</tt>
	 *         , otherwise.
	 */
	public FDObject remove(String slotName) {
		FDObject slot = slots.remove(slotName);
		return (slot == null) ? FDNil.kFD_NIL : slot;
	}

	/**
	 * Returns the length of the frame.<br>
	 * <tt>long FD_GetLength(FD_Handle obj)</tt>
	 * 
	 * @return the number of slots.
	 */
	public int getLength() {
		return slots.size();
	}

	/**
	 * Get the set of slot names for traversal.<br>
	 * <tt>FD_Handle FD_GetIndFrameSlotName(FD_Handle frame, long pos)</tt>
	 * 
	 * @return the slot names.
	 */
	public Set<String> getSlotNames() {
		return slots.keySet();
	}

	/**
	 * Get the list of slots for traversal.<br>
	 * <tt>FD_Handle FD_GetIndFrameSlot(FD_Handle frame, long pos)</tt>
	 * 
	 * @return the slot values.
	 */
	public Collection<FDObject> getSlots() {
		return slots.values();
	}
}
