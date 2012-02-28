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
 * Newton Streamed Object Format - Symbol.
 * <p>
 * A symbol object is a variable-size object used as a token or as an
 * identifier. Most often it is used as a slot name or object class. It is
 * composed of ASCII characters with values between 32 and 127 inclusive,
 * excluding the vertical bar (<tt>|</tt>) and backslash (<tt>\</tt>)
 * characters. A symbol must be shorter than 254 characters. When symbols are
 * compared to each other, a case-insensitive comparison is performed.
 * 
 * @author Moshe
 */
public class NSOFSymbol extends NSOFString {

	/** Default symbol class. */
	public static final NSOFSymbol CLASS_SYMBOL = new NSOFSymbol("symbol");

	private String valueLower;

	/**
	 * Constructs a new symbol.
	 */
	public NSOFSymbol() {
		super();
		setObjectClass(CLASS_SYMBOL);
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
		setValue(new String(name, CHARSET_MAC));
	}

	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(NSOF_SYMBOL);

		String name = getValue();
		// Number of characters in name (xlong)
		XLong.encode(name.length(), out);
		// Name (bytes)
		out.write(name.getBytes(CHARSET_MAC));
	}

	@Override
	public void setValue(String value) {
		if (value == null) {
			super.setValue(value);
			this.valueLower = null;
		} else {
			int len = value.length();
			if (len > 254)
				throw new SymbolTooLongException();
			char c;
			for (int i = 0; i < len; i++) {
				c = value.charAt(i);
				if ((c < 32) || (c > 127))
					throw new IllegalCharInSymbolException(c);
				if ((c == '|') || (c == '\\'))
					throw new IllegalCharInSymbolException(c);
			}
			super.setValue(value);
			this.valueLower = value.toLowerCase(Locale.ENGLISH);
		}
	}

	@Override
	public int hashCode() {
		return (valueLower == null) ? 0 : valueLower.hashCode();
	}

	@Override
	public int compareTo(NSOFString other) {
		if (other == null)
			return +1;
		if (other instanceof NSOFSymbol) {
			NSOFSymbol that = (NSOFSymbol) other;
			String valThis = this.valueLower;
			String valThat = that.valueLower;
			if (valThis == null)
				return (valThat == null) ? 0 : -1;
			return valThis.compareTo(valThat);
		}
		return super.compareTo(other);
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		return new NSOFSymbol(getValue());
	}
}
