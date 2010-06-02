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
package net.sf.jncu.protocol.v1_0.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.NewtonInfo;

/**
 * The name of the Newton.<br>
 * This command is sent in response to a correct <tt>kDInitiateDocking</tt>
 * command from the docker. The Newton's name is used to locate the proper
 * synchronise file. The version info includes things like machine type (e.g.
 * J1), ROM version, etc.
 * 
 * <pre>
 * 'name'
 * length
 * version info
 * name
 * </pre>
 */
public class DNewtonName extends DockCommandFromNewton {

	/** <tt>kDNewtonName</tt> */
	public static final String COMMAND = "name";

	private String name;
	private NewtonInfo info;

	/**
	 * Creates a new command.
	 */
	public DNewtonName() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		int versionInfoLength = ntohl(data);
		info = new NewtonInfo();
		info.setNewtonId(ntohl(data)); // #1
		info.setManufacturerId(ntohl(data)); // #2
		info.setMachineType(ntohl(data)); // #3
		info.setRomVersion(ntohl(data)); // #4
		info.setRomStage(ntohl(data)); // #5
		info.setRamSize(ntohl(data)); // #6
		info.setScreenHeight(ntohl(data)); // #7
		info.setScreenWidth(ntohl(data)); // #8
		info.setPatchVersion(ntohl(data)); // #9
		info.setObjectSystemVersion(ntohl(data)); // #10
		info.setInternalStoreSignature(ntohl(data)); // #11
		info.setScreenResolutionVertical(ntohl(data)); // #12
		info.setScreenResolutionHorizontal(ntohl(data)); // #13
		info.setScreenDepth(ntohl(data)); // #14

		final int versionSize = 14 * LENGTH_WORD;
		if (versionInfoLength > versionSize) {
			/**
			 * A bit field. The following two bits are defined:<br>
			 * 1 = has serial number <br>
			 * 2 = has target protocol
			 */
			int systemFlags = ntohl(data);

			if ((systemFlags & 0x1) == 0x1) {
				long serHi = ntohl(data) & 0xFFFFFFFFL;
				long serLo = ntohl(data) & 0xFFFFFFFFL;
				info.setSerialNumber((serHi << 32) | serLo);
			}
			if ((systemFlags & 0x2) == 0x2) {
				info.setTargetProtocol(ntohl(data));
			}
		}

		int nameLength = data.available();
		byte[] nameBytes = new byte[nameLength];
		data.read(nameBytes);
		try {
			while ((nameLength > 0) && (nameBytes[nameLength - 1] == 0)) {
				nameLength -= 2;
			}
			setName(new String(nameBytes, 0, nameLength, "UTF-16"));
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		}
	}

	/**
	 * Get the name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name.
	 * 
	 * @param name
	 *            the name.
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the Newton information.
	 * 
	 * @return the information.
	 */
	public NewtonInfo getInformation() {
		return info;
	}
}
