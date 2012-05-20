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

import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;

/**
 * @author moshe
 * 
 */
public class SoupEntry implements Comparable<SoupEntry> {

	private int id;
	private NSOFObject value;

	/**
	 * Creates a new entry.
	 */
	public SoupEntry() {
		super();
	}

	/**
	 * Get the entry ID.
	 * 
	 * @return the ID.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Set the entry ID.
	 * 
	 * @param id
	 *            the ID.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Get the entry value.
	 * 
	 * @return the value.
	 */
	public NSOFObject getValue() {
		return value;
	}

	/**
	 * Set the entry value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(NSOFObject value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public int compareTo(SoupEntry that) {
		int c = this.id - that.id;
		if (c != 0)
			return c;
		if (this.value == null) {
			if (that.value == null)
				return 0;
			return -1;
		}
		if (that.value == null)
			return +1;
		if ((this.value instanceof NSOFString) && (that.value instanceof NSOFString)) {
			NSOFString sThis = (NSOFString) this.value;
			NSOFString sThat = (NSOFString) that.value;

			return sThis.compareTo(sThat);
		}
		return 0;
	}
}
