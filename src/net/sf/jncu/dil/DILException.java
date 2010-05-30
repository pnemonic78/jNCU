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
package net.sf.jncu.dil;

/**
 * CDIL Error - <tt>DIL_Error</tt>.
 * 
 * @author moshew
 */
public class DILException extends Exception {

	/**
	 * Creates a new CDIL exception.
	 */
	public DILException() {
		super();
	}

	/**
	 * Creates a new CDIL exception.
	 * 
	 * @param message
	 *            the detail message.
	 */
	public DILException(String message) {
		super(message);
	}

	/**
	 * Creates a new CDIL exception.
	 * 
	 * @param cause
	 *            the cause.
	 */
	public DILException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new CDIL exception.
	 * 
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause.
	 */
	public DILException(String message, Throwable cause) {
		super(message, cause);
	}

}
