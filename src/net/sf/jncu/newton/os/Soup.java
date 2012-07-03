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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class Soup implements Comparable<Soup> {

	public static final NSOFSymbol SLOT_NAME = new NSOFSymbol("name");
	public static final NSOFSymbol SLOT_SIGNATURE = new NSOFSymbol("signature");
	public static final NSOFSymbol SLOT_SOUP_DEF = new NSOFSymbol("soupDef");
	public static final NSOFSymbol SLOT_USER_NAME = new NSOFSymbol("userName");
	public static final NSOFSymbol SLOT_USER_DESCRIPTION = new NSOFSymbol("userDescr");
	public static final NSOFSymbol SLOT_OWNER_APP_NAME = new NSOFSymbol("ownerAppName");
	public static final NSOFSymbol SLOT_INDEXES = new NSOFSymbol("indexes");
	public static final NSOFSymbol SLOT_STRUCTURE = new NSOFSymbol("structure");
	public static final NSOFSymbol SLOT_PATH = new NSOFSymbol("path");
	public static final NSOFSymbol SLOT_TYPE = new NSOFSymbol("type");
	public static final NSOFSymbol SLOT_OWNER_APP = new NSOFSymbol("ownerApp");
	public static final NSOFSymbol SLOT_BACKUP = new NSOFSymbol("NCKLastBackupTime");

	private String name;
	private final NSOFFrame info;
	private final Collection<SoupEntry> entries = new TreeSet<SoupEntry>();
	private final List<SoupIndex> indexes = new ArrayList<SoupIndex>();

	/**
	 * Creates a new soup.
	 * 
	 * @param name
	 *            the soup name.
	 */
	public Soup(String name) {
		super();
		this.info = new NSOFFrame();
		setName(name);
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
	private void setName(String name) {
		this.name = name;
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
	 * @return the array of indexes.
	 */
	public List<SoupIndex> getIndexes() {
		return indexes;
	}

	/**
	 * Set the indexes description.
	 * 
	 * @param indexes
	 *            the array of indexes.
	 */
	public void setIndexes(List<SoupIndex> indexes) {
		int size = (indexes == null) ? 0 : indexes.size();
		NSOFArray arr = new NSOFPlainArray(size);
		if (indexes != null) {
			SoupIndex index;
			for (int i = 0; i < size; i++) {
				index = indexes.get(i);
				arr.set(i, index.toFrame());
			}
		}
		getDefinition().put(SLOT_INDEXES, arr);
	}

	/**
	 * Set the indexes description.
	 * 
	 * @param indexes
	 *            the array of indexes.
	 */
	public void setIndexes(NSOFArray indexes) {
		if (indexes == null)
			indexes = new NSOFPlainArray();
		this.indexes.clear();

		int size = indexes.getLength();
		SoupIndex index;
		NSOFObject o;

		for (int i = 0; i < size; i++) {
			o = indexes.get(i);
			if (!NSOFImmediate.isNil(o)) {
				index = new SoupIndex();
				index.decodeFrame((NSOFFrame) o);
				this.indexes.add(index);
			}
		}

		getDefinition().put(SLOT_INDEXES, indexes);
	}

	/**
	 * Get the entries.
	 * 
	 * @return the entries.
	 */
	public Collection<SoupEntry> getEntries() {
		return entries;
	}

	/**
	 * Set the entries.
	 * 
	 * @param entries
	 *            the entries.
	 */
	public void setEntries(Collection<SoupEntry> entries) {
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
		frame.putAll(getInformation());
		frame.put(SLOT_NAME, new NSOFSoupName(getName()));
		return frame;
	}

	/**
	 * Decode the frame.
	 * 
	 * @param frame
	 *            the frame.
	 */
	public void decodeFrame(NSOFFrame frame) {
		setInformation(frame);

		if (this.name == null) {
			NSOFObject value = frame.get(SLOT_NAME);
			if (NSOFImmediate.isNil(value)) {
				NSOFFrame soupDef = (NSOFFrame) frame.get(SLOT_SOUP_DEF);
				if (soupDef != null)
					value = soupDef.get(SLOT_NAME);
			}
			if (!NSOFImmediate.isNil(value)) {
				NSOFString s = (NSOFString) value;
				this.name = s.getValue();
			}
		}
	}

	/**
	 * Get the soup information.
	 * 
	 * @return the information.
	 */
	public NSOFFrame getInformation() {
		return info;
	}

	/**
	 * Set the soup information.
	 * 
	 * @param info
	 *            the information.
	 */
	public void setInformation(NSOFFrame info) {
		this.info.clear();
		if (info != null) {
			this.info.putAll(info);

			NSOFObject value = info.get(SLOT_INDEXES);
			if (!NSOFImmediate.isNil(value)) {
				setIndexes((NSOFArray) value);
			}
		}
		this.info.put(SLOT_NAME, new NSOFString(name));
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
		return info.toString();
	}

	@Override
	public int hashCode() {
		return (name == null) ? 0 : name.hashCode();
	}

	@Override
	public int compareTo(Soup that) {
		int n = 0;
		if (this.name == null) {
			if (that.name != null) {
				return -1;
			}
		} else if (that.name == null) {
			return 1;
		} else {
			n = this.name.compareTo(that.name);
		}
		if (n != 0)
			return n;
		n = this.getSignature() - that.getSignature();
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Soup) {
			Soup that = (Soup) obj;
			return compareTo(that) == 0;
		}
		return super.equals(obj);
	}
}
