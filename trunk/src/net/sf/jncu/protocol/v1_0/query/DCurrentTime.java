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

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDCurrentTime</tt><br>
 * The current time on the Newton.
 * <p>
 * In response to a <tt>kDLastSyncTime</tt> command.
 * 
 * <pre>
 * 'time'
 * length = 4
 * time
 * </pre>
 * 
 * @author moshew
 */
public class DCurrentTime extends DockCommandFromNewton {

	public static final String COMMAND = "time";

	private int time;

	/**
	 * Creates a new command.
	 */
	public DCurrentTime() {
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
		setTime(ntohl(data));
	}

	/**
	 * Set the time.
	 * 
	 * @param time
	 *            the time.
	 */
	protected void setTime(int time) {
		this.time = time;
	}

	/**
	 * Get the time.
	 * 
	 * @return the time.
	 */
	public int getTime() {
		return time;
	}

}
