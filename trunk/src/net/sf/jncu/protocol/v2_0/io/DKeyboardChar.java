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
package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * This command sends 1 character to the Newton for processing. The char is a 2
 * byte Unicode character + a 2 byte state. The state is defined as follows:
 * <ol>
 * <li>Bit 1 = command key down
 * </ol>
 * 
 * <pre>
 * 'kbdc'
 * length
 * char
 * state
 * </pre>
 * 
 * @author moshew
 */
public class DKeyboardChar extends DockCommandToNewton {

	/** <tt>kDKeyboardChar</tt> */
	public static final String COMMAND = "kbdc";

	/** Command key down. */
	public static final int STATE_COMMAND_DOWN = 1;

	private char c;
	private int state;

	/**
	 * Creates a new command.
	 */
	public DKeyboardChar() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		htonl(((getCharacter() & 0xFFFF) << 16) | (getState() & 0xFFFF), data);
	}

	/**
	 * Get the Unicode character.
	 * 
	 * @return the character.
	 */
	public char getCharacter() {
		return c;
	}

	/**
	 * Set the Unicode character.
	 * 
	 * @param c
	 *            the character.
	 */
	public void setCharacter(char c) {
		this.c = c;
	}

	/**
	 * Get the state.
	 * 
	 * @return the state.
	 */
	public int getState() {
		return state;
	}

	/**
	 * Set the state.
	 * 
	 * @param state
	 *            the state.
	 */
	public void setState(int state) {
		this.state = state;
	}

}
