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

/**
 * Number utilities.
 *
 * @author moshew
 */
public class NumberUtils {

    /**
     * Creates a new utility.
     */
    private NumberUtils() {
        super();
    }

    /**
     * Convert the 64-bit number of an array of bytes arranged in Big Endian
     * order.
     *
     * @param l the number.
     * @return the array of 8 bytes.
     */
    public static byte[] toBytes(long l) {
        byte[] buf = new byte[8];
        for (int i = 7; i >= 0; i--, l >>>= 8) {
            buf[i] = (byte) (l & 0xFF);
        }
        return buf;
    }

    /**
     * Convert the array of bytes to a 64-bit number arranged in Big Endian
     * order.
     *
     * @param b the array of 8 bytes.
     * @return the 64-bit number.
     */
    public static long toLong(byte[] b) {
        if ((b == null) || (b.length == 0)) {
            return 0;
        }
        long l = 0;
        for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= (b[i] & 0xFFL);
        }
        return l;
    }

    /**
     * Formats a content size to be in the form of bytes, kilobytes, megabytes,
     * etc.
     *
     * @param number size value to be formatted.
     * @return formatted string with the number.
     */
    public static String formatFileSize(long number) {
        float result = number;
        String suffix = "B";
        if (result > 900) {
            suffix = "KB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "MB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "GB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "TB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "PB";
            result = result / 1024;
        }
        if (result < 1) {
            return String.format("%.2f %s", result, suffix);
        }
        if (result < 10) {
            return String.format("%.2f %s", result, suffix);
        }
        if (result < 100) {
            return String.format("%.2f %s", result, suffix);
        }
        return String.format("%.0f %s", result, suffix);
    }
}
