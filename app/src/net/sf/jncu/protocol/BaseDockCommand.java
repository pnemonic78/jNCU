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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Simple Docking Command implementation.
 * 
 * @author moshew
 */
public abstract class BaseDockCommand implements DockCommand {

	protected static final byte[] COMMAND_PREFIX_BYTES = COMMAND_PREFIX.getBytes();

	protected final String command;
	protected final byte[] commandBytes;
	private int length;

	/**
	 * Creates a new docking command.
	 * 
	 * @param command
	 *            the command.
	 */
	protected BaseDockCommand(String command) {
		super();
		if (command.length() != COMMAND_NAME_LENGTH) {
			throw new IllegalArgumentException("command length must be " + COMMAND_NAME_LENGTH);
		}
		this.command = command;
		this.commandBytes = command.getBytes();
	}

	/**
	 * Creates a new docking command.
	 * 
	 * @param commandBytes
	 *            the command.
	 */
	protected BaseDockCommand(byte[] commandBytes) {
		super();
		if (commandBytes.length != COMMAND_NAME_LENGTH) {
			throw new IllegalArgumentException("command length must be " + COMMAND_NAME_LENGTH);
		}
		this.commandBytes = commandBytes;
		this.command = new String(commandBytes);
	}

	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public int getLength() {
		return length;
	}

	/**
	 * Set the length.
	 * 
	 * @param length
	 *            the length.
	 */
	protected void setLength(int length) {
		this.length = length;
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
	protected static int readByte(InputStream in) throws IOException {
		int b;
		try {
			b = in.read();
		} catch (IOException ioe) {
			// PipedInputStream throws IOException instead of returning -1.
			try {
				if (in.available() == 0)
					throw new EOFException();
			} catch (IOException ioeAvail) {
				if ("Stream closed".equals(ioe.getMessage()))
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
