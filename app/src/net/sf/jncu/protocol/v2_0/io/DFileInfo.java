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
package net.sf.jncu.protocol.v2_0.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.Icon;

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.fdil.contrib.NSOFIcon;
import net.sf.jncu.protocol.BaseDockCommandToNewton;
import net.sf.jncu.util.NewtonDateUtils;
import net.sf.swing.SwingUtils;

/**
 * This command is sent in response to a <tt>kDGetFileInfo</tt> command. It
 * returns a frame that looks like this:<br>
 * <code>{<br>
 * &nbsp;&nbsp;kind: "Microsoft Word document",<br>
 * &nbsp;&nbsp;size: 20480,<br>
 * &nbsp;&nbsp;created: 3921837,<br>
 * &nbsp;&nbsp;modified: 3434923,<br>
 * &nbsp;&nbsp;icon: &lt;binary object of icon&gt;,<br>
 * &nbsp;&nbsp;path: "hd:files:another folder:"<br>
 * }</code>
 * <p>
 * <tt>kind</tt> is a description of the file.<br>
 * <tt>size</tt> is the number of bytes (actual, not the amount used on the
 * disk).<br>
 * <tt>created</tt> is the creation date in Newton date format.<br>
 * <tt>modified</tt> is the modification date of the file.<br>
 * <tt>icon</tt> is an icon to display. This is optional.<br>
 * <tt>path</tt> is the "user understandable" path description<br>
 * 
 * <pre>
 * 'finf'
 * length
 * frame of info
 * </pre>
 * 
 * @author moshew
 */
public class DFileInfo extends BaseDockCommandToNewton {

	/** <tt>kDFileInfo</tt> */
	public static final String COMMAND = "finf";

	protected static final NSOFSymbol SLOT_KIND = new NSOFSymbol("kind");
	protected static final NSOFSymbol SLOT_SIZE = new NSOFSymbol("size");
	protected static final NSOFSymbol SLOT_CREATED = new NSOFSymbol("created");
	protected static final NSOFSymbol SLOT_MODIFIED = new NSOFSymbol("modified");
	protected static final NSOFSymbol SLOT_ICON = new NSOFSymbol("icon");
	protected static final NSOFSymbol SLOT_ICONPRO = new NSOFSymbol("iconPro");
	protected static final NSOFSymbol SLOT_PATH = new NSOFSymbol("path");

	private File file;

	/**
	 * Creates a new command.
	 */
	public DFileInfo() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(toFrame(), data);
	}

	/**
	 * Get the file.
	 * 
	 * @return the file.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Set the file.
	 * 
	 * @param file
	 *            the file.
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Get the file frame.
	 * 
	 * @return the frame.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public NSOFFrame toFrame() throws IOException {
		NSOFFrame frame = new NSOFFrame();

		frame.put(SLOT_PATH, new NSOFString(file.getCanonicalPath()));

		String description = SwingUtils.getFileSystemView().getSystemTypeDescription(file);
		if (description == null)
			description = "Unknown";
		frame.put(SLOT_KIND, new NSOFString(description));

		NSOFInteger mtime = NewtonDateUtils.toMinutes(file.lastModified());
		frame.put(SLOT_CREATED, mtime);
		frame.put(SLOT_MODIFIED, mtime);

		int size = (int) (file.length() & 0xFFFFFFFFL);
		frame.put(SLOT_SIZE, new NSOFInteger(size));

		NSOFIcon icon = getIcon();
		if (icon != null)
			frame.put(SLOT_ICON, icon);

		return frame;
	}

	/**
	 * Get the file icon.
	 * 
	 * @return the icon.
	 */
	protected NSOFIcon getIcon() {
		Icon icon = SwingUtils.getFileSystemView().getSystemIcon(file);
		if (icon == null) {
			return null;
		}
		// Convert "Java Icon" to "Newton bitmap" or "Apple PICT image".
		NSOFIcon bmp = new NSOFIcon();
		bmp.fromIcon(icon);
		return bmp;
	}
}
