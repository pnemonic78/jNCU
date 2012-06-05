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
package net.sf.jncu.newton.os;

import java.util.Set;
import java.util.TreeSet;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.fdil.contrib.NSOFSoupName;

/**
 * Soup information.
 * 
 * @author moshew
 */
public class Soup {

	protected static final NSOFSymbol SLOT_NAME = new NSOFSymbol("name");
	protected static final NSOFSymbol SLOT_SIGNATURE = new NSOFSymbol("signature");

	private String name;
	private int signature;
	private NSOFArray index;
	private final Set<SoupEntry> entries = new TreeSet<SoupEntry>();

	/**
	 * Creates a new soup.
	 */
	public Soup() {
		super();
		setIndex(new NSOFPlainArray());
	}

	/**
	 * Get the name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the signature.
	 * 
	 * @return the signature.
	 */
	public int getSignature() {
		return signature;
	}

	/**
	 * Set the signature.
	 * 
	 * @param signature
	 *            the signature.
	 */
	public void setSignature(int signature) {
		this.signature = signature;
	}

	/**
	 * Get the index description.
	 * 
	 * @return the index.
	 */
	public NSOFArray getIndex() {
		return index;
	}

	/**
	 * Set the index description.
	 * 
	 * @param index
	 *            the index.
	 */
	public void setIndex(NSOFArray index) {
		this.index = index;
	}

	/**
	 * Get the entries.
	 * 
	 * @return the entries.
	 */
	public Set<SoupEntry> getEntries() {
		return entries;
	}

	/**
	 * Set the entries.
	 * 
	 * @param entries
	 *            the entries.
	 */
	public void setIds(Set<SoupEntry> entries) {
		this.entries.clear();
		if (entries != null)
			this.entries.addAll(entries);
	}

	/**
	 * Add an entry.
	 * 
	 * @param entry
	 *            the entry.
	 */
	public void addEntry(SoupEntry entry) {
		this.entries.add(entry);
	}

	/**
	 * Get the frame.
	 * 
	 * @return the frame.
	 */
	public NSOFFrame toFrame() {
		NSOFFrame frame = new NSOFFrame();
		frame.put(SLOT_NAME, new NSOFSoupName(getName()));
		if (getSignature() != 0)
			frame.put(SLOT_SIGNATURE, new NSOFInteger(getSignature()));
		return frame;
	}

	/**
	 * Decode the frame.
	 * 
	 * @param frame
	 *            the frame.
	 */
	public void decodeFrame(NSOFFrame frame) {
		setName(null);
		setSignature(0);

		NSOFObject value = frame.get(SLOT_NAME);
		if (!NSOFImmediate.isNil(value)) {
			NSOFString s = (NSOFString) value;
			setName(s.getValue());
		}

		value = frame.get(SLOT_SIGNATURE);
		if (!NSOFImmediate.isNil(value)) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setSignature(imm.getValue());
		}
	}
}
