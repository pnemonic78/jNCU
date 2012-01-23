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
import java.io.OutputStream;

import net.sf.jncu.io.RewriteByteArrayOutputStream;

/**
 * Docking command from desktop to Newton.
 * 
 * @author moshew
 */
public abstract class DockCommandToNewton extends DockCommand implements IDockCommandToNewton {

	/**
	 * Creates a new docking command from Newton.
	 * 
	 * @param cmd
	 *            the command.
	 */
	protected DockCommandToNewton(String cmd) {
		super(cmd);
	}

	/**
	 * Creates a new docking command from Newton.
	 * 
	 * @param cmd
	 *            the command.
	 */
	protected DockCommandToNewton(byte[] cmd) {
		super(cmd);
	}

	@Override
	public byte[] getPayload() throws IOException {
		RewriteByteArrayOutputStream payload = new RewriteByteArrayOutputStream();
		int length = 0;
		int indexLength, indexData;

		try {
			payload.write(COMMAND_PREFIX_BYTES);
			payload.write(commandBytes);
			indexLength = payload.size();
			htonl(length, payload);
			indexData = payload.size();
			writeCommandData(payload);
			length = payload.size() - indexData;
			setLength(length);
			payload.seek(indexLength);
			htonl(length, payload);
			payload.seekToEnd();
			// 4-byte align
			switch (payload.size() & 3) {
			case 1:
				payload.write(0);
			case 2:
				payload.write(0);
			case 3:
				payload.write(0);
				break;
			}
		} finally {
			try {
				payload.close();
			} catch (Exception e) {
				// ignore
			}
		}
		return payload.toByteArray();
	}

	/**
	 * Encode the data to write.
	 * 
	 * @param data
	 *            the data output stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected abstract void writeCommandData(OutputStream data) throws IOException;

	/**
	 * Host-to-network long.<br>
	 * Write 4 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param out
	 *            the output.
	 * @param frame
	 *            the frame data.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static void htonl(int n, OutputStream out) throws IOException {
		out.write((n >> 24) & 0xFF);
		out.write((n >> 16) & 0xFF);
		out.write((n >> 8) & 0xFF);
		out.write((n >> 0) & 0xFF);
	}

	/**
	 * Host-to-network long.<br>
	 * Write 4 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param out
	 *            the output.
	 * @param frame
	 *            the frame data.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static void htonl(long n, OutputStream out) throws IOException {
		htonl((int) ((n >> 32) & 0xFFFFFFFFL), out);
		htonl((int) ((n >> 0) & 0xFFFFFFFFL), out);
	}

	/**
	 * Write a <tt>C</tt>-style Unicode string (UTF-16, null-terminated).
	 * 
	 * @param s
	 *            the string.
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static void writeString(String s, OutputStream out) throws IOException {
		if ((s != null) && (s.length() > 0)) {
			byte[] utf16 = s.getBytes("UTF-16");
			// The 1st and 2nd bytes are UTF-16 header 0xFE and 0xFF.
			out.write(utf16, 2, utf16.length - 2);
		}
		// Null-terminated string.
		out.write(0);
		out.write(0);
	}
}
