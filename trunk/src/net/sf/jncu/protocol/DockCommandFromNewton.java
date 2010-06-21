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

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.v2_0.DockCommandFactory;

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
	 * Is the data a command?
	 * 
	 * @param data
	 *            the data.
	 * @return <tt>true</tt> if frame contains a command - <tt>false</tt>
	 *         otherwise.
	 */
	public static boolean isCommand(byte[] data) {
		if ((data == null) || (data.length < kDNewtonDockLength)) {
			return false;
		}
		for (int i = 0; i < kDNewtonDockLength; i++) {
			if (COMMAND_PREFIX_BYTES[i] != data[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Decode the data.
	 * 
	 * @param data
	 *            the data.
	 * @return the command.
	 */
	public static IDockCommandFromNewton deserialize(byte[] data) {
		if ((data == null) || (data.length < 16)) {
			return null;
		}
		int offset = 0;
		byte[] cmdName = new byte[COMMAND_LENGTH];
		System.arraycopy(data, kDNewtonDockLength, cmdName, offset, COMMAND_LENGTH);
		offset += kDNewtonDockLength;
		DockCommandFactory factory = DockCommandFactory.getInstance();
		IDockCommandFromNewton cmd = (IDockCommandFromNewton) factory.create(cmdName);
		offset += COMMAND_LENGTH;
		if (cmd != null) {
			byte[] dataBytes = new byte[data.length - offset];
			System.arraycopy(data, offset, dataBytes, 0, dataBytes.length);
			InputStream in = new ByteArrayInputStream(dataBytes);
			try {
				cmd.decode(in);
			} catch (IOException ioe) {
				throw new ArrayIndexOutOfBoundsException();
			}
		}
		return cmd;
	}

	/**
	 * Decode the command.
	 * 
	 * @param data
	 *            the command data.
	 * @throws IOException
	 *             if read past data buffer.
	 */
	public void decode(InputStream data) throws IOException {
		int length = ntohl(data);
		setLength(length);
		if ((length != -1) && (data.available() < length)) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (length > 0) {
			decodeData(data);
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
		int n24 = (in.read() & 0xFF) << 24;
		int n16 = (in.read() & 0xFF) << 16;
		int n08 = (in.read() & 0xFF) << 8;
		int n00 = (in.read() & 0xFF) << 0;

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
		int n08 = (in.read() & 0xFF) << 8;
		int n00 = (in.read() & 0xFF) << 0;

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
		int hi = in.read();
		if (hi == -1) {
			throw new EOFException();
		}
		int lo = in.read();
		if (lo == -1) {
			throw new EOFException();
		}
		int c = ((hi & 0xFF) << 8) | (lo & 0xFF);
		while (c != 0) {
			buf.append((char) c);
			hi = in.read();
			if (hi == -1) {
				throw new EOFException();
			}
			lo = in.read();
			if (lo == -1) {
				throw new EOFException();
			}
			c = ((hi & 0xFF) << 8) | (lo & 0xFF);
		}
		return buf.toString();
	}
}
