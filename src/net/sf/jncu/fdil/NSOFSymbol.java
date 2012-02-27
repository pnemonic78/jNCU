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
import java.util.Locale;

/**
 * Newton Streamed Object Format - Symbol.<br>
 * Symbols are case insensitive.
 * 
 * @author Moshe
 */
public class NSOFSymbol extends NSOFString {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("symbol");

	protected static final String ENCODING = "MacRoman";

	private String valueLower;

	/**
	 * Constructs a new symbol.
	 */
	public NSOFSymbol() {
		super();
		setNSClass(NS_CLASS);
	}

	/**
	 * Constructs a new symbol.
	 * 
	 * @param name
	 *            the symbol name.
	 */
	public NSOFSymbol(String name) {
		this();
		setValue(name);
	}

	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of characters in name (xlong)
		int len = XLong.decodeValue(in);
		// Name (bytes)
		byte[] name = new byte[len];
		readAll(in, name);
		setValue(new String(name, ENCODING));
	}

	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(SYMBOL);

		String name = getValue();
		// Number of characters in name (xlong)
		XLong.encode(name.length(), out);
		// Name (bytes)
		out.write(name.getBytes(ENCODING));
	}

	@Override
	public void setValue(String value) {
		super.setValue(value);
		this.valueLower = (value == null) ? null : value.toLowerCase(Locale.ENGLISH);
	}

	@Override
	public int hashCode() {
		return (valueLower == null) ? 0 : valueLower.hashCode();
	}

	@Override
	public int compareTo(NSOFString that) {
		String valThis = this.valueLower;
		String valThat = that.getValue();
		if (that instanceof NSOFSymbol)
			valThat = ((NSOFSymbol) that).valueLower;
		if (valThis == null)
			return (valThat == null) ? 0 : -1;
		return valThis.toLowerCase(Locale.ENGLISH).compareTo(valThat);
	}

}
