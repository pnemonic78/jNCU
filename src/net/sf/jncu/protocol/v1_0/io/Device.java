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
package net.sf.jncu.protocol.v1_0.io;

/**
 * Device information.
 * 
 * @author moshew
 */
public class Device {

	/** Desktop path type. */
	public static final int kDesktop = 0;
	/** File path type type. */
	public static final int kFile = 1;
	/** Folder path type. */
	public static final int kFolder = 2;
	/** Disk path type. */
	public static final int kDisk = 3;

	/** Floppy disk device. */
	public static final int kFloppyDisk = 0;
	/** Hard disk drive device. */
	public static final int kHardDisk = 1;
	/** CD-ROM disc device. */
	public static final int kCdRomDisk = 2;
	/** Network drive device. */
	public static final int kNetDrive = 3;

	/**
	 * Creates a new device.
	 */
	public Device() {
		super();
	}

}
