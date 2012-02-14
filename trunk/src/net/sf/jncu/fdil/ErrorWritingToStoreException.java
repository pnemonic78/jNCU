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
package net.sf.jncu.fdil;

/**
 * Error writing to store - <tt>kFD_ErrorWritingToStore</tt>.
 * 
 * @author Moshe
 */
public class ErrorWritingToStoreException extends FDILException {

	public ErrorWritingToStoreException() {
		super();
	}

	public ErrorWritingToStoreException(String message) {
		super(message);
	}

	public ErrorWritingToStoreException(Throwable cause) {
		super(cause);
	}

	public ErrorWritingToStoreException(String message, Throwable cause) {
		super(message, cause);
	}

}
