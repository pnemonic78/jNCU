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
package net.sf.jncu.protocol.v2_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

/**
 * This command specifies which translator the desktop should use to import the
 * file. The translator index is the index into the translator list sent by the
 * desktop in the <tt>kDTranslatorList</tt> command. The desktop should
 * acknowledge this command with an indication that the import is proceeding.
 * 
 * <pre>
 * 'tran'
 * length
 * translator index
 * </pre>
 * 
 * @author moshew
 */
public class DSetTranslator extends BaseDockCommandFromNewton {

	/** <tt>kDSetTranslator</tt> */
	public static final String COMMAND = "tran";

	private int translatorIndex;

	public DSetTranslator() {
		super(COMMAND);
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
		setTranslatorIndex(ntohl(data));
	}

	/**
	 * Get the translator index.
	 * 
	 * @return the index.
	 */
	public int getTranslatorIndex() {
		return translatorIndex;
	}

	/**
	 * Set the translator index.
	 * 
	 * @param translatorIndex
	 *            the index.
	 */
	public void setTranslatorIndex(int translatorIndex) {
		this.translatorIndex = translatorIndex;
	}

}
