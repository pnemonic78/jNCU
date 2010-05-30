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
package net.sf.jncu;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import net.sf.jncu.cdil.mnp.CommPorts;
import net.sf.jncu.cdil.mnp.MNPSerialPort;

/**
 * Settings.
 * 
 * @author moshew
 */
public class Settings {

	private final Collection<CommPortIdentifier> portIds = new ArrayList<CommPortIdentifier>();
	private CommPortIdentifier portId;
	private int baud = MNPSerialPort.BAUD_38400;
	private boolean listen = true;
	private File backupFolder = new File(".");

	/**
	 * Constructs new settings.
	 */
	public Settings() {
		super();
		rescanPorts();
	}

	public Collection<CommPortIdentifier> getPorts() {
		return portIds;
	}

	public void rescanPorts() {
		portIds.clear();

		CommPorts discoverer = new CommPorts();
		try {
			portIds.addAll(discoverer.getPortIdentifiers(CommPortIdentifier.PORT_SERIAL));
		} catch (NoSuchPortException nspe) {
			nspe.printStackTrace();
		}
	}

	/**
	 * @return the portSelected
	 */
	public CommPortIdentifier getPortIdentifier() {
		return portId;
	}

	/**
	 * @param portId
	 *            the portSelected to set
	 */
	public void setPortIdentifier(CommPortIdentifier portId) {
		this.portId = portId;
	}

	/**
	 * @param portName
	 *            the portSelected to set
	 */
	public void setPortIdentifier(String portName) {
		this.portId = null;
		for (CommPortIdentifier portId : portIds) {
			if (portId.getName().equals(portName)) {
				this.portId = portId;
				break;
			}
		}
	}

	/**
	 * @return the portSpeed
	 */
	public int getPortSpeed() {
		return baud;
	}

	/**
	 * @param portSpeed
	 *            the portSpeed to set
	 */
	public void setPortSpeed(int portSpeed) {
		this.baud = portSpeed;
	}

	/**
	 * @return the listen
	 */
	public boolean isListen() {
		return listen;
	}

	/**
	 * @param listen
	 *            the listen to set
	 */
	public void setListen(boolean listen) {
		this.listen = listen;
	}

	/**
	 * @return the backupFolder
	 */
	public File getBackupFolder() {
		return backupFolder;
	}

	/**
	 * @param backupFolder
	 *            the backupFolder to set
	 */
	public void setBackupFolder(File backupFolder) {
		this.backupFolder = backupFolder;
	}

	/**
	 * @param backupFolder
	 *            the backupFolder to set
	 */
	public void setBackupFolder(String backupFolder) {
		this.backupFolder = new File(backupFolder);
	}
}
