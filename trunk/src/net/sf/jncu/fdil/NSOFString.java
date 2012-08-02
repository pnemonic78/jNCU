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
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

import net.sf.lang.ControlCharacter;

/**
 * Newton Streamed Object Format - String.
 * <p>
 * An array of Unicode characters.
 * 
 * @author Moshe
 */
public class NSOFString extends NSOFPointer implements Comparable<NSOFString>, CharSequence {

	/**
	 * Default string class.<br>
	 * <tt>kFD_SymString</tt>
	 */
	public static final NSOFSymbol CLASS_STRING = new NSOFSymbol("string");

	public static final NSOFSymbol CLASS_ADDRESS = new NSOFSymbol("address");
	public static final NSOFSymbol CLASS_COMPANY = new NSOFSymbol("company");
	public static final NSOFSymbol CLASS_NAME = new NSOFSymbol("name");
	public static final NSOFSymbol CLASS_TITLE = new NSOFSymbol("title");
	public static final NSOFSymbol CLASS_PHONE = new NSOFSymbol("phone");
	public static final NSOFSymbol CLASS_PHONE_HOME = new NSOFSymbol("homePhone");
	public static final NSOFSymbol CLASS_PHONE_WORK = new NSOFSymbol("workPhone");
	public static final NSOFSymbol CLASS_PHONE_FAX = new NSOFSymbol("faxPhone");
	public static final NSOFSymbol CLASS_PHONE_OTHER = new NSOFSymbol("otherPhone");
	public static final NSOFSymbol CLASS_PHONE_CAR = new NSOFSymbol("carPhone");
	public static final NSOFSymbol CLASS_PHONE_BEEPER = new NSOFSymbol("beeperPhone");
	public static final NSOFSymbol CLASS_PHONE_MOBILE = new NSOFSymbol("mobilePhone");
	public static final NSOFSymbol CLASS_PHONE_HOME_FAX = new NSOFSymbol("homeFaxPhone");

	/** 8-bit ASCII character encoding. */
	public static final String CHARSET_ASCII = "ASCII";
	/** 16-bit Unicode character encoding. */
	public static final String CHARSET_UTF16 = "UTF-16";
	/** MacRoman character encoding. */
	public static final String CHARSET_MAC = "MacRoman";
	/** Windows character encoding. */
	public static final String CHARSET_WIN = "windows-1252";

	/**
	 * Character in place of the embedded ink for 16-bit strings.<br>
	 * <tt>kInkChar</tt>
	 */
	protected static final char INK = 0xF700;
	/**
	 * Character in place of the embedded ink for 8-bit strings.<br>
	 * <tt>kInkChar</tt>
	 */
	protected static final char INK8 = ControlCharacter.SUB;

	protected static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static final Map<NSOFSymbol, NSOFSymbol> classes = new TreeMap<NSOFSymbol, NSOFSymbol>();

	static {
		// For compatibility with the version of NewtonScript found on Newton
		// 1.x OS devices, the following classes are considered subclasses of
		// "string"
		setInheritance(CLASS_ADDRESS, CLASS_STRING);
		setInheritance(CLASS_COMPANY, CLASS_STRING);
		setInheritance(CLASS_NAME, CLASS_STRING);
		setInheritance(CLASS_TITLE, CLASS_STRING);
		setInheritance(CLASS_PHONE, CLASS_STRING);

		// Furthermore the following classes are considered subclasses of
		// "phone"
		setInheritance(CLASS_PHONE_HOME, CLASS_PHONE);
		setInheritance(CLASS_PHONE_WORK, CLASS_PHONE);
		setInheritance(CLASS_PHONE_FAX, CLASS_PHONE);
		setInheritance(CLASS_PHONE_OTHER, CLASS_PHONE);
		setInheritance(CLASS_PHONE_CAR, CLASS_PHONE);
		setInheritance(CLASS_PHONE_BEEPER, CLASS_PHONE);
		setInheritance(CLASS_PHONE_MOBILE, CLASS_PHONE);
		setInheritance(CLASS_PHONE_HOME_FAX, CLASS_PHONE);
	}

	private String value;
	protected boolean valueSet;
	protected String toString;

	/**
	 * Constructs a new string.
	 */
	public NSOFString() {
		super();
		setObjectClass(CLASS_STRING);
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
	public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of bytes in string (xlong)
		int numBytes = XLong.decodeValue(in);
		// String (halfwords)
		byte[] buf = new byte[numBytes];
		readAll(in, buf);
		// Trim?
		while ((numBytes >= 2) && (buf[numBytes - 2] == 0) && (buf[numBytes - 1] == 0))
			numBytes -= 2;
		setValue(new String(buf, 0, numBytes, CHARSET_UTF16));
	}

	@Override
	public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
		NSOFSymbol nsClass = getObjectClass();
		if (CLASS_STRING.equals(nsClass)) {
			out.write(NSOF_STRING);
			String s = getValue();
			if (s == null) {
				// Number of bytes in string (xlong)
				XLong.encode(0, out);
			} else {
				byte[] buf = s.getBytes(CHARSET_UTF16);
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
		} else {
			out.write(NSOF_BINARY);
			String s = getValue();
			if (s == null) {
				// Number of bytes of data (xlong)
				XLong.encode(0, out);

				// Class (object)
				encoder.flatten(nsClass, out);
			} else {
				byte[] buf = s.getBytes(CHARSET_UTF16);
				// Number of bytes of data (xlong)
				// 2-bytes per character + null-terminated
				XLong.encode(buf.length, out);

				// Class (object)
				encoder.flatten(nsClass, out);

				// Data
				// Bytes [0] and [1] are 0xFE and 0xFF
				if (buf.length >= 2)
					out.write(buf, 2, buf.length - 2);
				out.write(0);
				out.write(0);
			}
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
	protected void setValue(String value) {
		if (valueSet)
			throw new IllegalArgumentException("value already set");
		this.value = value;
		this.valueSet = true;
		this.toString = null;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	protected void setValue(char[] value) {
		setValue(new String(value));
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the <tt>null</tt>-terminated series of ASCII characters.
	 */
	protected void setValue(byte[] value) {
		int length = value.length;
		for (int i = 0; i < length; i++) {
			if (value[i] == 0) {
				length = i;
				break;
			}
		}
		String s;
		try {
			s = new String(value, 0, length, CHARSET_MAC);
		} catch (UnsupportedEncodingException uee) {
			// Should never arrive here!
			s = new String(value, 0, length);
		}
		s = s.replace(INK8, INK);
		setValue(s);
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
			if (value == null) {
				toString = NSOFNil.NIL.toString();
			} else {
				StringBuffer buf = new StringBuffer();
				int len = value.length();
				char c;

				for (int i = 0; i < len; i++) {
					c = value.charAt(i);
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
				toString = "\"" + buf.toString() + "\"";
			}
		}
		return toString;
	}

	@Override
	public int compareTo(NSOFString that) {
		if (that == null)
			return +1;
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

	/**
	 * Is this a rich string?
	 * <p>
	 * You may receive a rich string from a Newton device. A rich string is a
	 * string with embedded ink data. You cannot create a rich string, nor
	 * interpret the data in the ink portion of a rich string. When translating
	 * rich strings, a {@code 0xF700} or {@code 0x1A} character is inserted in
	 * the place of the embedded ink, depending on whether you are extracting
	 * 16-bit or 8-bit characters.
	 * 
	 * @return true if rich string.
	 */
	public boolean isRich() {
		return (value != null) && (value.indexOf(INK) >= 0);
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		return new NSOFString(getValue());
	}

	@Override
	public int length() {
		return (value == null) ? 0 : value.length();
	}

	@Override
	public char charAt(int index) {
		if (value == null)
			throw new NullPointerException();
		return value.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		if (value == null)
			throw new NullPointerException();
		return value.subSequence(start, end);
	}

	/**
	 * Get the inheritances.
	 * 
	 * @return the inheritances.
	 */
	public static Map<NSOFSymbol, NSOFSymbol> getInheritances() {
		return classes;
	}

	/**
	 * Set the inheritances.
	 * 
	 * @param inheritances
	 *            the inheritances.
	 */
	public static void setInheritances(Map<NSOFSymbol, NSOFSymbol> inheritances) {
		classes.clear();
		if (inheritances != null)
			classes.putAll(inheritances);
	}

	/**
	 * Set an inheritance.
	 * 
	 * @param clazz
	 *            the class.
	 * @param superclass
	 *            the superclass.
	 */
	public static void setInheritance(NSOFSymbol clazz, NSOFSymbol superclass) {
		classes.put(clazz, superclass);
	}

	/**
	 * Get the inheritance.
	 * 
	 * @param clazz
	 *            the class.
	 * @return the superclass - {@code null} otherwise.
	 */
	public static NSOFSymbol getInheritance(NSOFSymbol clazz) {
		if (clazz.getValue().startsWith("string."))
			return CLASS_STRING;
		return classes.get(clazz);
	}
}
