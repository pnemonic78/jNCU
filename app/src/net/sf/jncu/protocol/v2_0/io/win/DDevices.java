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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.protocol.BaseDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.io.Device;

/**
 * This command returns an array of frames describing devices. These are the
 * devices which will appear in the devices pop-up in the Windows file browsing
 * dialog. Each frame in the array should look like this:<br>
 * <code>{<br>
 * &nbsp;&nbsp;name: "c:mydisk",<br>
 * &nbsp;&nbsp;disktype: 1<br>
 * }</code><br>
 * where (floppy = 0, hardDrive = 1, cdRom = 2, netDrive = 3). The icon is
 * displayed in the pop-up. This may not be possible in which case this slot
 * will be optional.
 * 
 * <pre>
 * 'devs'
 * length
 * array
 * </pre>
 * 
 * @author moshew
 */
public class DDevices extends BaseDockCommandToNewton {

	/** <tt>kDDevices</tt> */
	public static final String COMMAND = "devs";

	/**
	 * Creates a new command.
	 */
	public DDevices() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		File[] roots = File.listRoots();
		final List<Device> devices = new ArrayList<Device>();

		if (roots != null) {
			Device device;
			for (File root : roots) {
				device = new Device(root);
				devices.add(device);
			}
		}

		NSOFObject[] paths = new NSOFObject[devices.size()];
		int i = 0;
		for (Device device : devices) {
			paths[i++] = device.toFrame();
		}
		NSOFArray arr = new NSOFPlainArray(paths);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(arr, data);
	}
}
