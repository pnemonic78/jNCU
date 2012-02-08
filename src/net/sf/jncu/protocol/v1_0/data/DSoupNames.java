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

import net.sf.jncu.newton.stream.NSOFArray;
import net.sf.jncu.newton.stream.NSOFDecoder;
import net.sf.jncu.newton.stream.NSOFImmediate;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.protocol.DockCommandFromNewton;

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
public class DSoupNames extends DockCommandFromNewton {

	/** <tt>kDSoupNames</tt> */
	public static final String COMMAND = "soup";

	private List<String> names;
	private List<Integer> signatures;

	/**
	 * Creates a new command.
	 */
	public DSoupNames() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		setNames(null);
		setSignatures(null);

		NSOFDecoder decoder = new NSOFDecoder();
		NSOFArray arr = (NSOFArray) decoder.decode(data);
		List<String> names = new ArrayList<String>();
		NSOFObject[] entries = arr.getValue();
		for (NSOFObject entry : entries) {
			names.add(((NSOFString) entry).getValue());
		}
		setNames(names);

		decoder = new NSOFDecoder();
		arr = (NSOFArray) decoder.decode(data);
		List<Integer> signatures = new ArrayList<Integer>();
		entries = arr.getValue();
		for (NSOFObject entry : entries) {
			signatures.add(((NSOFImmediate) entry).getValue());
		}
		setSignatures(signatures);
	}

	/**
	 * Get the soup names.
	 * 
	 * @return the names.
	 */
	public List<String> getNames() {
		return names;
	}

	/**
	 * Set the soup names.
	 * 
	 * @param names
	 *            the names.
	 */
	protected void setNames(List<String> names) {
		this.names = names;
	}

	/**
	 * Get the soup signatures.
	 * 
	 * @return the signatures.
	 */
	public List<Integer> getSignatures() {
		return signatures;
	}

	/**
	 * Set the soup signatures.
	 * 
	 * @param signatures
	 *            the signatures.
	 */
	protected void setSignatures(List<Integer> signatures) {
		this.signatures = signatures;
	}

}
