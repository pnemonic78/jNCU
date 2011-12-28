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
package net.sf.jncu.newton.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Character.
 * 
 * @author Moshe
 */
public class NSOFAnsiCharacter extends NSOFCharacter {

	/**
	 * Constructs a new character.
	 */
	public NSOFAnsiCharacter() {
		super();
	}

	/**
	 * Constructs a new character.
	 * 
	 * @param value
	 *            the character.
	 */
	public NSOFAnsiCharacter(char value) {
		super(value);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		setValue('\u0000');
		// Character code (byte)
		int c = in.read();
		if (c == -1) {
			throw new EOFException();
		}
		setValue((char) (c & 0xFF));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(CHARACTER);
		// Character code (byte)
		out.write(getValue() & 0xFF);
	}
}
