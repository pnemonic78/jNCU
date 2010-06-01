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
package net.sf.jncu.protocol.v1_0.query;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.newton.stream.NSOFArray;
import net.sf.jncu.newton.stream.NSOFDecoder;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDInheritance</tt><br>
 * Inheritance. This is a response to a <tt>kDGetInheritance</tt> request.
 * 
 * <pre>
 * 'dinh'
 * length
 * array of class, superclass pairs
 * </pre>
 */
public class DInheritance extends DockCommandFromNewton {

	public static final String COMMAND = "dinh";

	private List<NSOFObject> inheritances;

	/**
	 * Creates a new command.
	 */
	public DInheritance() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		List<NSOFObject> inheritances = new ArrayList<NSOFObject>();
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFArray arr = (NSOFArray) decoder.decode(data);
		for (NSOFObject entry : arr.getValue()) {
			inheritances.add(entry);
		}
		setInheritances(inheritances);
	}

	/**
	 * Get the inheritances.
	 * 
	 * @return the inheritances.
	 */
	public List<NSOFObject> getInheritances() {
		return inheritances;
	}

	/**
	 * Set the inheritances.
	 * 
	 * @param inheritances
	 *            the inheritances.
	 */
	protected void setInheritances(List<NSOFObject> inheritances) {
		this.inheritances = inheritances;
	}

}
