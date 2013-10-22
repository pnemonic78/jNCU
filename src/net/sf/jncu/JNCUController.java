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

import java.util.concurrent.TimeoutException;

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.newton.os.NewtonInfo;

/**
 * jNCU controller interface.
 * 
 * @author Moshe
 */
public interface JNCUController {

	/**
	 * Start listening for Newton.
	 * 
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void start() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException, BadPipeStateException, PipeDisconnectedException, TimeoutException;

	/**
	 * Stop listening for Newton.
	 */
	public void stop();

	/**
	 * Shut down the controller and exit the application.
	 */
	public void exit();

	/**
	 * Backup from Newton to desktop.
	 */
	public void backupToDesktop();

	/**
	 * Export from Newton to desktop.
	 */
	public void exportToDesktop();

	/**
	 * Restore from desktop to Newton.
	 */
	public void restoreToNewton();

	/**
	 * Import from desktop to Newton.
	 */
	public void importToNewton();

	/**
	 * Synchronize.
	 */
	public void sync();

	/**
	 * Use keyboard passthrough.
	 */
	public void keyboard();

	/**
	 * Install package on the Newton.
	 */
	public void installPackage();

	/**
	 * Get the device information.
	 * 
	 * @return the Newton information.
	 */
	public NewtonInfo getDeviceInformation();

	/**
	 * Show the Newton information.
	 */
	public void showDevice();

	/**
	 * Show the settings dialog.
	 * 
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void showSettings() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException;
}
