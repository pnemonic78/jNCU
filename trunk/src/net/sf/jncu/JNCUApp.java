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

import java.awt.Frame;
import java.awt.Window;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.ui.JNCUFrame;

/**
 * jNCU main application.
 * 
 * @author Moshe
 */
public class JNCUApp {

	private Controller control;
	private JNCUFrame frame;

	/**
	 * Constructs a new NCU.
	 * 
	 * @throws PlatformException
	 *             if an error occurs.
	 */
	public JNCUApp() throws PlatformException {
		super();
		this.frame = new JNCUFrame();
		this.control = new Controller(frame);
		frame.setController(control);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		try {
			JNCUApp ncu = new JNCUApp();
			ncu.run();
		} catch (Exception e) {
			showError(null, "main", e);
		}
	}

	/**
	 * Run.
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
	public void run() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		control.start();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Show the error.
	 * 
	 * @param window
	 *            the owner window.
	 * @param message
	 *            the message.
	 * @param e
	 *            the error.
	 */
	public static void showError(final Window window, final String message, final Throwable e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				String msg = (message == null ? "" : message);
				if (e != null) {
					e.printStackTrace();
					msg += "\n" + e.getLocalizedMessage();
				}
				String title = null;
				if (window != null) {
					if (window instanceof Frame) {
						title = ((Frame) window).getTitle();
					}
				}
				if (title == null)
					title = JNCUResources.getString("jncu", "jNCU");
				JOptionPane.showMessageDialog(window, msg, title, JOptionPane.ERROR_MESSAGE);
			}
		});
	}

}
