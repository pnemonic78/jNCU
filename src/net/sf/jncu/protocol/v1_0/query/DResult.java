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
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;

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
public class DResult extends DockCommandFromNewton implements IDockCommandToNewton {

	/** <tt>kDResult</tt> */
	public static final String COMMAND = "dres";

	/** No error. */
	public static final int ERROR_NONE = 0;
	/** Communications problem (message timed out). */
	public static final int ERROR_TIMEOUT = -10021;
	/** Bad package. */
	public static final int ERROR_BAD_PACKAGE = -10401;
	/** Request cancelled or connection disconnected. */
	public static final int ERROR_DICONNECTED = -16005;
	/**  */
	public static final int ERROR_48000 = -48000;
	/** The PCMCIA card is not a data storage card. */
	public static final int ERROR_48001 = -48001;
	/** Store format is too old to understand. */
	public static final int ERROR_48002 = -48002;
	/** Store format is too new to understand. */
	public static final int ERROR_48003 = -48003;
	/** Store is corrupted, can’t recover. */
	public static final int ERROR_48004 = -48004;
	/** Single object is corrupted, can’t recover. */
	public static final int ERROR_OBJECT_CORRUPTED = -48005;
	/** Object stream has unknown format version. */
	public static final int ERROR_UNKNOWN_FORMAT = -48006;
	/** Fault block is invalid. */
	public static final int ERROR_48007 = -48007;
	/** Not a fault block. */
	public static final int ERROR_48008 = -48008;
	/** Not a soup entry. */
	public static final int ERROR_48009 = -48009;
	/** Tried to remove a store that was not registered. */
	public static final int ERROR_48010 = -48010;
	/** Expected a frame, array, or binary object. */
	public static final int ERROR_OBJECT_NON_PTR = -48200;
	/** Invalid magic pointer. */
	public static final int ERROR_48201 = -48201;
	/** Empty path. */
	public static final int ERROR_48202 = -48202;
	/** Invalid segment in path expression. */
	public static final int ERROR_48203 = -48203;
	/**
	 * Path failed.
	 * <p>
	 * Victor Rehorst: The -48204 is a fairly common error that can happen
	 * whenenver there is an invalid data reference or external function call
	 * that fails. This is usually because there is a missing package or soup
	 * that is being referenced.
	 */
	public static final int ERROR_48204 = -48204;
	/** Index out of bounds (string or array). */
	public static final int ERROR_OUT_OF_BOUNDS = -48205;
	/** Source and destination must be different objects. */
	public static final int ERROR_48206 = -48206;
	/** Long out of range. */
	public static final int ERROR_48207 = -48207;
	/**  */
	public static final int ERROR_48208 = -48208;
	/**  */
	public static final int ERROR_48209 = -48209;
	/** Bad arguments. */
	public static final int ERROR_48210 = -48210;
	/** String too big. */
	public static final int ERROR_48211 = -48211;
	/** Expected a frame, array, or binary object. */
	public static final int ERROR_48212 = -48212;
	/** Expected a frame, array, or binary object. */
	public static final int ERROR_48213 = -48213;
	/** Object is read-only. */
	public static final int ERROR_READ_ONLY = -48214;
	/**  */
	public static final int ERROR_48215 = -48215;
	/** Out of heap memory. */
	public static final int ERROR_48216 = -48216;
	/** Invalid attempted use of magic pointer. */
	public static final int ERROR_48217 = -48217;
	/** Cannot create or change an object to negative size. */
	public static final int ERROR_48218 = -48218;
	/** Value out of range. */
	public static final int ERROR_OUT_OF_RANGE = -48219;
	/** Expected a frame. */
	public static final int ERROR_NOT_FRAME = -48400;
	/** Expected an array. */
	public static final int ERROR_NOT_ARRAY = -48401;
	/** Expected a string. */
	public static final int ERROR_NOT_STRING = -48402;
	/** Expected a frame. array, or binary object. */
	public static final int ERROR_48403 = -48403;
	/** Expected a number. */
	public static final int ERROR_48404 = -48404;
	/** Expected a real. */
	public static final int ERROR_48405 = -48405;
	/** Expected an integer. */
	public static final int ERROR_NOT_INTEGER = -48406;
	/** Expected a character. */
	public static final int ERROR_NOT_CHAR = -48407;
	/** Expected a binary object. */
	public static final int ERROR_NOT_BINARY = -48408;
	/** Expected a path expression (or a symbol or integer). */
	public static final int ERROR_NOT_PATH = -48409;
	/** Expected a symbol. */
	public static final int ERROR_NOT_SYMBOL = -48410;
	/** Expected a function. */
	public static final int ERROR_NOT_FUNCTION = -48411;
	/** Expected a frame or an array. */
	public static final int ERROR_48412 = -48412;
	/** Expected a magic pointer. */
	public static final int ERROR_NOT_MAGIC = -48450;

	private int errorCode;

	/**
	 * Creates a new command.
	 */
	public DResult() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.protocol.DockCommandFromNewton#decodeData(java.io.InputStream
	 * )
	 */
	@Override
	protected void decodeData(InputStream data) throws IOException {
		setErrorCode(ntohl(data));
	}

	public byte[] getPayload() {
		IDockCommandToNewton cmd = new DockCommandToNewton(COMMAND) {
			@Override
			protected void writeCommandData(OutputStream data) throws IOException {
				htonl(getErrorCode(), data);
			}
		};
		return cmd.getPayload();
	}

	/**
	 * Get the error code.
	 * 
	 * @return the error code.
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * Set the error code.
	 * 
	 * @param errorCode
	 *            the error code.
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public int hashCode() {
		return getErrorCode();
	}

	@Override
	public String toString() {
		return "error code: " + getErrorCode();
	}
}
