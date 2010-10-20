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
import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.protocol.v2_0.DockCommandFactory;

/**
 * Docking command from Newton to desktop.
 * 
 * @author moshew
 */
public abstract class DockCommandFromNewton extends DockCommand implements IDockCommandFromNewton {

	/**
	 * Minimum length for command header.<br>
	 * <tt>minimum length := length(prefix) + length(command name) + length(command data)</tt>
	 */
	protected static final int MIN_COMMAND_HEADER_LENGTH = COMMAND_PREFIX_LENGTH + COMMAND_NAME_LENGTH + LENGTH_WORD;

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
		return isCommand(data, 0);
	}

	/**
	 * Is the data a command?
	 * 
	 * @param data
	 *            the data.
	 * @param offset
	 *            the offset.
	 * @return <tt>true</tt> if frame contains a command - <tt>false</tt>
	 *         otherwise.
	 */
	public static boolean isCommand(byte[] data, int offset) {
		if ((data == null) || (data.length < COMMAND_PREFIX_LENGTH)) {
			return false;
		}
		for (int i = 0, j = offset; i < COMMAND_PREFIX_LENGTH; i++, j++) {
			if (COMMAND_PREFIX_BYTES[i] != data[j]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Is the data a command?
	 * 
	 * @param data
	 *            the data.
	 * @return <tt>true</tt> if frame contains a command - <tt>false</tt>
	 *         otherwise.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static boolean isCommand(InputStream data) throws IOException {
		if (data == null) {
			return false;
		}
		int b;
		for (int i = 0; i < COMMAND_PREFIX_LENGTH; i++) {
			b = readByte(data);
			if (COMMAND_PREFIX_BYTES[i] != b) {
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
	 * @return the list of commands.
	 */
	public static List<IDockCommandFromNewton> deserialize(byte[] data) {
		if ((data == null) || (data.length < MIN_COMMAND_HEADER_LENGTH)) {
			return new ArrayList<IDockCommandFromNewton>();
		}
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			return deserialize(in);
		} catch (IOException ioe) {
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Decode the data.
	 * 
	 * @param data
	 *            the data stream.
	 * @return the list of commands.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static List<IDockCommandFromNewton> deserialize(InputStream data) throws IOException {
		List<IDockCommandFromNewton> cmds = new ArrayList<IDockCommandFromNewton>();
		if (data == null) {
			return cmds;
		}
		IDockCommandFromNewton cmd = null;
		try {
			do {
				cmd = deserializeCommand(data);
				if (cmd != null) {
					cmds.add(cmd);
				}
			} while (cmd != null);
		} catch (EOFException eofe) {
			// no more commands.
		}
		return cmds;
	}

	protected static class Command {
		public IDockCommandFromNewton cmd;
		public int offset;

		public Command() {
			super();
		}
	}

	/**
	 * Decode the data.
	 * 
	 * @param data
	 *            the data.
	 * @return the command - {@code null} otherwise.
	 */
	public static IDockCommandFromNewton deserializeCommand(byte[] data) {
		InputStream in = new ByteArrayInputStream(data);
		try {
			return deserializeCommand(in);
		} catch (IOException ioe) {
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Decode the data.
	 * 
	 * @param data
	 *            the data stream.
	 * @return the command - {@code null} otherwise.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static IDockCommandFromNewton deserializeCommand(InputStream data) throws IOException {
		if (!isCommand(data)) {
			return null;
		}
		DockCommandFactory factory = DockCommandFactory.getInstance();
		IDockCommandFromNewton cmd = (IDockCommandFromNewton) factory.create(data);
		if (cmd != null) {
			cmd.decode(data);
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
		if (length == 0x010000) {
			length = readByte(data);
		}
		/*
		 * if ((length != -1) && (data.available() < length)) { throw new
		 * ArrayIndexOutOfBoundsException("length " + length); }
		 */
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
			buf.append((char) c);
			hi = readByte(in);
			lo = readByte(in);
			c = ((hi & 0xFF) << 8) | (lo & 0xFF);
		}
		return buf.toString();
	}

	/**
	 * Read a byte.
	 * 
	 * @param in
	 *            the input.
	 * @return the byte value.
	 * @throws EOFException
	 *             if end of stream is reached.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private static int readByte(InputStream in) throws IOException {
		int b;
		try {
			b = in.read();
		} catch (IOException ioe) {
			// PipedInputStream throws IOException instead of returning -1.
			if (in.available() == 0) {
				throw new EOFException();
			}
			throw ioe;
		}
		if (b == -1) {
			throw new EOFException();
		}
		return b;
	}
}
