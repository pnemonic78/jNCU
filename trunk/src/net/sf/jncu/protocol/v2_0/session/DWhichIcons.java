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
 * <tt>kDWhichIcons</tt><br>
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

	public static final String COMMAND = "wicn";

	private int icons;

	/** "Backup" icon. */
	public static final int kBackupIcon = 1;
	/** "Restore" icon. */
	public static final int kRestoreIcon = 2;
	/** "Install package" icon. */
	public static final int kInstallIcon = 4;
	/** "Import" icon. */
	public static final int kImportIcon = 8;
	/** "Synchronise" icon. */
	public static final int kSyncIcon = 16;
	/** "Keyboard" icon. */
	public static final int kKeyboardIcon = 32;

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

	public void addIcon(int icon) {
		switch (icon) {
		case kBackupIcon:
		case kRestoreIcon:
		case kInstallIcon:
		case kImportIcon:
		case kSyncIcon:
		case kKeyboardIcon:
			icons |= icon;
			break;
		default:
			throw new IllegalArgumentException("unknown icon");
		}
	}
}
