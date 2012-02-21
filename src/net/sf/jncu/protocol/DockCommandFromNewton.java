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
package net.sf.jncu.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * Docking command from Newton to desktop.
 * 
 * @author moshew
 */
public abstract class DockCommandFromNewton extends DockCommand implements IDockCommandFromNewton {

	/**
	 * Creates a new docking command from Newton.
	 * 
	 * @param cmd
	 *            the command.
	 */
	protected DockCommandFromNewton(String cmd) {
		super(cmd);
	}

	/**
	 * Creates a new docking command from Newton.
	 * 
	 * @param cmd
	 *            the command.
	 */
	protected DockCommandFromNewton(byte[] cmd) {
		super(cmd);
	}

	/**
	 * Decode the command.
	 * 
	 * @param data
	 *            the command data.
	 * @throws IOException
	 *             if read past data buffer.
	 */
	@Override
	public void decode(InputStream data) throws IOException {
		int length = ntohl(data);
		setLength(length);
		if (length > 0) {
			decodeData(data);
			// Commands are 4-byte aligned.
			switch (length & 3) {
			case 1:
				data.skip(1);
			case 2:
				data.skip(1);
			case 3:
				data.skip(1);
				break;
			}
		}
	}

	/**
	 * Decode the command data.
	 * 
	 * @param data
	 *            the data.
	 * @throws IOException
	 *             if read past data buffer.
	 */
	protected abstract void decodeData(InputStream data) throws IOException;

	/**
	 * Network-to-host long.<br>
	 * Read 4 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param in
	 *            the input.
	 * @return the number.
	 * @throws IOException
	 *             if read past buffer.
	 */
	public static int ntohl(InputStream in) throws IOException {
		int n24 = (readByte(in) & 0xFF) << 24;
		int n16 = (readByte(in) & 0xFF) << 16;
		int n08 = (readByte(in) & 0xFF) << 8;
		int n00 = (readByte(in) & 0xFF) << 0;

		return n24 | n16 | n08 | n00;
	}

	/**
	 * Network-to-host short.<br>
	 * Read 2 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param in
	 *            the input.
	 * @return the number.
	 * @throws IOException
	 *             if read past buffer.
	 */
	public static short ntohs(InputStream in) throws IOException {
		int n08 = (readByte(in) & 0xFF) << 8;
		int n00 = (readByte(in) & 0xFF) << 0;

		return (short) (n08 | n00);
	}

	/**
	 * Read a <tt>C</tt>-style Unicode string (UTF-16, null-terminated).
	 * 
	 * @param in
	 *            the input.
	 * @return the string.
	 * @throws IOException
	 *             if read past buffer.
	 */
	public static String readString(InputStream in) throws IOException {
		StringBuffer buf = new StringBuffer();
		int hi = readByte(in);
		int lo = readByte(in);
		int c = ((hi & 0xFF) << 8) | (lo & 0xFF);
		while (c != 0) {
			buf.appendCodePoint(c);
			hi = readByte(in);
			lo = readByte(in);
			c = ((hi & 0xFF) << 8) | (lo & 0xFF);
		}
		return buf.toString();
	}

	/**
	 * Read into the whole array.
	 * 
	 * @param in
	 *            the input.
	 * @param b
	 *            the destination buffer.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readAll(InputStream in, byte[] b) throws IOException {
		int count = 0;
		while (count < b.length)
			count += in.read(b, count, b.length - count);
	}
}
