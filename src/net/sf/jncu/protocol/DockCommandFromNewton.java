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
	 * Decode a single command from the Newton.
	 * 
	 * @param data
	 *            the data.
	 * @return the command.
	 */
	public static IDockCommandFromNewton deserialize(byte[] data) {
		List<IDockCommandFromNewton> cmds = deserializeAll(data);
		if (cmds.isEmpty()) {
			return null;
		}
		return cmds.get(0);
	}

	/**
	 * Decode the data.
	 * 
	 * @param data
	 *            the data.
	 * @return the command.
	 */
	public static List<IDockCommandFromNewton> deserializeAll(byte[] data) {
		List<IDockCommandFromNewton> cmds = new ArrayList<IDockCommandFromNewton>();
		if ((data == null) || (data.length < MIN_COMMAND_HEADER_LENGTH)) {
			return cmds;
		}
		IDockCommandFromNewton cmd = null;
		Command c = new Command();
		do {
			deserializeCommand(data, c);
			cmd = c.cmd;
			if (cmd != null) {
				cmds.add(cmd);
			}
		} while (cmd != null);
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
	 * @param c
	 *            the command.
	 */
	protected static void deserializeCommand(byte[] data, Command c) {
		c.cmd = null;
		int offset = c.offset;
		if ((data == null) || ((data.length - offset) < MIN_COMMAND_HEADER_LENGTH)) {
			return;
		}
		if (!isCommand(data, offset)) {
			return;
		}
		offset += COMMAND_PREFIX_LENGTH;
		DockCommandFactory factory = DockCommandFactory.getInstance();
		IDockCommandFromNewton cmd = (IDockCommandFromNewton) factory.create(data, offset);
		offset += COMMAND_NAME_LENGTH;
		if (cmd != null) {
			InputStream in = new ByteArrayInputStream(data, offset, data.length - offset);
			try {
				cmd.decode(in);
				offset = data.length - in.available();
				in.close();
			} catch (IOException ioe) {
				throw new ArrayIndexOutOfBoundsException();
			}
		}
		c.cmd = cmd;
		c.offset = offset;
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
			throw new ArrayIndexOutOfBoundsException("length " + length);
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
