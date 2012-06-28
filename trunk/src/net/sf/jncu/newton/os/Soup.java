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
 * Newton soup.
 * <p>
 * Typical soup information frame might look like this: <br>
 * {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;'name="Calendar",<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;'signature=-23730660,<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;'soupDef={<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'userName="calendar
 * meetings",<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'userDescr="non-repeating
 * meetings in the calendar",<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'name="Calendar",<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'ownerAppName="Dates",<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'indexes=[{'structure='slot,
 * 'path='mtgStartDate, 'type='int}, {'structure='slot, 'path='mtgAlarm,
 * 'type='int}],<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'ownerApp='calendar<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;},<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;'NCKLastBackupTime=57030835<br>
 * }<br>
 * 
 * @author moshew
 */
public class Soup {

	protected static final NSOFSymbol SLOT_NAME = new NSOFSymbol("name");
	protected static final NSOFSymbol SLOT_SIGNATURE = new NSOFSymbol("signature");
	protected static final NSOFSymbol SLOT_SOUP_DEF = new NSOFSymbol("soupDef");
	protected static final NSOFSymbol SLOT_USER_NAME = new NSOFSymbol("userName");
	protected static final NSOFSymbol SLOT_USER_DESCRIPTION = new NSOFSymbol("userDescr");
	protected static final NSOFSymbol SLOT_OWNER_APP_NAME = new NSOFSymbol("ownerAppName");
	protected static final NSOFSymbol SLOT_INDEXES = new NSOFSymbol("indexes");
	protected static final NSOFSymbol SLOT_STRUCTURE = new NSOFSymbol("structure");
	protected static final NSOFSymbol SLOT_PATH = new NSOFSymbol("path");
	protected static final NSOFSymbol SLOT_TYPE = new NSOFSymbol("type");
	protected static final NSOFSymbol SLOT_OWNER_APP = new NSOFSymbol("ownerApp");
	protected static final NSOFSymbol SLOT_BACKUP = new NSOFSymbol("NCKLastBackupTime");

	private NSOFFrame info;
	private final Set<SoupEntry> entries = new TreeSet<SoupEntry>();

	/**
	 * Creates a new soup.
	 */
	public Soup() {
		super();
	}

	/**
	 * Get the name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		NSOFObject value = getInformation().get(SLOT_NAME);
		if (NSOFImmediate.isNil(value)) {
			value = getDefinition().get(SLOT_NAME);
		}
		if (!NSOFImmediate.isNil(value)) {
			NSOFString s = (NSOFString) value;
			return s.getValue();
		}
		return null;
	}

	/**
	 * Set the name.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		getInformation().put(SLOT_NAME, new NSOFString(name));
	}

	/**
	 * Get the signature.
	 * 
	 * @return the signature.
	 */
	public int getSignature() {
		NSOFObject value = getInformation().get(SLOT_SIGNATURE);
		if (!NSOFImmediate.isNil(value)) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setSignature(imm.getValue());
		}
		return 0;
	}

	/**
	 * Set the signature.
	 * 
	 * @param signature
	 *            the signature.
	 */
	public void setSignature(int signature) {
		getInformation().put(SLOT_SIGNATURE, new NSOFInteger(signature));
	}

	/**
	 * Get the indexes description.
	 * 
	 * @return the indexes.
	 */
	public NSOFArray getIndexes() {
		NSOFArray indexes = (NSOFArray) getDefinition().get(SLOT_INDEXES);
		if (indexes == null) {
			indexes = new NSOFPlainArray();
			setIndexes(indexes);
		}
		return indexes;
	}

	/**
	 * Set the indexes description.
	 * 
	 * @param indexes
	 *            the indexes.
	 */
	public void setIndexes(NSOFArray indexes) {
		if (indexes == null)
			indexes = new NSOFPlainArray();
		getDefinition().put(SLOT_INDEXES, indexes);
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
	public void setEntries(Set<SoupEntry> entries) {
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
		NSOFFrame info = new NSOFFrame();
		info.putAll(frame);
		setInformation(info);
	}

	/**
	 * Get the soup information.
	 * 
	 * @return the information.
	 */
	public NSOFFrame getInformation() {
		if (info == null)
			info = new NSOFFrame();
		return info;
	}

	/**
	 * Set the soup information.
	 * 
	 * @param info
	 *            the information.
	 */
	public void setInformation(NSOFFrame info) {
		this.info = info;
	}

	/**
	 * Get the soup definition.
	 * 
	 * @return the definition.
	 */
	public NSOFFrame getDefinition() {
		NSOFFrame soupDef = (NSOFFrame) getInformation().get(SLOT_SOUP_DEF);
		if (soupDef == null) {
			soupDef = new NSOFFrame();
		}
		return soupDef;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
