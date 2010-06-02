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
package net.sf.jncu.protocol.v2_0.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.newton.stream.NSOFArray;
import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.newton.stream.NSOFPlainArray;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v1_0.io.Device;

/**
 * This command returns an array of information that's used to display a
 * standard file like dialog box on the Newton. Each element of the array is a
 * frame describing one file, folder or device. The individual frame would look
 * like this:<br>
 * <code>{<br>
 * &nbsp;&nbsp;name: "whatever",<br>
 * &nbsp;&nbsp;type: kFolder,<br>
 * &nbsp;&nbsp;disktype: 0,		// optional if type = disk<br>
 * &nbsp;&nbsp;whichVol: 0,		// optional if name is on the desktop<br>
 * &nbsp;&nbsp;alias: nil,		// optional if it's an alias<br>
 * }</code>
 * <br>
 * The possible values for type are desktop, file, folder or disk (0, 1, 2, 3).
 * The frames should be in the order in the array that they are to be displayed
 * in on the Newton. For example, the array might look like this:<br>
 * <code>[{name: "Applications", type: kFolder},<br>
 * &nbsp;{name: "important info", type: kFile},<br>
 * &nbsp;{name: "System", type: kFolder}]</code>
 * <p>
 * If the type is a disk, then the frame will have an additional slot
 * <tt>disktype</tt> with the values (floppy = 0, hardDrive = 1, cdRom = 2,
 * netDrive = 3). Also, if the current location is the desktop, there is an
 * additional slot <tt>whichvol</tt> to indicate the location of the individual
 * files, folders and disks with the values <tt>0</tt> for disks and a negative
 * number for the <tt>volRefNum</tt> for files and folders on the desktop.
 * <p>
 * If the item is an alias there is an <tt>alias</tt> slot. The existence of
 * this slot indicates that the item is an alias.<br>
 * A Windows alias could be a "shortcut", or a "NTFS symbolic link". A
 * Unix/Linux/Posix alias is a link (as created by the "ln" command).
 * 
 * <pre>
 * 'file'
 * length
 * file/folder array
 * </pre>
 * 
 * @see #kCdRomDisk
 * @see #kDesktop
 * @see #kDisk
 * @see #kFile
 * @see #kFloppyDisk
 * @see #kFolder
 * @see #kHardDisk
 * @see #kNetDrive
 * @author moshew
 */
public class DFilesAndFolders extends DockCommandToNewton {

	/** <tt>kDFilesAndFolders</tt> */
	public static final String COMMAND = "file";

	private File folder;

	/**
	 * Creates a new command.
	 */
	public DFilesAndFolders() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		File[] files = getFolder().listFiles();
		List<Device> devices = new ArrayList<Device>();

		if (files != null) {
			Device device;
			for (File file : files) {
				if (!file.isHidden()) {
					device = new Device(file);
					devices.add(device);
				}
			}
		}

		NSOFObject[] paths = new NSOFObject[devices.size()];
		int i = 0;
		for (Device device : devices) {
			paths[i++] = device.toFrame();
		}
		NSOFArray arr = new NSOFPlainArray(paths);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(arr, data);
	}

	/**
	 * Get the root folder.
	 * 
	 * @return the folder.
	 */
	public File getFolder() {
		return folder;
	}

	/**
	 * Set the root folder.
	 * 
	 * @param folder
	 *            the folder.
	 */
	public void setFolder(File folder) {
		this.folder = folder;
	}

}
