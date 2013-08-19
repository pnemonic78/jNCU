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

	/** Backup files folder. */
	private static final String FOLDER = "jNCU";

	private File mainFolder;
	private Communications comm;
	private General general;
	private Security sec;
	private AutoDock dock;

	/**
	 * Constructs new settings.
	 */
	public Settings() {
		super();

		File userFolder = new File(System.getProperty("user.home"));
		this.mainFolder = new File(userFolder, FOLDER);
		this.comm = new Communications();
		this.general = new General();
		this.sec = new Security();
		this.dock = new AutoDock();
		load();
	}

	protected void load() {
		Preferences prefs = Preferences.getInstance();
		comm.read(prefs);
		general.read(prefs);
		sec.read(prefs);
		dock.read(prefs);
	}

	public void save() {
		Preferences prefs = Preferences.getInstance();
		comm.write(prefs);
		general.write(prefs);
		sec.write(prefs);
		dock.write(prefs);
		prefs.save();
	}

	/**
	 * Settings category.
	 * 
	 * @author Moshe
	 */
	protected interface SettingsCategory {
		/**
		 * Read the settings from the preferences.
		 * 
		 * @param prefs
		 *            the preferences.
		 */
		public void read(Preferences prefs);

		/**
		 * Write the settings to the preferences.
		 * 
		 * @param prefs
		 *            the preferences.
		 */
		public void write(Preferences prefs);
	}

	/**
	 * Communications settings.
	 * 
	 * @author Moshe
	 */
	public class Communications implements SettingsCategory {
		/** Property key for port id. */
		private static final String KEY_PORT_ID = "jncu.port.id";
		/** Property key for port speed. */
		private static final String KEY_PORT_SPEED = "jncu.port.baud";
		/** Property key for listening. */
		private static final String KEY_LISTEN = "jncu.listen";

		private final Collection<String> portIds = new ArrayList<String>();
		private String portId;
		private int baud;
		private boolean listen;

		public Communications() {
			rescanPorts();
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

		@Override
		public void read(Preferences prefs) {
			setPortIdentifier(prefs.get(KEY_PORT_ID));
			setPortSpeed(Integer.parseInt(prefs.get(KEY_PORT_SPEED,
					Integer.toString(MNPSerialPort.BAUD_38400))));
			setListen(prefs.getBoolean(KEY_LISTEN, true));
		}

		@Override
		public void write(Preferences prefs) {
			prefs.set(KEY_LISTEN, isListen());
			prefs.set(KEY_PORT_ID, getPortIdentifier());
			prefs.set(KEY_PORT_SPEED, Integer.toString(getPortSpeed()));
		}
	}

	public Communications getCommunications() {
		return comm;
	}

	/**
	 * General settings.
	 * 
	 * @author Moshe
	 */
	public class General implements SettingsCategory {
		/** Property key for backup path. */
		private static final String KEY_BACKUP_PATH = "jncu.backup.path";

		private File backupFolder;

		public General() {
			this.backupFolder = new File(mainFolder, "Backups");
			backupFolder.mkdirs();
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

		@Override
		public void read(Preferences prefs) {
			setBackupFolder(prefs.get(KEY_BACKUP_PATH, getBackupFolder()
					.getPath()));
		}

		@Override
		public void write(Preferences prefs) {
			prefs.set(KEY_BACKUP_PATH, getBackupFolder().getPath());
		}
	}

	public General getGeneral() {
		return general;
	}

	/**
	 * Security settings.
	 * 
	 * @author Moshe
	 */
	public class Security implements SettingsCategory {
		/** Property key for old password. */
		private static final String KEY_PASSWORD_OLD = "jncu.password.old";
		/** Property key for new password. */
		private static final String KEY_PASSWORD_NEW = "jncu.password.new";

		private transient String passwordOld;
		private transient String passwordNew;

		public Security() {
		}

		public String getPasswordOld() {
			return passwordOld;
		}

		public void setPasswordOld(String password) {
			this.passwordOld = password;
		}

		public String getPasswordNew() {
			return passwordNew;
		}

		public void setPasswordNew(String password) {
			this.passwordNew = password;
		}

		@Override
		public void read(Preferences prefs) {
			// TODO decrypt the password
			setPasswordOld(prefs.get(KEY_PASSWORD_OLD));
			// TODO decrypt the password
			setPasswordNew(prefs.get(KEY_PASSWORD_NEW));
		}

		@Override
		public void write(Preferences prefs) {
			// TODO encrypt the password
			prefs.set(KEY_PASSWORD_OLD, getPasswordOld());
			// TODO encrypt the password
			prefs.set(KEY_PASSWORD_NEW, getPasswordNew());
		}
	}

	public Security getSecurity() {
		return sec;
	}

	/**
	 * Automatic docking (Auto Dock) settings.
	 * 
	 * @author Moshe
	 */
	public class AutoDock implements SettingsCategory {
		/** Property key for automatic backup. */
		private static final String KEY_DOCK_BACKUP = "jncu.dock.backup";
		/** Property key for automatic selective backup. */
		private static final String KEY_DOCK_BACKUP_SELECTIVE = "jncu.dock.backup.selective";
		/** Property key for automatic synchronize. */
		private static final String KEY_DOCK_SYNC = "jncu.dock.sync";

		private boolean backup;
		private boolean backupSelective;
		private boolean sync;

		public AutoDock() {
		}

		public boolean isBackup() {
			return backup;
		}

		public void setBackup(boolean backup) {
			this.backup = backup;
		}

		/**
		 * @return the backupSelective
		 */
		public boolean isBackupSelective() {
			return backupSelective;
		}

		/**
		 * @param backupSelective
		 *            the backupSelective to set
		 */
		public void setBackupSelective(boolean backupSelective) {
			this.backupSelective = backupSelective;
		}

		/**
		 * @return the sync
		 */
		public boolean isSync() {
			return sync;
		}

		/**
		 * @param sync
		 *            the sync to set
		 */
		public void setSync(boolean sync) {
			this.sync = sync;
		}

		@Override
		public void read(Preferences prefs) {
			setBackup(prefs.getBoolean(KEY_DOCK_BACKUP, false));
			setBackupSelective(prefs.getBoolean(KEY_DOCK_BACKUP_SELECTIVE,
					false));
			setSync(prefs.getBoolean(KEY_DOCK_SYNC, false));
		}

		@Override
		public void write(Preferences prefs) {
			prefs.set(KEY_DOCK_BACKUP, isBackup());
			prefs.set(KEY_DOCK_BACKUP_SELECTIVE, isBackup());
			prefs.set(KEY_DOCK_SYNC, isBackup());
		}
	}

	public AutoDock getAutoDock() {
		return dock;
	}
}
