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
package net.sf.jncu.util;

/**
 * Newton date utilities.
 * <p>
 * These functions use the system clock value, which is either the number of
 * minutes since midnight, January 1, 1904 or the number of seconds since
 * midnight, January 1, 1993.
 * 
 * @author moshew
 */
public class NewtonDateUtils {

	/** The number of minutes since 1904 January 1, 00:00:00 UTC (GMT). */
	public static final long MINUTES_1904 = -2082844800000L;
	/** The number of seconds since 1993 January 1, 00:00:00 UTC (GMT). */
	public static final long SECONDS_1993 = 725846400000L;

	/**
	 * Creates a new utility.
	 */
	private NewtonDateUtils() {
		super();
	}

	/**
	 * Get the number of seconds since 1993 January 1, 00:00:00 UTC (GMT).
	 * 
	 * @param millis
	 *            the number of milliseconds since 1970 January 1, 00:00:00 UTC
	 *            (GMT).
	 * @return the Newton date.
	 */
	public static int getSeconds(long millis) {
		return (int) ((millis - SECONDS_1993) / 1000L);
	}

	/**
	 * Get the number of minutes since 1904 January 1, 00:00:00 UTC (GMT).
	 * 
	 * @param millis
	 *            the number of milliseconds since 1970 January 1, 00:00:00 UTC
	 *            (GMT).
	 * @return the Newton date.
	 */
	public static int getMinutes(long millis) {
		return (int) ((millis - MINUTES_1904) / 60000L);
	}

}
