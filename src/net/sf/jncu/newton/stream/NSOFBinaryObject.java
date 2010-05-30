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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Binary Object.
 * 
 * @author Moshe
 */
public class NSOFBinaryObject extends NSOFObject implements Precedent {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("binary");

	private byte[] value;

	/**
	 * Constructs a new binary object.
	 */
	public NSOFBinaryObject() {
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
		// Number of bytes of data (xlong)
		int length = XLong.decodeValue(in);
		byte[] v = new byte[length];

		// Class (object)
		NSOFSymbol symbol = (NSOFSymbol) decoder.decode(in);
		setNSClass(symbol);

		// Data
		if (in.read(v) != length) {
			throw new EOFException();
		}
		setValue(v);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(BINARY_OBJECT);
		byte[] v = getValue();

		// Number of bytes of data (xlong)
		int length = (v == null) ? 0 : v.length;
		XLong.encode(length, out);

		// Class (object)
		encoder.encode(getNSClass(), out);

		// Data
		if (v != null) {
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
	}

}
