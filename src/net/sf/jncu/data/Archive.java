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
package net.sf.jncu.data;

/**
 * Archive file for backup from the Newton device, and restore to the Newton
 * device.
 * <p>
 * The contents of a jNCU archive typically have the following structure:
 * <ul>
 * <li><tt>device.meta</tt></li>
 * <li><tt>stores</tt>
 * <ul>
 * <li><tt>Internal</tt>
 * <ul>
 * <li><tt>store.meta</tt></li>
 * <li><tt>packages</tt>
 * <ul>
 * <li>package #1</li>
 * <li>...</li>
 * <li>package #n</li>
 * </ul>
 * </li>
 * <li><tt>soups</tt>
 * <ul>
 * <li>soup name #1
 * <ul>
 * <li><tt>soup.meta</tt></li>
 * <li><tt>entries</tt></li>
 * </ul>
 * </li>
 * <li>...</li>
 * <li>soup name #n</li>
 * </ul>
 * </ul>
 * </li>
 * <li>external store #1</li>
 * <li>...</li>
 * <li>external store #n</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author mwaisberg
 * 
 */
public class Archive {

	/**
	 * Creates a new archive.
	 */
	public Archive() {
	}
}
