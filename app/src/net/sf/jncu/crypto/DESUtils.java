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
package net.sf.jncu.crypto;

/**
 * DES utilities.
 *
 * @author moshew
 */
public class DESUtils {

    /**
     * Convert a 64-bit Big Endian number to 64 separate bits.<br>
     * MSB is at index {@code 0}.
     *
     * @param l the number.
     * @return the array of bits.
     */
    public static byte[] toBits(long l) {
        byte[] b = new byte[64];
        for (int i = 64 - 1; i >= 0; i--) {
            b[i] = (byte) (l & 0x1L);
            l >>= 1;
        }
        return b;
    }

    /**
     * Convert 64 bits to a 64-bit Big Endian number.<br>
     * MSB is at index {@code 0}.
     *
     * @param b the array of bits.
     * @return the number.
     */
    public static long fromBits(byte[] b) {
        long l = 0;
        for (int i = 0; i < 64; i++) {
            l <<= 1;
            l |= b[i] & 0x1L;
        }
        return l;
    }

    /**
     * Odd parity.<br>
     * <em>Bits 8, 16,..., 64 are for use in assuring that each byte is of odd parity.</em>
     *
     * @param n the array of Big Endian bits.
     */
    public static void oddParity(byte[] n) {
        int parity;
        for (int p = 0; p < 64; ) {
            parity = 1;
            if (n[p++] != 0) {
                parity++;
            }
            if (n[p++] != 0) {
                parity++;
            }
            if (n[p++] != 0) {
                parity++;
            }
            if (n[p++] != 0) {
                parity++;
            }
            if (n[p++] != 0) {
                parity++;
            }
            if (n[p++] != 0) {
                parity++;
            }
            if (n[p++] != 0) {
                parity++;
            }
            n[p++] = (byte) (parity & 1);
        }
    }

}
