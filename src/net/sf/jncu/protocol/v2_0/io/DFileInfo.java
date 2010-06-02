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
import javax.swing.filechooser.FileSystemView;

import net.sf.jncu.newton.stream.NSOFBinaryObject;
import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFFrame;
import net.sf.jncu.newton.stream.NSOFInteger;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.newton.stream.NSOFSymbol;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.util.NewtonDateUtils;

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
public class DFileInfo extends DockCommandToNewton {

	/** <tt>kDFileInfo</tt> */
	public static final String COMMAND = "finf";

	protected static final NSOFSymbol SLOT_KIND = new NSOFSymbol("kind");
	protected static final NSOFSymbol SLOT_SIZE = new NSOFSymbol("size");
	protected static final NSOFSymbol SLOT_CREATED = new NSOFSymbol("created");
	protected static final NSOFSymbol SLOT_MODIFIED = new NSOFSymbol("modified");
	protected static final NSOFSymbol SLOT_ICON = new NSOFSymbol("icon");
	protected static final NSOFSymbol SLOT_PATH = new NSOFSymbol("path");

	private File file;
	protected static final FileSystemView fileSystemView = FileSystemView.getFileSystemView();

	/**
	 * Creates a new command.
	 */
	public DFileInfo() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(getFrame(getFile()), data);
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
	 * @param file
	 *            the file.
	 * @return the frame.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected NSOFFrame getFrame(File file) throws IOException {
		String description = fileSystemView.getSystemTypeDescription(file);
		if (description == null) {
			description = "";
		}

		NSOFInteger mtime = new NSOFInteger(NewtonDateUtils.getMinutes(file.lastModified()));
		NSOFBinaryObject icon = getIcon(file);

		NSOFFrame frame = new NSOFFrame();
		frame.put(SLOT_CREATED, mtime);
		if (icon != null) {
			frame.put(SLOT_ICON, icon);
		}
		frame.put(SLOT_KIND, new NSOFString(description));
		frame.put(SLOT_MODIFIED, mtime);
		frame.put(SLOT_PATH, new NSOFString(file.getCanonicalPath()));
		frame.put(SLOT_SIZE, new NSOFInteger((int) file.length()));
		return frame;
	}

	/**
	 * Get the file icon.
	 * 
	 * @param file
	 *            the file.
	 * @return the icon.
	 */
	protected NSOFBinaryObject getIcon(File file) {
		Icon icon = fileSystemView.getSystemIcon(file);
		if (icon == null) {
			return null;
		}
		NSOFBinaryObject bin = new NSOFBinaryObject();
		// TODO convert "Java Icon" to "Newton bitmap" or "Apple PICT image".
		// see "developer/QAs-2.x/html/newtbitm.htm"
		return bin;
	}
}
