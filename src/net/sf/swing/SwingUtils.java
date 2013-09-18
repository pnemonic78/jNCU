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
package net.sf.swing;

import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

/**
 * Swing utilities.
 * 
 * @author Moshe
 */
public class SwingUtils {

	private static boolean initialised;
	private static FileSystemView fileSystemView;

	static {
		init();
	}

	/**
	 * Constructs a new object.
	 */
	private SwingUtils() {
		super();
	}

	/**
	 * Initialise.
	 */
	public static void init() {
		if (initialised)
			return;

		// Set the system L&F.
		try {
			String systemLAF = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(systemLAF);
		} catch (Exception e) {
			e.printStackTrace();
		}

		initialised = true;
	}

	/**
	 * Get the file system view.
	 * 
	 * @return the view.
	 */
	public static FileSystemView getFileSystemView() {
		if (fileSystemView == null) {
			fileSystemView = FileSystemView.getFileSystemView();
		}
		return fileSystemView;
	}

	/**
	 * Get all root partitions on this system.
	 * 
	 * @return the roots.
	 */
	public static File[] getRoots() {
		return getFileSystemView().getRoots();
	}

	/**
	 * Post a window closing event to the window.
	 * 
	 * @param window
	 *            the window to close.
	 */
	public static void postWindowClosing(Window window) {
		WindowEvent closingEvent = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);
	}
}
