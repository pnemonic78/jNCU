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
package net.sf.jncu.protocol.v2_0;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.newton.stream.NSOFDecoder;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * Command from the Newton with a single NewtonScript object result.
 * 
 * @author moshew
 */
public abstract class DockCommandFromNewtonScript extends DockCommandFromNewton {

	private NSOFObject result;

	/**
	 * Creates a new command.
	 * 
	 * @param cmd
	 *            the command.
	 */
	public DockCommandFromNewtonScript(String cmd) {
		super(cmd);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		setResult(decoder.decode(data));
	}

	/**
	 * Set the result.
	 * 
	 * @param result
	 *            the result.
	 */
	protected void setResult(NSOFObject result) {
		this.result = result;
	}

	/**
	 * Get the result.
	 * 
	 * @return the result.
	 */
	public NSOFObject getResult() {
		return result;
	}

}
