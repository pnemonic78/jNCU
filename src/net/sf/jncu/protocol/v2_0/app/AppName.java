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
public class AppName extends NSOFFrame {

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
	 * Get the application name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		NSOFObject o = get(SLOT_NAME);
		if (!NSOFImmediate.isNil(o))
			return ((NSOFString) o).getValue();
		return null;
	}

	/**
	 * Get the application soups.
	 * 
	 * @return the soups.
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
	 * @return true if there are packages installed.
	 */
	public boolean hasPackages() {
		NSOFImmediate imm = (NSOFImmediate) get(SLOT_PACKAGES);
		if (imm != null)
			return imm.isTrue();
		return false;
	}
}
