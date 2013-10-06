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
package net.sf.jncu.protocol.v1_0.query;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.newton.NewtonError;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandBidi;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command is sent in response to any of the commands from the desktop that
 * don't request data. It lets the desktop know that things are still proceeding
 * OK.
 * 
 * <pre>
 * 'dres'
 * length
 * error code
 * </pre>
 * 
 * @see http://tools.unna.org/errors/
 * @author moshew
 */
public class DResult extends BaseDockCommandFromNewton implements DockCommandBidi {

	/** <tt>kDResult</tt> */
	public static final String COMMAND = "dres";

	/** No error. */
	public static final int OK = 0;

	private NewtonError error;
	private DockCommandToNewton to;

	/**
	 * Creates a new command.
	 */
	public DResult() {
		super(COMMAND);
		setErrorCode(OK);
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
		setErrorCode(ntohl(data));
	}

	@Override
	public InputStream getCommandPayload() throws IOException {
		if (to == null) {
			to = new DockCommandToNewtonLong(COMMAND) {
				@Override
				protected int getValue() {
					return getErrorCode();
				}
			};
		}
		return to.getCommandPayload();
	}

	@Override
	public int getCommandPayloadLength() throws IOException {
		return to.getCommandPayloadLength();
	}

	/**
	 * Get the error code.
	 * 
	 * @return the error code.
	 */
	public int getErrorCode() {
		return (error == null) ? OK : error.getErrorCode();
	}

	/**
	 * Set the error code.
	 * 
	 * @param errorCode
	 *            the error code.
	 */
	public void setErrorCode(int errorCode) {
		this.error = new NewtonError(errorCode);
	}

	/**
	 * Get the error.
	 * 
	 * @return the error.
	 */
	public NewtonError getError() {
		return error;
	}

	/**
	 * Set the error.
	 * 
	 * @param error
	 *            the error.
	 */
	public void setError(NewtonError error) {
		this.error = error;
	}

	@Override
	public int hashCode() {
		return getErrorCode();
	}

	@Override
	public String toString() {
		return (error == null) ? super.toString() : error.toString();
	}

}
