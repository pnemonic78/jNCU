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
 * The contents of a jNCU archive usually have the following entries structure:
 * <ul>
 * <li>MANIFEST.MF
 * <li>stores
 * <ul>
 * <li><tt>Internal Flash</tt>
 * <li>(name of external flash store)
 * </ul>
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
