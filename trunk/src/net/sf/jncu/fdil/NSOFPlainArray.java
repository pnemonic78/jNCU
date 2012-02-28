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
 * Newton Streamed Object Format - Plain Array.
 * 
 * @author Moshe
 */
public class NSOFPlainArray extends NSOFArray {

	/** Default plain array class. */
	public static final NSOFSymbol CLASS_PLAIN_ARRAY = new NSOFSymbol("plainArray");

	/**
	 * Constructs a new array.
	 */
	public NSOFPlainArray() {
		super();
		setObjectClass(CLASS_PLAIN_ARRAY);
	}

	/**
	 * Constructs a new array.
	 * 
	 * @param value
	 *            the value.
	 */
	public NSOFPlainArray(NSOFObject[] value) {
		super(value);
		setObjectClass(CLASS_PLAIN_ARRAY);
	}

	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		setValue((NSOFObject[]) null);

		// Number of slots (xlong)
		int length = XLong.decodeValue(in);
		NSOFObject[] entries = new NSOFObject[length];
		// Slot values in ascending order (objects)
		for (int i = 0; i < length; i++) {
			entries[i] = decoder.decode(in);
		}
		setValue(entries);
	}

	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(NSOF_PLAIN_ARRAY);

		NSOFObject[] slots = getValue();
		if (slots == null) {
			// Number of slots (xlong)
			XLong.encode(0, out);
		} else {
			// Number of slots (xlong)
			XLong.encode(slots.length, out);
			// Slot values in ascending order (objects)
			for (int i = 0; i < slots.length; i++) {
				encoder.encode(slots[i], out);
			}
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		NSOFPlainArray copy = new NSOFPlainArray();
		copy.setValue(this.getValue());
		return copy;
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		NSOFPlainArray copy = new NSOFPlainArray();
		int length = this.getLength();
		for (int i = 0; i < length; i++)
			copy.add(this.get(i).deepClone());
		return copy;
	}
}
