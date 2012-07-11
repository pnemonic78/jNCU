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

import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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
	 * Place the window in the middle of its parent or owner (or screen if not
	 * owned).
	 * 
	 * @param window
	 *            the window.
	 */
	public static void centreInOwner(Window window) {
		int w = window.getWidth();
		int h = window.getHeight();
		int ox = 0;
		int oy = 0;
		int ow;
		int oh;
		Window owner = window.getOwner();
		if (owner == null) {
			GraphicsDevice gd;
			GraphicsConfiguration gc = window.getGraphicsConfiguration();
			if (gc == null) {
				gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			} else {
				gd = gc.getDevice();
			}
			DisplayMode dm = gd.getDisplayMode();
			ow = dm.getWidth();
			oh = dm.getHeight();
		} else {
			ox = owner.getX();
			oy = owner.getY();
			ow = owner.getWidth();
			oh = owner.getHeight();
		}
		int x = (ox + (ow / 2)) - (w / 2);
		int y = (oy + (oh / 2)) - (h / 2);
		window.setLocation(x, y);
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