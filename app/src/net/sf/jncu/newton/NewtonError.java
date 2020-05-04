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
package net.sf.jncu.newton;

import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Newton system error.
 * 
 * @author moshew
 */
public class NewtonError extends Exception {

	private static final Map<Integer, String> errors = new TreeMap<Integer, String>();

	private int errorCode;

	/**
	 * Creates a new error.
	 */
	protected NewtonError() {
		super();
		initErrors();
	}

	/**
	 * Creates a new error.
	 * 
	 * @param errorCode
	 *            the error code.
	 */
	public NewtonError(int errorCode) {
		this();
		this.errorCode = errorCode;
	}

	/**
	 * Creates a new error.
	 * 
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause.
	 */
	public NewtonError(int errorCode, Throwable cause) {
		super(cause);
		initErrors();
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		return errors.get(errorCode);
	}

	/**
	 * Initialise the errors.
	 */
	private void initErrors() {
		synchronized (errors) {
			if (errors.isEmpty()) {
				populateErrors(errors);
			}
		}
	}

	/**
	 * Populate the errors.
	 * 
	 * @param errors
	 *            the list of errors.
	 */
	protected void populateErrors(Map<Integer, String> errors) {
		ResourceBundle bundle = ResourceBundle.getBundle(getClass().getName());
		Enumeration<String> enu = bundle.getKeys();
		String key;
		String value;
		Integer errId;
		while (enu.hasMoreElements()) {
			key = enu.nextElement();
			value = bundle.getString(key);
			errId = Integer.valueOf(key);
			errors.put(errId, value);
		}
	}

	/**
	 * Get the error code.
	 * 
	 * @return the error code.
	 */
	public int getErrorCode() {
		return errorCode;
	}
}
