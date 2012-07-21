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

import java.io.IOException;

/**
 * Thrown to indicate that a backup operation could not complete.
 * 
 * @author Moshe
 */
public class BackupException extends IOException {

	/**
	 * Constructs a new exception.
	 */
	public BackupException() {
		super();
	}

	/**
	 * Constructs a new exception.
	 * 
	 * @param message
	 *            the detail message.
	 */
	public BackupException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception.
	 * 
	 * @param cause
	 *            the cause.
	 */
	public BackupException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception.
	 * 
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause.
	 */
	public BackupException(String message, Throwable cause) {
		super(message, cause);
	}

}
