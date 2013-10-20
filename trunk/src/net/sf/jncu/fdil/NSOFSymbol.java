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

	/** Maximum length of a symbol. */
	public static final int MAX_LENGTH = 254;

	/**
	 * Default symbol class.<br>
	 * <tt>kFD_SymSymbol</tt>
	 */
	public static final NSOFSymbol CLASS_SYMBOL = new NSOFSymbol("symbol");

	private String valueLower;

	/**
	 * Constructs a new symbol.<br>
	 * <em>Reserved for use by decoder!</em>
	 */
	public NSOFSymbol() {
		super();
		setObjectClass(CLASS_SYMBOL);
		this.valueLower = "";
	}

	/**
	 * Constructs a new symbol.
	 * 
	 * @param name
	 *            the symbol name.
	 */
	public NSOFSymbol(String name) {
		super();
		setObjectClass(CLASS_SYMBOL);
		setValue(name);
	}

	@Override
	public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of characters in name (xlong)
		int numChars = XLong.decodeValue(in);
		// Name (bytes)
		if (numChars == 0) {
			setValue("");
		} else {
			byte[] name = new byte[numChars];
			readAll(in, name);
			setValue(new String(name, CHARSET_MAC));
		}
	}

	@Override
	public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(NSOF_SYMBOL);

		String name = getValue();
		// Number of characters in name (xlong)
		int numChars = (name == null) ? 0 : name.length();
		XLong.encode(numChars, out);
		if (numChars > 0) {
			// Name (bytes)
			out.write(name.getBytes(CHARSET_MAC));
		}
	}

	@Override
	protected void setValue(String value) {
		if (value == null)
			throw new IllegalArgumentException("non-null value required");
		int len = value.length();
		if (len > MAX_LENGTH)
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

	@Override
	public int hashCode() {
		return valueLower.hashCode();
	}

	@Override
	public int compareTo(NSOFString other) {
		if (other == null)
			return +1;
		if (other instanceof NSOFSymbol) {
			NSOFSymbol that = (NSOFSymbol) other;
			return this.valueLower.compareTo(that.valueLower);
		}
		return super.compareTo(other);
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		return new NSOFSymbol(getValue());
	}

	@Override
	public String toString() {
		if (toString == null) {
			String value = getValue();
			StringBuffer buf = new StringBuffer();
			int len = value.length();
			char c;
			boolean colon = false;

			for (int i = 0; i < len; i++) {
				c = value.charAt(i);

				switch (c) {
				case '.':
				case ':':
				case '?':
					colon = true;
					break;
				}
				buf.append(c);
			}
			if (colon) {
				buf.insert(0, '|');
				buf.append('|');
			}
			toString = "'" + buf.toString();
		}
		return toString;
	}
}
