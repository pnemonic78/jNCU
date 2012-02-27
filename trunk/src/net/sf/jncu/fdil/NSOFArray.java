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

/**
 * Newton Streamed Object Format - Array.
 * 
 * @author Moshe
 */
public class NSOFArray extends NSOFObject implements Precedent {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("array");

	private NSOFObject[] value;

	/**
	 * Constructs a new array.
	 */
	public NSOFArray() {
		super();
		setNSClass(NS_CLASS);
	}

	/**
	 * Constructs a new array.
	 * 
	 * @param value
	 *            the value.
	 */
	public NSOFArray(NSOFObject[] value) {
		this();
		setValue(value);
	}

	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of slots (xlong)
		int length = XLong.decodeValue(in);
		NSOFObject[] slots = new NSOFObject[length];

		// Class (object)
		setNSClass((NSOFSymbol) decoder.decode(in));

		// Slot values in ascending order (objects)
		for (int i = 0; i < length; i++) {
			slots[i] = decoder.decode(in);
		}
		setValue(slots);
	}

	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(ARRAY);

		NSOFObject[] slots = getValue();
		int length = (slots == null) ? 0 : slots.length;

		// Number of slots (xlong)
		XLong.encode(length, out);

		// Class (object)
		encoder.encode(getNSClass(), out);

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
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(NSOFObject[] value) {
		this.value = value;
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

}
