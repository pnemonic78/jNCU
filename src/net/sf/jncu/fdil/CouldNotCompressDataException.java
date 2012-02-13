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

import net.sf.jncu.dil.DILException;

/**
 * Could not compress data error - <tt>kFD_CouldNotCompressData</tt>.
 * 
 * @author Moshe
 */
public class CouldNotCompressDataException extends DILException {

	public CouldNotCompressDataException() {
		super();
	}

	public CouldNotCompressDataException(String message) {
		super(message);
	}

	public CouldNotCompressDataException(Throwable cause) {
		super(cause);
	}

	public CouldNotCompressDataException(String message, Throwable cause) {
		super(message, cause);
	}

}
