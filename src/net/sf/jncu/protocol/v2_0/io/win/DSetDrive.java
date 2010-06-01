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
package net.sf.jncu.protocol.v2_0.io.win;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDSetDrive</tt><br>
 * This command asks the desktop to change the drive on the desktop and set the
 * directory to the current directory for that drive. The string contains the
 * drive letter followed by a colon e.g. "<tt>C:</tt>". Windows only.
 * 
 * <pre>
 * 'sdrv'
 * length
 * drive string
 * </pre>
 * 
 * @author moshew
 */
public class DSetDrive extends DockCommandFromNewton {

	public static final String COMMAND = "sdrv";

	private String drive;

	public DSetDrive() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		setDrive(readString(data));
	}

	/**
	 * Get the drive.
	 * 
	 * @return the drive.
	 */
	public String getDrive() {
		return drive;
	}

	/**
	 * Set the drive.
	 * 
	 * @param drive
	 *            the drive.
	 */
	protected void setDrive(String drive) {
		this.drive = drive;
	}

}