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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Newton Streamed Object Format - Frame.
 * <p>
 * A frame is an aggregate object where each element, called a "slot," contains
 * any FDIL object, and is indexed by name. The slot name itself is a symbol.
 * Rather than using an integer index to retrieve a value that's been added to a
 * frame (as you would with an array), you specify the slot name to get the slot
 * value.
 * 
 * @author Moshe
 */
public class NSOFFrame extends NSOFPointer {

	/**
	 * Default frame class.<br>
	 * <tt>kFD_SymFrame</tt>
	 */
	public static final NSOFSymbol CLASS_FRAME = new NSOFSymbol("frame");

	private final Map<NSOFSymbol, NSOFObject> slots = new HashMap<NSOFSymbol, NSOFObject>();

	/**
	 * Constructs a new frame.
	 */
	public NSOFFrame() {
		super();
		setObjectClass(CLASS_FRAME);
	}

	@Override
	public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
		this.slots.clear();

		// Number of slots (xlong)
		int length = XLong.decodeValue(in);
		NSOFSymbol[] symbols = new NSOFSymbol[length];
		NSOFString str;

		// Slot tags in ascending order (symbol objects)
		for (int i = 0; i < length; i++) {
			str = (NSOFString) decoder.inflate(in);
			if (str instanceof NSOFSymbol) {
				symbols[i] = (NSOFSymbol) str;
			} else {
				symbols[i] = new NSOFSymbol(str.getValue());
			}
		}

		// Slot values in ascending order (objects)
		for (int i = 0; i < length; i++) {
			put(symbols[i], decoder.inflate(in));
		}
	}

	@Override
	public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(NSOF_FRAME);

		// Number of slots (xlong)
		XLong.encode(slots.size(), out);

		// Slot tags in ascending order (symbol objects)
		for (NSOFSymbol sym : slots.keySet()) {
			encoder.flatten(sym, out);
		}

		// Slot values in ascending order (objects)
		NSOFObject slot;
		for (NSOFSymbol sym : slots.keySet()) {
			slot = get(sym);
			encoder.flatten(slot, out);
		}
	}

	@Override
	public int hashCode() {
		return slots.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NSOFFrame) {
			return this.slots.equals(((NSOFFrame) obj).slots);
		}
		return super.equals(obj);
	}

	/**
	 * Is the frame empty?
	 * 
	 * @return true if this frame contains no slots.
	 */
	public boolean isEmpty() {
		return slots.isEmpty();
	}

	/**
	 * Removes all of the slots from this frame.
	 */
	public void clear() {
		slots.clear();
	}

	/**
	 * Adds a key/value pair to the frame.
	 * <p>
	 * If a pair with the specified key already exists in the frame, its
	 * corresponding value object is replaced.
	 * 
	 * @param slotName
	 *            the slot name.
	 * @param value
	 *            the slot value.
	 * @return the replaced object or <tt>NIL</tt> if the slot does not exist.
	 */
	public NSOFObject put(NSOFSymbol name, NSOFObject value) {
		NSOFObject old = slots.put(name, (value == null) ? NSOFNil.NIL : value);
		return (old == null) ? NSOFNil.NIL : old;
	}

	/**
	 * Associates the specified slot value with the specified symbol.
	 * 
	 * @param slotName
	 *            the slot name.
	 * @param value
	 *            the slot value.
	 * @see #put(NSOFSymbol, NSOFObject)
	 */
	public void put(String slotName, NSOFObject value) {
		put(new NSOFSymbol(slotName), value);
	}

	/**
	 * Get the mapped slot value for the specified key.
	 * 
	 * @param slotName
	 *            the slot name.
	 * @return the slot value - {@code NIL} otherwise.
	 */
	public NSOFObject get(NSOFSymbol slotName) {
		NSOFObject value = slots.get(slotName);
		return (value == null) ? NSOFNil.NIL : value;
	}

	/**
	 * Get the mapped slot value for the specified key.
	 * 
	 * @param slotName
	 *            the slot name.
	 * @return the slot value - {@code NIL} otherwise.
	 * @see #get(NSOFSymbol)
	 */
	public NSOFObject get(String slotName) {
		return get(new NSOFSymbol(slotName));
	}

	/**
	 * Get the mapped slot value at the specified index.
	 * <p>
	 * The order in which the objects are returned is not defined. In
	 * particular, you should not expect to retrieve them in the order in which
	 * they were inserted.
	 * 
	 * @param pos
	 *            an index into the frame.
	 * @return the slot value - {@code NIL} otherwise.
	 */
	public NSOFObject get(int pos) {
		List<NSOFSymbol> names = new ArrayList<NSOFSymbol>(slots.keySet());
		NSOFSymbol key = names.get(pos);
		return get(key);
	}

	/**
	 * Removes the slot/value pair.
	 * 
	 * @param slotName
	 *            the slot name.
	 * @return the removed slot value - {@code NIL} otherwise.
	 */
	public NSOFObject remove(NSOFSymbol slotName) {
		NSOFObject value = slots.remove(slotName);
		return (value == null) ? NSOFNil.NIL : value;
	}

	@Override
	public String toString() {
		return slots.toString();
	}

	/**
	 * Get the number of slots.
	 * 
	 * @return the size.
	 */
	public int size() {
		return slots.size();
	}

	/**
	 * Get the set of slot names for traversal.
	 * 
	 * @return the names.
	 */
	public Set<NSOFSymbol> getNames() {
		return slots.keySet();
	}

	/**
	 * Returns whether or not a slot with the given name exists in the frame.
	 * 
	 * @param slotName
	 *            the slot name.
	 * @return true if slot found.
	 */
	public boolean hasSlot(NSOFSymbol slotName) {
		return slots.containsKey(slotName);
	}

	/**
	 * Returns whether or not a slot with the given name exists in the frame.
	 * 
	 * @param slotName
	 *            the slot name.
	 * @return true if slot found.
	 * @see #hasSlot(NSOFSymbol)
	 */
	public boolean hasSlot(String slotName) {
		return hasSlot(new NSOFSymbol(slotName));
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		NSOFFrame copy = new NSOFFrame();
		copy.slots.putAll(this.slots);
		return copy;
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		NSOFFrame copy = new NSOFFrame();
		for (NSOFSymbol name : this.slots.keySet()) {
			copy.slots.put(name, this.slots.get(name).deepClone());
		}
		return copy;
	}

	/**
	 * Add all the slots.
	 * 
	 * @param frame
	 *            the source frame.
	 */
	public void putAll(NSOFFrame frame) {
		this.slots.putAll(frame.slots);
	}
}
