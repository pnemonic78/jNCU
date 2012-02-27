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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Binary Object.
 * 
 * @author Moshe
 */
public class NSOFBinaryObject extends NSOFPointer {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("binary");

	private byte[] value;
	private NSOFObject object;

	/**
	 * Constructs a new binary object.
	 */
	public NSOFBinaryObject() {
		super();
		setNSClass(NS_CLASS);
	}

	/**
	 * Constructs a new binary object.
	 * 
	 * @param value
	 *            the value.
	 */
	public NSOFBinaryObject(byte[] value) {
		this();
		setValue(value);
	}

	/**
	 * Constructs a new binary object.
	 * 
	 * @param value
	 *            the value.
	 */
	public NSOFBinaryObject(NSOFObject value) {
		this();
		setObject(value);
	}

	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of bytes of data (xlong)
		int numBytesData = XLong.decodeValue(in);
		byte[] data = new byte[numBytesData];

		// Class (object)
		NSOFSymbol symbol = (NSOFSymbol) decoder.decode(in);
		setNSClass(symbol);

		// Data
		readAll(in, data);
		setValue(data);
	}

	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(BINARY_OBJECT);

		byte[] v = getValue();
		NSOFObject o = getObject();

		if (o != null) {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			NSOFEncoder enc = new NSOFEncoder(false);
			enc.setPrecedents(false);
			enc.encode(o, bout);
			v = bout.toByteArray();
		}

		if (v == null) {
			// Number of bytes of data (xlong)
			XLong.encode(0, out);

			// Class (object)
			encoder.encode(getNSClass(), out);
		} else {
			// Number of bytes of data (xlong)
			XLong.encode(v.length, out);

			// Class (object)
			encoder.encode(getNSClass(), out);

			// Data
			out.write(v);
		}
	}

	/**
	 * Get the value.
	 * 
	 * @return the value.
	 */
	public byte[] getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(byte[] value) {
		this.value = value;
		this.object = null;
	}

	/**
	 * Set the object value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setObject(NSOFObject value) {
		this.value = null;
		this.object = value;
	}

	/**
	 * Get the object value.
	 * 
	 * @return the value.
	 */
	public NSOFObject getObject() {
		return object;
	}

}
