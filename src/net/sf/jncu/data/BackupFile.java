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
package net.sf.jncu.data;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Backup data file.
 * 
 * @author mwaisberg
 * 
 */
public class BackupFile extends ZipFile {

	/**
	 * Creates a new backup file.
	 * 
	 * @param name
	 *            the name of the backup file.
	 * @throws IOException
	 *             if an I/O error has occurred.
	 */
	public BackupFile(String name) throws IOException {
		super(name);
	}

	/**
	 * Creates a new backup file.
	 * 
	 * @param file
	 *            the file to be opened.
	 * @throws ZipException
	 * @throws IOException
	 *             if an I/O error has occurred.
	 */
	public BackupFile(File file) throws ZipException, IOException {
		super(file);
	}

	/**
	 * Creates a new backup file.
	 * 
	 * @param file
	 *            the file to be opened.
	 * @param mode
	 *            the mode in which the file is to be opened.
	 * @throws IOException
	 *             if an I/O error has occurred.
	 * @see ZipFile#OPEN_DELETE
	 * @see ZipFile#OPEN_READ
	 */
	public BackupFile(File file, int mode) throws IOException {
		super(file, mode);
	}

}
