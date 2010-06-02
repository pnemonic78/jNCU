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

import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * This command requests that the name of the current store be set to the
 * specified name.
 * 
 * <pre>
 * 'ssna'
 * length
 * name ref
 * </pre>
 * 
 * @author moshew
 */
public class DSetStoreName extends DockCommandToNewton {

	/** <tt>kDSetStoreName</tt> */
	public static final String COMMAND = "ssna";

	private String name;

	/**
	 * Creates a new command.
	 */
	public DSetStoreName() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		NSOFString name = new NSOFString(getName());
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(name, data);
	}

	/**
	 * Get the store name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the store name.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		this.name = name;
	}

}
