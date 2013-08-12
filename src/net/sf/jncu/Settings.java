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

	/** Property key for backup path. */
	private static final String KEY_BACKUP_PATH = "jncu.backup.path";
	/** Property key for port id. */
	private static final String KEY_PORT_ID = "jncu.port.id";
	/** Property key for port speed. */
	private static final String KEY_PORT_SPEED = "jncu.port.baud";
	/** Property key for listening. */
	private static final String KEY_LISTEN = "jncu.listen";
	/** Backup files folder. */
	private static final String FOLDER = "jNCU";

	private final Collection<String> portIds = new ArrayList<String>();
	private String portId;
	private int baud;
	private boolean listen;
	private File backupFolder;

	/**
	 * Constructs new settings.
	 */
	public Settings() {
		super();
		rescanPorts();
		load();
	}

	private void load() {
		File userFolder = new File(System.getProperty("user.home"));
		File jncuFolder = new File(userFolder, FOLDER);

		Preferences prefs = Preferences.getInstance();
		setBackupFolder(prefs.get(KEY_BACKUP_PATH, jncuFolder.getPath()));
		setPortIdentifier(prefs.get(KEY_PORT_ID));
		setPortSpeed(Integer.parseInt(prefs.get(KEY_PORT_SPEED,
				Integer.toString(MNPSerialPort.BAUD_38400))));
		setListen(Boolean.parseBoolean(prefs.get(KEY_LISTEN,
				Boolean.TRUE.toString())));
	}

	public Collection<String> getPorts() {
		return portIds;
	}

	public void rescanPorts() {
		portIds.clear();

		CommPorts discoverer = new CommPorts();
		portIds.addAll(discoverer.getPortNames());
	}

	/**
	 * @return the portSelected
	 */
	public String getPortIdentifier() {
		return portId;
	}

	/**
	 * @param portName
	 *            the portSelected to set
	 */
	public void setPortIdentifier(String portName) {
		this.portId = null;
		for (String portId : portIds) {
			if (portId.equals(portName)) {
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
		setBackupFolder(new File(backupFolder));
	}

	public void save() {
		Preferences prefs = Preferences.getInstance();
		prefs.set(KEY_BACKUP_PATH, getBackupFolder().getPath());
		prefs.set(KEY_LISTEN, Boolean.toString(isListen()));
		prefs.set(KEY_PORT_ID, getPortIdentifier());
		prefs.set(KEY_PORT_SPEED, Integer.toString(getPortSpeed()));
		prefs.save();
	}
}
