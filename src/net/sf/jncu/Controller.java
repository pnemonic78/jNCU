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

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.protocol.v2_0.app.LoadPackage;
import net.sf.jncu.protocol.v2_0.io.KeyboardInput;
import net.sf.jncu.protocol.v2_0.sync.BackupDialog;
import net.sf.jncu.ui.JNCUFrame;
import net.sf.jncu.ui.JNCUSettingsDialog;

/**
 * jNCU controller.
 * 
 * @author Moshe
 */
public class Controller {

	private final JNCUFrame frame;
	private CDLayer layer;
	private MNPPipe pipe;
	private Settings settings;
	private KeyboardInput keyboardDialog;
	private BackupDialog backupDialog;
	private LoadPackage packageLoader;
	private JFileChooser packageChooser;
	private JNCUSettingsDialog settingsDialog;

	/**
	 * Create a new controller.
	 * 
	 * @param frame
	 *            the UI frame.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 */
	public Controller(JNCUFrame frame) throws PlatformException {
		this.frame = frame;
		this.layer = CDLayer.getInstance();
		layer.startUp();
	}

	/**
	 * Connect to MNP.
	 * 
	 * @param portId
	 *            the port name.
	 * @param portSpeed
	 *            the port speed.
	 */
	public void connectMNP(String portId, int portSpeed) {
		try {
			this.pipe = layer.createMNPSerial(portId, portSpeed);
		} catch (Exception e) {
			JNCUApp.showError(frame, e);
		}
	}

	/**
	 * Synchronize.
	 */
	public void sync() {
		// TODO implement me!
	}

	/**
	 * Install package.
	 */
	public void install() {
		if (packageLoader == null) {
			packageLoader = new LoadPackage(pipe, false, frame);
		}
		if (packageChooser == null) {
			packageChooser = new JFileChooser();
			packageChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			FileFilter filter = new FileNameExtensionFilter("Newton Package", "pkg", "PKG");
			packageChooser.setFileFilter(filter);
			packageChooser.setAcceptAllFileFilterUsed(false);
		}
		int ret = packageChooser.showOpenDialog(frame);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File selectedFile = packageChooser.getSelectedFile();
			packageLoader.loadPackage(selectedFile);
		}
	}

	/**
	 * Use keyboard.
	 */
	public void keyboard() {
		if (keyboardDialog == null) {
			keyboardDialog = new KeyboardInput(pipe, frame);
		}
		keyboardDialog.getDialog().setVisible(true);
	}

	/**
	 * Backup.
	 */
	public void backupToDesktop() {
		if (backupDialog == null) {
			backupDialog = new BackupDialog(frame);
			// TODO backupDialog.setStores(stores);
			// TODO backupDialog.setApplications(apps);
		}
		backupDialog.setVisible(true);
	}

	/**
	 * Export.
	 */
	public void exportToDesktop() {
		// TODO implement me!
	}

	/**
	 * Restore.
	 */
	public void restoreToNewton() {
		// TODO implement me!
	}

	/**
	 * Import.
	 */
	public void importToNewton() {
		// TODO implement me!
	}

	/**
	 * Shut down.
	 */
	public void close() {
		stop();
		try {
			layer.shutDown();
		} catch (Exception e) {
			// ignore
		}
	}

	/**
	 * Get the settings.
	 * 
	 * @return the settings.
	 */
	public Settings getSettings() {
		if (settings == null) {
			settings = new Settings();
		}
		return settings;
	}

	/**
	 * Set the settings.
	 * 
	 * @param settings
	 *            the settings.
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * Stop listening for Newton.
	 */
	public void stop() {
		try {
			// comm.stopListenForNewton();
			if (pipe != null) {
				pipe.disconnect();
				pipe.dispose();
			}
			layer.shutDown();
		} catch (Exception e) {
			// ignore
		}
	}

	/**
	 * Start listening for Newton.
	 * 
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 */
	public void start() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
		Settings settings = getSettings();
		// if (settings.getCommunications().isListen()) {
		String portName = settings.getCommunications().getPortIdentifier();
		if (portName == null) {
			settings.getCommunications().setPortIdentifier("");
			settings.save();
			showSettings();
			return;
		}
		if (portName.length() > 0) {
			int baud = settings.getCommunications().getPortSpeed();
			connectMNP(portName, baud);
		}
		// }
	}

	/**
	 * Show the settings dialog.
	 */
	public void showSettings() {
		stop();
		Settings settings = getSettings();
		getSettingsDialog().setSettings(settings);
		getSettingsDialog().setVisible(true);
		setSettings(settings);
		try {
			start();
		} catch (Exception e) {
			JNCUApp.showError(frame, e);
		}
	}

	/**
	 * Get the settings dialog.
	 * 
	 * @return the dialog.
	 */
	private JNCUSettingsDialog getSettingsDialog() {
		if (settingsDialog == null) {
			settingsDialog = new JNCUSettingsDialog(frame);
			settingsDialog.setSettings(getSettings());
		}
		return settingsDialog;
	}
}
