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
package net.sf.jncu.util;

/**
 * Number utilities.
 * 
 * @author moshew
 */
public class NumberUtils {

	/**
	 * Creates a new utility.
	 */
	private NumberUtils() {
		super();
	}

	/**
	 * Convert the 64-bit number of an array of bytes arranged in Big Endian
	 * order.
	 * 
	 * @param l
	 *            the number.
	 * @return the array of 8 bytes.
	 */
	public static byte[] toBytes(long l) {
		byte[] buf = new byte[8];
		for (int i = 7; i >= 0; i--, l >>>= 8) {
			buf[i] = (byte) (l & 0xFF);
		}
		return buf;
	}

	/**
	 * Convert the array of bytes to a 64-bit number arranged in Big Endian
	 * order.
	 * 
	 * @param b
	 *            the array of 8 bytes.
	 * @return the 64-bit number.
	 */
	public static long toLong(byte[] b) {
		if ((b == null) || (b.length == 0)) {
			return 0;
		}
		long l = 0;
		for (int i = 0; i < 8; i++) {
			l <<= 8;
			l |= (b[i] & 0xFFL);
		}
		return l;
	}

	/**
	 * Formats a content size to be in the form of bytes, kilobytes, megabytes,
	 * etc.
	 * 
	 * @param number
	 *            size value to be formatted.
	 * @return formatted string with the number.
	 */
	public static String formatFileSize(long number) {
		float result = number;
		String suffix = "B";
		if (result > 900) {
			suffix = "KB";
			result = result / 1024;
		}
		if (result > 900) {
			suffix = "MB";
			result = result / 1024;
		}
		if (result > 900) {
			suffix = "GB";
			result = result / 1024;
		}
		if (result > 900) {
			suffix = "TB";
			result = result / 1024;
		}
		if (result > 900) {
			suffix = "PB";
			result = result / 1024;
		}
		if (result < 1) {
			return String.format("%.2f %s", result, suffix);
		}
		if (result < 10) {
			return String.format("%.2f %s", result, suffix);
		}
		if (result < 100) {
			return String.format("%.2f %s", result, suffix);
		}
		return String.format("%.0f %s", result, suffix);
	}
}
