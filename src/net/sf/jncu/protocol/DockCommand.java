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

/**
 * Docking Command.
 * 
 * @author moshew
 */
public abstract class DockCommand implements IDockCommand {

	/** Number of bytes for a word. */
	protected static final int LENGTH_WORD = 4;
	/** Number of command characters. */
	protected static final int COMMAND_LENGTH = LENGTH_WORD;

	/** False. */
	public static final int FALSE = 0;
	/** True. */
	public static final int TRUE = 1;

	/**
	 * <tt>kDNewtonDock</tt><br>
	 * Command prefix.
	 */
	public static final String COMMAND_PREFIX = "newtdock";
	protected static final byte[] COMMAND_PREFIX_BYTES = COMMAND_PREFIX.getBytes();
	protected static final int kDNewtonDockLength = COMMAND_PREFIX_BYTES.length;

	protected final String command;
	protected final byte[] commandBytes;
	private int length;

	/**
	 * Creates a new docking command.
	 * 
	 * @param command
	 *            the command.
	 */
	protected DockCommand(String command) {
		super();
		if (command.length() != COMMAND_LENGTH) {
			throw new IllegalArgumentException("command length must be " + COMMAND_LENGTH);
		}
		this.command = command;
		this.commandBytes = command.getBytes();
	}

	/**
	 * Creates a new docking command.
	 * 
	 * @param cmdBytes
	 *            the command.
	 */
	protected DockCommand(byte[] commandBytes) {
		super();
		if (commandBytes.length != COMMAND_LENGTH) {
			throw new IllegalArgumentException("command length must be " + COMMAND_LENGTH);
		}
		this.commandBytes = commandBytes;
		this.command = new String(commandBytes);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.protocol.IDockCommand#getCommand()
	 */
	public String getCommand() {
		return command;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.protocol.IDockCommand#getLength()
	 */
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

}
