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
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Newton Streamed Object Format - Frame.
 * 
 * @author Moshe
 */
public class NSOFFrame extends NSOFObject implements Precedent {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("frame");

	protected final Map<NSOFSymbol, NSOFObject> slots = new LinkedHashMap<NSOFSymbol, NSOFObject>();

	/**
	 * Constructs a new frame.
	 */
	public NSOFFrame() {
		super();
		setNSClass(NS_CLASS);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		this.slots.clear();

		// Number of slots (xlong)
		int length = XLong.decodeValue(in);
		NSOFSymbol[] symbols = new NSOFSymbol[length];

		// Slot tags in ascending order (symbol objects)
		for (int i = 0; i < length; i++) {
			symbols[i] = (NSOFSymbol) decoder.decode(in);
		}

		// Slot values in ascending order (objects)
		for (int i = 0; i < length; i++) {
			put(symbols[i], decoder.decode(in));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(FRAME);

		// Number of slots (xlong)
		XLong.encode(slots.size(), out);

		// Slot tags in ascending order (symbol objects)
		for (NSOFSymbol sym : slots.keySet()) {
			encoder.encode(sym, out);
		}

		// Slot values in ascending order (objects)
		NSOFObject slot;
		for (NSOFSymbol sym : slots.keySet()) {
			slot = slots.get(sym);
			encoder.encode(slot, out);
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
	 * Associates the specified slot value with the specified symbol.
	 * 
	 * @param key
	 *            the slot symbol.
	 * @param value
	 *            the slot value.
	 */
	public void put(NSOFSymbol key, NSOFObject value) {
		slots.put(key, value);
	}

	/**
	 * Associates the specified slot value with the specified symbol.
	 * 
	 * @param key
	 *            the slot symbol name.
	 * @param value
	 *            the slot value.
	 */
	public void put(String key, NSOFObject value) {
		put(new NSOFSymbol(key), value);
	}

	/**
	 * Get the mapped slot value for the specified key.
	 * 
	 * @param key
	 *            the slot symbol.
	 * @return the slot value - <tt>null</tt> otherwise.
	 */
	public NSOFObject get(NSOFSymbol key) {
		return slots.get(key);
	}

	/**
	 * Get the mapped slot value for the specified key.
	 * 
	 * @param key
	 *            the slot symbol name.
	 * @return the slot value - <tt>null</tt> otherwise.
	 */
	public NSOFObject get(String key) {
		return get(new NSOFSymbol(key));
	}

	/**
	 * Remove a slot entry.
	 * 
	 * @param key
	 *            the slot symbol.
	 * @return the removed slot value.
	 */
	public NSOFObject remove(NSOFSymbol key) {
		return slots.remove(key);
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
	 * Get the slot symbols.
	 * 
	 * @return the symbols.
	 */
	public Set<NSOFSymbol> getSymbols() {
		return slots.keySet();
	}
}
