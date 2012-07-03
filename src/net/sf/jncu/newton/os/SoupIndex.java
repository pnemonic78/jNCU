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

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Soup index.
 * <code>{structure='slot, index=159384445, path='mtgStartDate, type='int}</code>
 * 
 * @author mwaisberg
 * 
 */
public class SoupIndex {

	public static final NSOFSymbol SLOT_STRUCTURE = new NSOFSymbol("structure");
	public static final NSOFSymbol SLOT_INDEX = new NSOFSymbol("index");
	public static final NSOFSymbol SLOT_PATH = new NSOFSymbol("path");
	public static final NSOFSymbol SLOT_TYPE = new NSOFSymbol("type");

	private final NSOFFrame frame = new NSOFFrame();

	/**
	 * Creates a new soup index.
	 */
	public SoupIndex() {
		super();
	}

	/**
	 * Decode the index frame.
	 * 
	 * @param frame
	 *            the frame.
	 */
	public void decodeFrame(NSOFFrame frame) {
		this.frame.putAll(frame);
	}

	/**
	 * Get the index frame.
	 * 
	 * @return the frame.
	 */
	public NSOFFrame toFrame() {
		return frame;
	}

	/**
	 * Get the index structure.
	 * 
	 * @return the structure.
	 */
	public NSOFSymbol getStructure() {
		return (NSOFSymbol) frame.get(SLOT_STRUCTURE);
	}

	/**
	 * Set the index structure.
	 * 
	 * @param structure
	 *            the structure.
	 */
	public void setStructure(NSOFSymbol structure) {
		frame.put(SLOT_STRUCTURE, structure);
	}

	/**
	 * Get the index structure.
	 * 
	 * @return the structure.
	 */
	public int getIndex() {
		NSOFImmediate imm = (NSOFImmediate) frame.get(SLOT_INDEX);
		if (imm != null) {
			return imm.getValue();
		}
		return 0;
	}

	/**
	 * Set the index.
	 * 
	 * @param index
	 *            the index.
	 */
	public void setIndex(int index) {
		setIndex(new NSOFInteger(index));
	}

	/**
	 * Set the index.
	 * 
	 * @param index
	 *            the index.
	 */
	public void setIndex(NSOFInteger index) {
		frame.put(SLOT_INDEX, index);
	}

	/**
	 * Get the index path.
	 * 
	 * @return the path.
	 */
	public NSOFSymbol getPath() {
		return (NSOFSymbol) frame.get(SLOT_PATH);
	}

	/**
	 * Set the index path.
	 * 
	 * @param path
	 *            the path.
	 */
	public void setPath(NSOFSymbol path) {
		frame.put(SLOT_PATH, path);
	}

	/**
	 * Get the index type.
	 * 
	 * @return the type.
	 */
	public NSOFSymbol getType() {
		return (NSOFSymbol) frame.get(SLOT_TYPE);
	}

	/**
	 * Set the index type.
	 * 
	 * @param type
	 *            the type.
	 */
	public void setType(NSOFSymbol type) {
		frame.put(SLOT_TYPE, type);
	}
}
