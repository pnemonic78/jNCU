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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.newton.stream.NSOFDecoder;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
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

	/** <tt>kDSetDrive</tt> */
	public static final String COMMAND = "sdrv";

	private String drive;

	public DSetDrive() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		setDrive((String) null);
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFString drive = (NSOFString) decoder.decode(data);
		setDrive(drive);
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
		if (drive != null) {
			if (drive.endsWith(":")) {
				drive += File.separatorChar;
			}
		}
		this.drive = drive;
	}

	/**
	 * Set the drive.
	 * 
	 * @param drive
	 *            the drive.
	 */
	protected void setDrive(NSOFString drive) {
		if (drive == null) {
			setDrive((String) null);
		} else {
			setDrive(drive.getValue());
		}
	}
}
