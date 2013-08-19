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

import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.protocol.v2_0.io.KeyboardInput;
import net.sf.jncu.ui.NCUFrame;

/**
 * NCU controller.
 * 
 * @author Moshe
 */
public class Controller {

	private NCUFrame frame;
	private CDLayer layer;
	private MNPPipe pipe;
	private KeyboardInput keyboardInput;
	private Preferences prefs;
	private Settings settings;

	/**
	 * Create a new controller.
	 * 
	 * @param frame
	 *            the UI frame.
	 * @throws PlatformException
	 */
	public Controller(NCUFrame frame) throws PlatformException {
		this.frame = frame;
		this.prefs = Preferences.getInstance();
		this.layer = CDLayer.getInstance();
		layer.startUp();
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
		// TODO implement me!
	}

	/**
	 * Use keyboard.
	 */
	public void keyboard() {
		if (keyboardInput == null) {
			keyboardInput = new KeyboardInput(pipe, frame);
		}
		keyboardInput.getDialog().setVisible(true);
	}

	/**
	 * Backup.
	 */
	public void backupToDesktop() {
		// TODO implement me!
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

	public Settings getSettings() {
		if (settings == null) {
			settings = new Settings();
		}
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * Stop listening for Newton.
	 */
	public void stop() {
		try {
			if (pipe != null) {
				pipe.disconnect();
			}
		} catch (Exception e) {
			// ignore
		}
	}

	/**
	 * Start listening for Newton.
	 * 
	 * @throws ServiceNotSupportedException
	 * @throws PlatformException
	 * @throws CDILNotInitializedException
	 */
	public void start() throws CDILNotInitializedException, PlatformException,
			ServiceNotSupportedException {
		if (settings.getCommunications().isListen()) {
			String portName = settings.getCommunications().getPortIdentifier();
			int baud = settings.getCommunications().getPortSpeed();
			this.pipe = layer.createMNPSerial(portName, baud);
		}
	}
}
