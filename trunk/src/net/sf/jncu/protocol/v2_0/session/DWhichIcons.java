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
package net.sf.jncu.protocol.v2_0.session;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * This command is used to customise the set of icons shown on the Newton. The
 * <tt>iconMask</tt> is a long that indicates which icons should be shown. For
 * example, to show all icons you would use this:
 * <code>kBackupIcon + kSyncIcon + kInstallIcon + kRestoreIcon + kImportIcon + kKeyboardIcon</code>
 * Where:
 * <ul>
 * <li>kBackupIcon = 1
 * <li>kRestoreIcon = 2
 * <li>kInstallIcon = 4
 * <li>kImportIcon = 8
 * <li>kSyncIcon = 16
 * <li>kKeyboardIcon = 32
 * </ul>
 * 
 * <pre>
 * 'wicn'
 * length
 * iconMask
 * </pre>
 * 
 * @author moshew
 */
public class DWhichIcons extends DockCommandToNewton {

	/** <tt>kDWhichIcons</tt> */
	public static final String COMMAND = "wicn";

	private int icons;

	/**
	 * "Backup" icon.<br>
	 * <tt>kBackupIcon</tt>
	 */
	public static final int BACKUP = 1;
	/**
	 * "Restore" icon.<br>
	 * <tt>kRestoreIcon</tt>
	 */
	public static final int RESTORE = 2;
	/**
	 * "Install package" icon.<br>
	 * <tt>kInstallIcon</tt>
	 */
	public static final int INSTALL = 4;
	/**
	 * "Import" icon.<br>
	 * <tt>kImportIcon</tt>
	 */
	public static final int IMPORT = 8;
	/**
	 * "Synchronise" icon.<br>
	 * <tt>kSyncIcon</tt>
	 */
	public static final int SYNC = 16;
	/**
	 * "Keyboard" icon.<br>
	 * <tt>kKeyboardIcon</tt>
	 */
	public static final int KEYBOARD = 32;
	/**
	 * All icons.
	 */
	public static final int ALL = BACKUP | IMPORT | INSTALL | KEYBOARD | RESTORE | SYNC;

	/**
	 * Creates a new command.
	 */
	public DWhichIcons() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		htonl(getIcons(), data);
	}

	/**
	 * Get the icons.
	 * 
	 * @return the icons.
	 */
	public int getIcons() {
		return icons;
	}

	/**
	 * Set the icons.
	 * 
	 * @param icons
	 *            the icons.
	 */
	public void setIcons(int icons) {
		this.icons = icons;
	}

	/**
	 * Add an icon.
	 * 
	 * @param icon
	 *            the icon.
	 */
	public void addIcon(int icon) {
		switch (icon) {
		case BACKUP:
		case RESTORE:
		case INSTALL:
		case IMPORT:
		case SYNC:
		case KEYBOARD:
			icons |= icon;
			break;
		default:
			throw new IllegalArgumentException("unknown icon");
		}
	}
}
