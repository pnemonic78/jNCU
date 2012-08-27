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
package net.sf.jncu.translate;

import java.io.IOException;

/**
 * Translator exception.
 * 
 * @author Moshe
 */
public class TranslationException extends IOException {

	/**
	 * Constructs a new exception.
	 */
	public TranslationException() {
		super();
	}

	/**
	 * Constructs a new exception.
	 * 
	 * @param message
	 *            the detail message.
	 */
	public TranslationException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception.
	 * 
	 * @param cause
	 *            the cause.
	 */
	public TranslationException(Throwable cause) {
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
	public TranslationException(String message, Throwable cause) {
		super(message, cause);
	}

}
