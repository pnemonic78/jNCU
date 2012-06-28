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
import java.util.Map;
import java.util.TreeMap;

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

	/** Default symbol class. */
	public static final NSOFSymbol CLASS_SYMBOL = new NSOFSymbol("symbol");

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
	public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of characters in name (xlong)
		int len = XLong.decodeValue(in);
		// Name (bytes)
		byte[] name = new byte[len];
		readAll(in, name);
		setValue(new String(name, CHARSET_MAC));
	}

	@Override
	public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(NSOF_SYMBOL);

		String name = getValue();
		// Number of characters in name (xlong)
		XLong.encode(name.length(), out);
		// Name (bytes)
		out.write(name.getBytes(CHARSET_MAC));
	}

	@Override
	protected void setValue(String value) {
		if (value == null) {
			super.setValue(value);
			this.valueLower = null;
		} else {
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
		return classes.get(clazz);
	}

	@Override
	public String toString() {
		if (toString == null) {
			String value = getValue();
			if (value == null) {
				toString = NSOFNil.NIL.toString();
			} else {
				StringBuffer buf = new StringBuffer();
				int len = value.length();
				char c;
				boolean colon = false;
				boolean dot = false;

				for (int i = 0; i < len; i++) {
					c = value.charAt(i);

					if ((c < 32) || (c > 127))
						throw new IllegalCharInSymbolException(c);
					if ((c == '|') || (c == '\\'))
						throw new IllegalCharInSymbolException(c);
					if (c == ':')
						colon = true;
					else if (c == '.')
						dot = true;
					buf.append(c);
				}
				if (colon || dot) {
					buf.insert(0, '|');
					buf.append('|');
				}
				toString = "'" + buf.toString();
			}
		}
		return toString;
	}
}
