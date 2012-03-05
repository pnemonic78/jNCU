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
package net.sf.jncu.protocol.v1_0.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * This command will load a package into the Newton's RAM. The package data
 * should be padded to an even multiple of 4 by adding zero bytes to the end of
 * the package data.
 * 
 * <pre>
 * 'lpkg'
 * length
 * package data
 * </pre>
 * 
 * @author Moshe
 */
public class DLoadPackage extends DockCommandToNewton {

	/** <tt>kDLoadPackage</tt> */
	public static final String COMMAND = "lpkg";

	private File file;

	/**
	 * Creates a new command.
	 */
	public DLoadPackage() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		File file = getFile();
		if (file == null)
			return;

		int size = (int) file.length();
		if (size < 8)
			throw new PackageException("package size too small");
		FileInputStream in = null;
		byte[] buf = new byte[Math.min(size, 1024)];
		int count;

		try {
			in = new FileInputStream(file);
			count = in.read(buf);

			// Check that the file header starts with "package"
			if ((buf[0] != 'p') || (buf[1] != 'a') || (buf[2] != 'c') || (buf[3] != 'k') || (buf[4] != 'a') || (buf[5] != 'g') || (buf[6] != 'e'))
				throw new PackageException("package header must start with 'package'");

			while ((count != -1) && (size > 0)) {
				data.write(buf, 0, count);
				size -= count;
				count = in.read(buf);
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					// ignore
				}
			}
		}
	}

	/**
	 * Get the package file.
	 * 
	 * @return the file.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Set the package file.
	 * 
	 * @param file
	 *            the file.
	 */
	public void setFile(File file) {
		this.file = file;
		if (file == null)
			setLength(0);
		else
			setLength((int) file.length());
	}

}
