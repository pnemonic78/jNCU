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
package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * Create a soup.
 * 
 * <pre>
 * 'csop'
 * length
 * soup name
 * index description
 * </pre>
 * 
 * @author moshew
 */
public class DCreateSoup extends DockCommandToNewton {

	/** <tt>kDCreateSoup</tt> */
	public static final String COMMAND = "csop";

	private String name;
	private NSOFObject index;

	/**
	 * Creates a new command.
	 */
	public DCreateSoup(String cmd) {
		super(COMMAND);
	}

	/**
	 * Get the soup name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the soup name.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the index description.
	 * 
	 * @return the index.
	 */
	public NSOFObject getIndex() {
		return index;
	}

	/**
	 * Set the index description.
	 * 
	 * @param index
	 *            the index.
	 */
	public void setIndex(NSOFObject index) {
		this.index = index;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		writeString(getName(), data);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(getIndex(), data);
	}

}
