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
package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFBoolean;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Application name.
 * 
 * @author mwaisberg
 * 
 */
public class AppName extends NSOFFrame implements Comparable<AppName> {

	public static final NSOFSymbol SLOT_NAME = new NSOFSymbol("name");
	public static final NSOFSymbol SLOT_SOUPS = new NSOFSymbol("soups");
	public static final NSOFSymbol SLOT_PACKAGES = new NSOFSymbol("packages");

	/**
	 * If there are packages installed, a "Packages" item is listed.
	 * 
	 * @see #CLASS_PACKAGES
	 */
	public static final String NAME_PACKAGES = "Packages";
	/**
	 * Application name for soups that don't have an associated application,
	 * there's an "Other information" entry.
	 * 
	 * @see #CLASS_OTHER
	 */
	public static final String NAME_OTHER = "Other information";
	/**
	 * "System information" includes the system and directory soups.
	 * 
	 * @see #CLASS_SYSTEM
	 */
	public static final String NAME_SYSTEM = "System information";
	/**
	 * If a card is present and has a backup there will be a "Card backup" item.
	 * 
	 * @see #CLASS_BACKUP
	 */
	public static final String NAME_BACKUP = "Card backup";

	/**
	 * Object class slot to indicate that this array entry is for the packages.
	 * 
	 * @see #NAME_PACKAGES
	 * */
	public static final NSOFSymbol CLASS_PACKAGES = new NSOFSymbol("packageFrame");
	/**
	 * Object class slot to indicate that this array entry is for other
	 * information.
	 * 
	 * @see #NAME_OTHER
	 * */
	public static final NSOFSymbol CLASS_OTHER = new NSOFSymbol("otherFrame");
	/**
	 * Object class slot to indicate that this array entry is for the system
	 * information.
	 * 
	 * @see #NAME_SYSTEM
	 * */
	public static final NSOFSymbol CLASS_SYSTEM = new NSOFSymbol("systemFrame");
	/**
	 * Object class slot to indicate that this array entry is for the card
	 * backup.
	 * 
	 * @see #NAME_BACKUP
	 * */
	public static final NSOFSymbol CLASS_BACKUP = new NSOFSymbol("backupFrame");

	/**
	 * Creates a new name.
	 */
	public AppName() {
		super();
	}

	/**
	 * Creates a new name.
	 * 
	 * @param frame
	 *            the source frame.
	 */
	public AppName(NSOFFrame frame) {
		super();
		this.putAll(frame);
	}

	/**
	 * Creates a new name.
	 * 
	 * @param name
	 *            the name.
	 */
	public AppName(String name) {
		super();
		setName(name);
	}

	/**
	 * Get the application name.
	 * 
	 * @return the name - {@code null} otherwise.
	 */
	public String getName() {
		NSOFObject o = get(SLOT_NAME);
		if (!NSOFImmediate.isNil(o))
			return ((NSOFString) o).getValue();
		return null;
	}

	/**
	 * Set the application name.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		put(SLOT_NAME, new NSOFString(name));
	}

	/**
	 * Get the application soup names.
	 * 
	 * @return the array of names - {@code null} otherwise.
	 */
	public NSOFArray getSoups() {
		NSOFObject o = get(SLOT_SOUPS);
		if (!NSOFImmediate.isNil(o))
			return (NSOFArray) o;
		return null;
	}

	/**
	 * Are packages installed?
	 * 
	 * @return {@code true} if there are packages installed.
	 */
	public boolean hasPackages() {
		NSOFImmediate imm = (NSOFImmediate) get(SLOT_PACKAGES);
		if (imm != null)
			return imm.isTrue();
		return false;
	}

	/**
	 * Set installed packages.
	 * 
	 * @param packages
	 *            {@code true} if there are packages installed.
	 */
	public void setPackages(boolean packages) {
		put(SLOT_PACKAGES, packages ? NSOFBoolean.TRUE : NSOFBoolean.FALSE);
	}

	@Override
	public int compareTo(AppName that) {
		int c = 0;
		String thisName = this.getName();
		String thatName = that.getName();
		if (thisName == null) {
			if (thatName != null) {
				return -1;
			}
		} else if (thatName == null) {
			return 1;
		} else {
			c = thisName.compareTo(thatName);
		}
		if (c != 0)
			return c;
		NSOFArray thisSoups = this.getSoups();
		NSOFArray thatSoups = that.getSoups();
		if (thisSoups == null) {
			if (thatSoups != null) {
				return -1;
			}
		} else if (thatSoups == null) {
			return 1;
		} else {
			c = thisSoups.length() - thatSoups.length();
		}
		return c;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof AppName) {
			AppName that = (AppName) obj;
			return compareTo(that) == 0;
		}
		return super.equals(obj);
	}
}
