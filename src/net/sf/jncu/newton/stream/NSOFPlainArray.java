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

/**
 * Newton Streamed Object Format - Plain Array.
 * 
 * @author Moshe
 */
public class NSOFPlainArray extends NSOFArray {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("plainArray");

	/**
	 * Constructs a new array.
	 */
	public NSOFPlainArray() {
		super();
		setNSClass(NS_CLASS);
	}

	/**
	 * Constructs a new array.
	 * 
	 * @param value
	 *            the value.
	 */
	public NSOFPlainArray(NSOFObject[] value) {
		super(value);
		setNSClass(NS_CLASS);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of slots (xlong)
		int len = XLong.decodeValue(in);
		NSOFObject[] entries = new NSOFObject[len];
		// Slot values in ascending order (objects)
		for (int i = 0; i < len; i++) {
			entries[i] = decoder.decode(in);
		}
		setValue(entries);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(PLAIN_ARRAY);

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
}
