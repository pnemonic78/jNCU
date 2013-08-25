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

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.ui.NCUFrame;

/**
 * jNCU entry point.
 * 
 * @author Moshe
 */
public class JNCU {

	private Controller control;
	private NCUFrame frame;

	/**
	 * Constructs a new NCU.
	 * 
	 * @throws PlatformException
	 *             if an error occurs.
	 */
	public JNCU() throws PlatformException {
		super();
		this.frame = new NCUFrame();
		this.control = new Controller(frame);
		frame.setController(control);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 * @throws Exception
	 *             if an error occurs.
	 */
	public static void main(String[] args) throws Exception {
		JNCU ncu = new JNCU();
		ncu.run();
	}

	/**
	 * Run.
	 */
	public void run() {
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
	 * @param frame
	 *            the owner frame.
	 * @param e
	 *            the error.
	 */
	public static void showError(Frame frame, Exception e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(frame, "Error: " + e.getLocalizedMessage(), frame.getTitle(), JOptionPane.ERROR_MESSAGE);
		// TODO Log the error using SourceForge
	}

}
