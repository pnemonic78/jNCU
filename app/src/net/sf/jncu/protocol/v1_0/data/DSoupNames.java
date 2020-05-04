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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

/**
 * This command is sent in response to a <tt>kDGetSoupNames</tt> command. It
 * returns the names and signatures of all the soups in the current store.
 * 
 * <pre>
 * 'soup'
 * length
 * array of string names
 * array of corresponding soup signatures
 * </pre>
 */
public class DSoupNames extends BaseDockCommandFromNewton {

	/** <tt>kDSoupNames</tt> */
	public static final String COMMAND = "soup";

	private final List<Soup> soups = new ArrayList<Soup>();

	/**
	 * Creates a new command.
	 */
	public DSoupNames() {
		super(COMMAND);
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
		setSoups(null);

		NSOFDecoder decoder = new NSOFDecoder();
		NSOFArray arr = (NSOFArray) decoder.inflate(data);
		NSOFObject[] entries = arr.getValue();
		NSOFObject entry;

		final int length = entries.length;
		List<Soup> soups = new ArrayList<Soup>(length);
		Soup soup;

		for (int i = 0; i < length; i++) {
			entry = entries[i];
			soup = new Soup(((NSOFString) entry).getValue());
			soups.add(soup);
		}

		decoder = new NSOFDecoder();
		arr = (NSOFArray) decoder.inflate(data);
		entries = arr.getValue();
		for (int i = 0; i < length; i++) {
			entry = entries[i];
			soup = soups.get(i);
			soup.setSignature(((NSOFImmediate) entry).getValue());
		}

		setSoups(soups);
	}

	/**
	 * Get the soups.
	 * 
	 * @return the soups.
	 */
	public List<Soup> getSoups() {
		return soups;
	}

	/**
	 * Set the soup names.
	 * 
	 * @param names
	 *            the names.
	 */
	protected void setSoups(List<Soup> soups) {
		this.soups.clear();
		if (soups != null)
			this.soups.addAll(soups);
	}
}
