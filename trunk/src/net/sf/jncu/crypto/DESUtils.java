package net.sf.jncu.crypto;

/**
 * DES utilities.
 * 
 * @author moshew
 */
public class DESUtils {

	/**
	 * Convert the array of bytes to a 64-bit number arranged in Big Endian
	 * order.
	 * 
	 * @param b
	 *            the array of 8 bytes.
	 * @return the 64-bit number.
	 */
	public static long toLong(byte[] b) {
		long l = 0;
		for (int i = 0; i < 8; i++) {
			l <<= 8;
			l |= (b[i] & 0xFFL);
		}
		return l;
	}

	/**
	 * Convert the 64-bit number of an array of bytes arranged in Big Endian
	 * order.
	 * 
	 * @param l
	 *            the number.
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
	 * Convert a 64-bit Big Endian number to 64 separate bits.<br>
	 * MSB is at index <tt>0</tt>.
	 * 
	 * @param l
	 *            the number.
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
	 * MSB is at index <tt>0</tt>.
	 * 
	 * @param b
	 *            the array of bits.
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
	 * @param n
	 *            the array of Big Endian bits.
	 */
	public static void oddParity(byte[] n) {
		int parity;
		for (int p = 0; p < 64;) {
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
