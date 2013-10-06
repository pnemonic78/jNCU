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
package net.sf.jncu.protocol.v1_0.sync;

import net.sf.jncu.protocol.DockCommandToNewtonLong;
import net.sf.jncu.util.NewtonDateUtils;

/**
 * The time of the last sync.
 * 
 * <pre>
 * 'stme'
 * length
 * time in minutes
 * </pre>
 * 
 * @author moshew
 */
public class DLastSyncTime extends DockCommandToNewtonLong {

	/** <tt>kDLastSyncTime</tt> */
	public static final String COMMAND = "stme";

	/**
	 * Creates a new command.
	 */
	public DLastSyncTime() {
		this(0);
	}

	/**
	 * Creates a new command.
	 * 
	 * @param time
	 *            the time in milliseconds.
	 */
	public DLastSyncTime(long time) {
		super(COMMAND);
		setLastSyncTime(time);
	}

	/**
	 * Get time of the last sync.
	 * 
	 * @return the time in milliseconds.
	 */
	public long getLastSyncTime() {
		return NewtonDateUtils.fromMinutes(getValue());
	}

	/**
	 * Set time of the last sync.
	 * 
	 * @param time
	 *            the time in milliseconds.
	 */
	public void setLastSyncTime(long time) {
		setValue(NewtonDateUtils.getMinutes(time));
	}
}
