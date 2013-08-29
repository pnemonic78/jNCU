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
package net.sf.jncu;

import java.awt.Toolkit;
import java.util.ResourceBundle;

/**
 * jNCU resource bundle.
 * 
 * @author Moshe
 */
public class JNCUResources {

	private static final String BUNDLE = "net.sf.jncu.jncu";

	private static ResourceBundle bundle;

	private JNCUResources() {
		// Utility class, hide constructor.
	}

	/**
	 * Gets a string for the given key from this resource bundle or one of its
	 * parents.
	 * 
	 * @param key
	 *            the key name.
	 * @param defaultValue
	 *            the default value if the property is not found.
	 * @return the string for the given key.
	 */
	public static String getString(String key, String defaultValue) {
		if (key == null || key.isEmpty())
			return defaultValue;
		if (bundle == null)
			bundle = ResourceBundle.getBundle(BUNDLE);
		if (!bundle.containsKey(key))
			return defaultValue;
		String value = bundle.getString(key);
		if (value.startsWith("@")) {
			key = value.substring(1);
			return Toolkit.getProperty(key, defaultValue);
		}
		return value;
	}

	/**
	 * Gets a character for the given key from this resource bundle or one of
	 * its parents.
	 * 
	 * @param key
	 *            the key name.
	 * @param defaultValue
	 *            the default value if the property is not found.
	 * @return the character for the given key.
	 */
	public static int getChar(String key, int defaultValue) {
		String value = getString(key, null);
		if (value == null)
			return defaultValue;
		if (value.length() > 0)
			return value.codePointAt(0);
		return defaultValue;
	}

	/**
	 * Gets a character for the given key from this resource bundle or one of
	 * its parents.
	 * 
	 * @param key
	 *            the key name.
	 * @param defaultValue
	 *            the default value if the property is not found.
	 * @return the character for the given key.
	 */
	public static int getChar(String key, char defaultValue) {
		return getChar(key, (int) defaultValue);
	}
}
