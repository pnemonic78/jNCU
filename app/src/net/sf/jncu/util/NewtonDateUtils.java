/*
 * Copyright 2010, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jncu.util;

import net.sf.jncu.fdil.NSOFInteger;

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

    /**
     * The number of seconds since 1993 January 1, 00:00:00 UTC (GMT).
     */
    public static final long SECONDS_1993 = 725846400000L;
    /**
     * The number of minutes since 1904 January 1, 00:00:00 UTC (GMT).
     */
    public static final long MINUTES_1904 = -2082844800000L;

    /**
     * Creates a new utility.
     */
    private NewtonDateUtils() {
        super();
    }

    /**
     * Get the number of seconds since 1993 January 1, 00:00:00 UTC (GMT).
     *
     * @param millis the number of milliseconds since 1970 January 1, 00:00:00 UTC
     *               (GMT).
     * @return the Newton date.
     */
    public static int getSeconds(long millis) {
        return (int) ((millis - SECONDS_1993) / 1000L);
    }

    /**
     * Get the number of minutes since 1904 January 1, 00:00:00 UTC (GMT).
     *
     * @param millis the number of milliseconds since 1970 January 1, 00:00:00 UTC
     *               (GMT).
     * @return the Newton date.
     */
    public static int getMinutes(long millis) {
        return (int) ((millis - MINUTES_1904) / 60000L);
    }

    /**
     * Get the number of days since 1904 January 1, 00:00:00 UTC (GMT).
     *
     * @param millis the number of milliseconds since 1970 January 1, 00:00:00 UTC
     *               (GMT).
     * @return the Newton date.
     */
    public static int getDays(long millis) {
        return (int) ((millis - MINUTES_1904) / 86400000L);
    }

    /**
     * Get the number of seconds since 1993 January 1, 00:00:00 UTC (GMT).
     *
     * @param millis the number of milliseconds since 1970 January 1, 00:00:00 UTC
     *               (GMT).
     * @return the Newton date.
     */
    public static NSOFInteger toSeconds(long millis) {
        return new NSOFInteger(getSeconds(millis));
    }

    /**
     * Get the number of minutes since 1904 January 1, 00:00:00 UTC (GMT).
     *
     * @param millis the number of milliseconds since 1970 January 1, 00:00:00 UTC
     *               (GMT).
     * @return the Newton date.
     */
    public static NSOFInteger toMinutes(long millis) {
        return new NSOFInteger(getMinutes(millis));
    }

    /**
     * Get the number of days since 1904 January 1, 00:00:00 UTC (GMT).
     *
     * @param millis the number of milliseconds since 1970 January 1, 00:00:00 UTC
     *               (GMT).
     * @return the Newton date.
     */
    public static NSOFInteger toDays(long millis) {
        return new NSOFInteger(getDays(millis));
    }

    /**
     * Get the number of milliseconds since 1970 January 1, 00:00:00 UTC (GMT).
     *
     * @param seconds the number of seconds since 1993 January 1, 00:00:00 UTC
     *                (GMT).
     * @return the Java date.
     */
    public static long fromSeconds(int seconds) {
        return (seconds * 1000L) + SECONDS_1993;
    }

    /**
     * Get the number of milliseconds since 1970 January 1, 00:00:00 UTC (GMT).
     *
     * @param minutes the number of minutes since 1904 January 1, 00:00:00 UTC
     *                (GMT).
     * @return the Java date.
     */
    public static long fromMinutes(int minutes) {
        return (minutes * 60000L) + MINUTES_1904;
    }

    /**
     * Get the number of milliseconds since 1970 January 1, 00:00:00 UTC (GMT).
     *
     * @param days the number of days since 1904 January 1, 00:00:00 UTC
     *             (GMT).
     * @return the Java date.
     */
    public static long fromDays(int days) {
        return (days * 86400000L) + MINUTES_1904;
    }

}
