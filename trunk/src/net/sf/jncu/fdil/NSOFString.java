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
 * Newton Streamed Object Format - String.
 * 
 * @author Moshe
 */
public class NSOFString extends NSOFObject implements Comparable<NSOFString>, Precedent {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("string");

	protected static final String CHARSET = "UTF-16";
	protected static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private String value;
	private String toString;

	/**
	 * Constructs a new string.
	 */
	public NSOFString() {
		super();
		setNSClass(NS_CLASS);
	}

	/**
	 * Constructs a new string.
	 * 
	 * @param value
	 *            the value.
	 */
	public NSOFString(String value) {
		this();
		setValue(value);
	}

	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of bytes in string (xlong)
		int numBytes = XLong.decodeValue(in);
		// String (halfwords)
		byte[] buf = new byte[numBytes];

		int count = 0;
		while (count < numBytes)
			count += in.read(buf, count, numBytes - count);
		// Trim?
		while ((numBytes >= 2) && (buf[numBytes - 2] == 0) && (buf[numBytes - 1] == 0))
			numBytes -= 2;
		setValue(new String(buf, 0, numBytes, CHARSET));
	}

	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(STRING);
		String s = getValue();
		if (s == null) {
			// Number of bytes in string (xlong)
			XLong.encode(0, out);
		} else {
			byte[] buf = s.getBytes(CHARSET);
			// Number of bytes in string (xlong)
			// 2-bytes per character + null-terminated
			XLong.encode(buf.length, out);
			// String (halfwords)
			// Bytes [0] and [1] are 0xFE and 0xFF
			if (buf.length >= 2)
				out.write(buf, 2, buf.length - 2);
			out.write(0);
			out.write(0);
		}
	}

	/**
	 * Get the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(String value) {
		this.value = value;
		this.toString = null;
	}

	@Override
	public int hashCode() {
		return (value == null) ? 0 : value.hashCode();
	}

	/**
	 * <code>" [ { stringChar | escSeq } ]* [ truncEscape ] ] "</code>
	 * <p>
	 * stringChar Consists of a tab character or any ASCII character with code
	 * 32-127 except the double quote (") or backslash (\).<br>
	 * escSeq Consists of either a special character specification sequence or a
	 * Unicode specification sequence. The special character specification
	 * sequence is: backslash (\) followed by a quote ("), backslash (\), the
	 * letter n or the letter t. The escape sequence for specifying Unicode
	 * begins with backslash-u (\\u), is followed by any number of groups of
	 * four hexDigits, and ends with backslash-u (\\u).<br>
	 * truncEscape Consists of the shortened Unicode specification sequence. It
	 * is: backslash-u (\\u), is followed by any number of groups of four
	 * hexDigits.
	 */
	@Override
	public String toString() {
		if (toString == null) {
			if (value != null) {
				StringBuffer buf = new StringBuffer();
				char[] chars = value.toCharArray();

				for (char c : chars) {
					if ((c >= 32) && (c <= 127)) {
						buf.append(c);
					} else if (c == '\n') {
						buf.append("\\n");
					} else if (c == '\r') {
						buf.append("\\r");
					} else if (c == '\t') {
						buf.append("\\t");
					} else if (c == '\\') {
						buf.append("\\\\");
					} else if (c == '"') {
						buf.append("\\\"");
					} else {
						buf.append("\\u");
						buf.append(HEX[(c >>> 12) & 0x000F]);
						buf.append(HEX[(c >>> 8) & 0x000F]);
						buf.append(HEX[(c >>> 4) & 0x000F]);
						buf.append(HEX[(c >>> 0) & 0x000F]);
					}
				}
				toString = "\"" + value + "\"";
			}
		}
		return toString;
	}

	@Override
	public int compareTo(NSOFString that) {
		String valThis = this.getValue();
		String valThat = that.getValue();
		if (valThis == null) {
			return (valThat == null) ? 0 : -1;
		}
		return valThis.compareTo(valThat);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NSOFString) {
			return compareTo((NSOFString) obj) == 0;
		}
		return super.equals(obj);
	}

}
