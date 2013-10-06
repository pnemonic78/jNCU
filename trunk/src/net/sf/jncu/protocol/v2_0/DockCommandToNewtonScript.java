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
import java.io.OutputStream;

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

/**
 * Command to the Newton with a single NewtonScript object request.
 * 
 * @author moshew
 */
public abstract class DockCommandToNewtonScript<T extends NSOFObject> extends BaseDockCommandToNewton {

	private T object;

	/**
	 * Creates a new command.
	 * 
	 * @param cmd
	 *            the command.
	 */
	public DockCommandToNewtonScript(String cmd) {
		super(cmd);
	}

	/**
	 * Set the object.
	 * 
	 * @param object
	 *            the object.
	 */
	public void setObject(T object) {
		this.object = object;
	}

	/**
	 * Get the object.
	 * 
	 * @return the object.
	 */
	public T getObject() {
		return object;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(getObject(), data);
	}
}
