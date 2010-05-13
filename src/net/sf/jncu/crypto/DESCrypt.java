package net.sf.jncu.crypto;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DATA ENCRYPTION STANDARD (DES), Electronic Codebook (ECB), no padding.
 * 
 * @see http://www.itl.nist.gov/fipspubs/fip46-2.htm
 * @author moshew
 */
public class DESCrypt {

	/**
	 * The 64 bits of the input block to be enciphered are first subjected to
	 * the following permutation, called the initial permutation (IP).
	 */
	private static final int[] IP = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16,
			8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };

	/**
	 * That is the permuted input has bit 58 of the input as its first bit, bit
	 * 50 as its second bit, and so on with bit 7 as its last bit. The permuted
	 * input block is then the input to a complex key-dependent computation
	 * described below. The output of that computation, called the pre-output,
	 * is then subjected to the following permutation which is the inverse of
	 * the initial permutation.
	 */
	private static final int[] IP_1 = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61,
			29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };

	/**
	 * Let E denote a function which takes a block of 32 bits as input and
	 * yields a block of 48 bits as output. Let E be such that the 48 bits of
	 * its output, written as 8 blocks of 6 bits each, are obtained by selecting
	 * the bits in its inputs in order.
	 */
	private static final int[] E = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23,
			24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };

	/**
	 * The permutation function P yields a 32-bit output from a 32-bit input by
	 * permuting the bits of the input block.
	 */
	private static final int[] P = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25 };

	/** Selection function 1. */
	private static final int[][] S1 = { { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 }, { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
			{ 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 }, { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } };
	/** Selection function 2. */
	private static final int[][] S2 = { { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 }, { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
			{ 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 }, { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } };
	/** Selection function 3. */
	private static final int[][] S3 = { { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 }, { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
			{ 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 }, { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } };
	/** Selection function 4. */
	private static final int[][] S4 = { { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 }, { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
			{ 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 }, { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } };
	/** Selection function 5. */
	private static final int[][] S5 = { { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 }, { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
			{ 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 }, { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } };
	/** Selection function 6. */
	private static final int[][] S6 = { { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 }, { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
			{ 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 }, { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } };
	/** Selection function 7. */
	private static final int[][] S7 = { { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 }, { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
			{ 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 }, { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } };
	/** Selection function 8. */
	private static final int[][] S8 = { { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 }, { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
			{ 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 }, { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } };

	/**
	 * Each of the unique selection functions S1,S2,...,S8, takes a 6-bit block
	 * as input and yields a 4-bit block as output.
	 */
	private static final int[][][] S = { S1, S2, S3, S4, S5, S6, S7, S8 };

	/**
	 * Permuted choice 1.
	 */
	private static final int[] PC1 = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47,
			39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };

	/**
	 * Permuted choice 2.
	 */
	private static final int[] PC2 = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40,
			51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 };

	/**
	 * Schedule of left shifts of the individual blocks.
	 */
	private static final int[] SHIFTS = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

	/** Cipher block size. */
	private static final int CIPHER_BLOCK_SIZE = 64;
	/** Cipher function block size. */
	private static final int CF_BLOCK_SIZE = 32;
	/** Key block size. */
	private static final int KEY_BLOCK_SIZE = 48;
	/** Key schedule block size. */
	private static final int KS_BLOCK_SIZE = 28;
	/** Key schedule block size * 2. */
	private static final int KS_BLOCK_SIZE2 = KS_BLOCK_SIZE << 1;

	private int[] key;
	private boolean decrypting = false;
	// Let K be a block of 48 bits chosen from the 64-bit key.
	private final int[] k1 = new int[KEY_BLOCK_SIZE];
	private final int[] k2 = new int[KEY_BLOCK_SIZE];
	private final int[] k3 = new int[KEY_BLOCK_SIZE];
	private final int[] k4 = new int[KEY_BLOCK_SIZE];
	private final int[] k5 = new int[KEY_BLOCK_SIZE];
	private final int[] k6 = new int[KEY_BLOCK_SIZE];
	private final int[] k7 = new int[KEY_BLOCK_SIZE];
	private final int[] k8 = new int[KEY_BLOCK_SIZE];
	private final int[] k9 = new int[KEY_BLOCK_SIZE];
	private final int[] k10 = new int[KEY_BLOCK_SIZE];
	private final int[] k11 = new int[KEY_BLOCK_SIZE];
	private final int[] k12 = new int[KEY_BLOCK_SIZE];
	private final int[] k13 = new int[KEY_BLOCK_SIZE];
	private final int[] k14 = new int[KEY_BLOCK_SIZE];
	private final int[] k15 = new int[KEY_BLOCK_SIZE];
	private final int[] k16 = new int[KEY_BLOCK_SIZE];

	/**
	 * 
	 */
	public DESCrypt() {
		super();
	}

	public void setKey(long key) {
		this.key = toBits(key);
		keySchedule(this.key);
	}

	public void setKey(Key key) {
		setKey(toLong(key.getEncoded()));
	}

	public long encipher(long input) {
		int[] in = toBits(input);
		int[] out = new int[CIPHER_BLOCK_SIZE];
		encipher(in, out);
		return fromBits(out);
	}

	private void encipher(int[] in, int[] out) {
		// The 64 bits of the input block to be enciphered are first subjected
		// to the following permutation, called the initial permutation IP.
		// Let the 64 bits of the input block to an iteration consist of a 32
		// bit block L followed by a 32 bit block R. Using the notation defined
		// in the introduction, the input block is then LR.
		int[] l0 = new int[CF_BLOCK_SIZE];
		int[] r0 = new int[CF_BLOCK_SIZE];
		for (int i = 0, j = CF_BLOCK_SIZE; i < CF_BLOCK_SIZE; i++, j++) {
			l0[i] = in[IP[i] - 1];
			r0[i] = in[IP[j] - 1];
		}

		// Ln = Rn-1
		// Rnn = Ln-1(+)f(Rn-1,Kn)
		int[] l1 = r0;
		int[] r1 = xor(l0, f(r0, k1));
		int[] l2 = r1;
		int[] r2 = xor(l1, f(r1, k2));
		int[] l3 = r2;
		int[] r3 = xor(l2, f(r2, k3));
		int[] l4 = r3;
		int[] r4 = xor(l3, f(r3, k4));
		int[] l5 = r4;
		int[] r5 = xor(l4, f(r4, k5));
		int[] l6 = r5;
		int[] r6 = xor(l5, f(r5, k6));
		int[] l7 = r6;
		int[] r7 = xor(l6, f(r6, k7));
		int[] l8 = r7;
		int[] r8 = xor(l7, f(r7, k8));
		int[] l9 = r8;
		int[] r9 = xor(l8, f(r8, k9));
		int[] l10 = r9;
		int[] r10 = xor(l9, f(r9, k10));
		int[] l11 = r10;
		int[] r11 = xor(l10, f(r10, k11));
		int[] l12 = r11;
		int[] r12 = xor(l11, f(r11, k12));
		int[] l13 = r12;
		int[] r13 = xor(l12, f(r12, k13));
		int[] l14 = r13;
		int[] r14 = xor(l13, f(r13, k14));
		int[] l15 = r14;
		int[] r15 = xor(l14, f(r14, k15));
		int[] l16 = r15;
		int[] r16 = xor(l15, f(r15, k16));

		// The computation which uses the permuted input block as its input to
		// produce the preoutput block consists, but for a final interchange of
		// blocks, of 16 iterations of a calculation that is described below in
		// terms of the cipher function f which operates on two blocks, one of
		// 32 bits and one of 48 bits, and produces a block of 32 bits.
		// The preoutput block is then R16L16.
		// The output of that computation, called the preoutput, is then
		// subjected to the following permutation which is the inverse of the
		// initial permutation.
		int ip1;
		for (int i = 0; i < CIPHER_BLOCK_SIZE; i++) {
			ip1 = IP_1[i] - 1;
			out[i] = (ip1 < CF_BLOCK_SIZE) ? r16[ip1] : l16[ip1 - CF_BLOCK_SIZE];
		}
	}

	private static long toLong(byte[] b) {
		long l = 0;
		for (int i = 0; i < 8; i++) {
			l <<= 8;
			l |= b[i] & 0xFFL;
		}
		return l;
	}

	/**
	 * Rotate bits to the left, with the most-significant bits being moved into
	 * the least-significant bits.
	 * <p>
	 *<code>[1 2 3 4 5] -> [2 3 4 5 1]</code>
	 */
	private void rotateLeft(int[] b) {
		int len1 = b.length - 1;
		int temp = b[0];
		for (int i = 0, j = i + 1; i < len1; i++, j++) {
			b[i] = b[j];
		}
		b[len1] = temp;
	}

	/**
	 * <em>Bits 8, 16,..., 64 are for use in assuring that each byte is of odd parity.</em>
	 * 
	 * @param n
	 */
	private void oddParity(int[] n) {
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
			n[p++] = parity & 1;
		}
	}

	/**
	 * Key schedule.
	 * <p>
	 * Let <b>KS</b> be a function which takes an integer <em>n</em> in the
	 * range from 1 to 16 and a 64-bit block <b>KEY</b> as input and yields as
	 * output a 48-bit block <b>K<sub>n</sub></b> which is a permuted selection
	 * of bits from <b>KEY</b>.
	 * <p>
	 * (2) <code>K<sub>n</sub> = KS(n,KEY)</code>
	 * 
	 * @param n
	 *            the iteration, from <tt>1</tt> to <tt>16</tt> inclusive.
	 * @param key
	 *            the 64-bit key designated by <b>KEY</b>.
	 */
	private void keySchedule(int[] key) {
		oddParity(key);

		int[] c = new int[KS_BLOCK_SIZE];
		int[] d = new int[KS_BLOCK_SIZE];
		for (int i = 0, j = KS_BLOCK_SIZE; i < KS_BLOCK_SIZE; i++, j++) {
			c[i] = key[PC1[i] - 1];
			d[i] = key[PC1[j] - 1];
		}
		int pc2;

		// n = 1
		for (int i = 0; i < SHIFTS[0]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k1[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 2
		for (int i = 0; i < SHIFTS[1]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k2[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 3
		for (int i = 0; i < SHIFTS[2]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k3[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 4
		for (int i = 0; i < SHIFTS[3]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k4[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 5
		for (int i = 0; i < SHIFTS[4]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k5[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 6
		for (int i = 0; i < SHIFTS[5]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k6[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 7
		for (int i = 0; i < SHIFTS[6]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k7[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 8
		for (int i = 0; i < SHIFTS[7]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k8[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 9
		for (int i = 0; i < SHIFTS[8]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k9[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 10
		for (int i = 0; i < SHIFTS[9]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k10[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 11
		for (int i = 0; i < SHIFTS[10]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k11[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 12
		for (int i = 0; i < SHIFTS[11]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k12[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 13
		for (int i = 0; i < SHIFTS[12]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k13[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 14
		for (int i = 0; i < SHIFTS[13]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k14[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 15
		for (int i = 0; i < SHIFTS[14]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k15[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}

		// n = 16
		for (int i = 0; i < SHIFTS[15]; i++) {
			rotateLeft(c);
			rotateLeft(d);
		}
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			pc2 = PC2[i] - 1;
			k16[i] = (pc2 < KS_BLOCK_SIZE) ? c[pc2] : d[pc2 - KS_BLOCK_SIZE];
		}
	}

	/**
	 * Cipher function. <code>R -> E -> (48 bits) (+) K (48 bits)</code><br>
	 * <code>S1 S2 S3 S4 S5 S6 S7 S8</code><br>
	 * <code>-> P -> (32 bits)</code>
	 * 
	 * @param r
	 *            <b>R<sub>n-1</sub></b> (32 bits).
	 * @param k
	 *            <b>K<sub>n</sub></b> (48 bits).
	 */
	private int[] f(int[] r, int[] k) {
		int[] f = new int[CF_BLOCK_SIZE];

		int[] b = new int[KEY_BLOCK_SIZE];
		for (int i = 0; i < KEY_BLOCK_SIZE; i++) {
			b[i] = r[E[i] - 1] ^ k[i];
		}

		// Each of the unique selection functions S1,S2,...,S8, takes a 6-bit
		// block as input and yields a 4-bit block as output.
		// That block is the output S1(B) of S1 for the input B.
		int[][] s;
		int bi = 0;
		int b4;
		int i0, i1, j0, j1, j2, j3;
		int row;
		int col;
		int[] sb = new int[CF_BLOCK_SIZE];
		int sbi = 0;

		for (int si = 0; si < S.length; si++) {
			s = S[si];

			i1 = b[bi++];
			j3 = b[bi++];
			j2 = b[bi++];
			j1 = b[bi++];
			j0 = b[bi++];
			i0 = b[bi++];
			row = (i1 << 1) | i0;
			col = (j3 << 3) | (j2 << 2) | (j1 << 1) | j0;

			b4 = s[row][col];
			sb[sbi++] = (b4 >> 3) & 1;
			sb[sbi++] = (b4 >> 2) & 1;
			sb[sbi++] = (b4 >> 1) & 1;
			sb[sbi++] = (b4 >> 0) & 1;
		}

		// The permutation function P yields a 32-bit output from a 32-bit
		// input by permuting the bits of the input block.
		for (int i = 0; i < CF_BLOCK_SIZE; i++) {
			f[i] = sb[P[i] - 1];
		}

		return f;
	}

	/**
	 * eXlusive OR (XOR).<br>
	 * "where (+) denotes bit-by-bit addition modulo 2."
	 */
	private int[] xor(int[] a, int[] b) {
		int len = Math.min(a.length, b.length);
		int[] x = new int[len];
		for (int i = 0; i < len; i++) {
			x[i] = (a[i] ^ b[i]) & 1;
		}
		return x;
	}

	public static void main(String[] args) {
		try {
			long key = 0x0123456789abcdefL;
			byte[] keyBytes = toBytes(key);
			DESKeySpec keySpec = new DESKeySpec(keyBytes);
			// assertNotNull(keySpec);

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// assertNotNull(keyFactory);
			SecretKey sk = keyFactory.generateSecret(keySpec);
			// assertNotNull(sk);

			Cipher cipherSun = Cipher.getInstance("DES");
			// assertNotNull(cipherSun);
			cipherSun.init(Cipher.ENCRYPT_MODE, sk);

			DESCrypt cipherNcu = new DESCrypt();
			// assertNotNull(cipherNcu);
			cipherNcu.setKey(sk);

			long plaintext = 0x4e6f772069732074L;
			long ciphertext = 0x3fa40e8a984d4815L;
			System.out.println(ciphertext);
			byte[] data = toBytes(plaintext);
			// assertNotNull(data);
			byte[] encSun = cipherSun.doFinal(data);
			// assertNotNull(encSun);
			long encSunL = toLong(encSun);
			System.out.println(encSunL);

			long encNcuL = cipherNcu.encipher(plaintext);
			System.out.println(encNcuL);
			// assertNotNull(encNcu);

			// System.out.println("encSun=" + toLong(encSun));
			// System.out.println("encNcu=" + toLong(encNcu));
			// assertEquals(encSun, encNcu);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static byte[] toBytes(long l) {
		byte[] b = new byte[8];
		// assertNotNull(b);
		// assertEquals(8, b.length);
		for (int i = 7; i >= 0; i--, l >>>= 8) {
			b[i] = (byte) (l & 0xFF);
		}
		// assertEquals(0L, l);
		return b;
	}

	/**
	 * Bit 0 is at index <tt>n - 1</tt>.
	 * 
	 * @param b
	 * @return
	 */
	private static long fromBits(int[] b) {
		long l = 0;
		for (int i = 0; i < CIPHER_BLOCK_SIZE; i++) {
			l <<= 1;
			l |= b[i] & 0x1L;
		}
		return l;
	}

	/**
	 * Bit 0 is at index <tt>n - 1</tt>.
	 * 
	 * @param l
	 * @return
	 */
	private static int[] toBits(long l) {
		int[] b = new int[CIPHER_BLOCK_SIZE];
		for (int i = CIPHER_BLOCK_SIZE - 1; i >= 0; i--) {
			b[i] = (int) (l & 0x1L);
			l >>= 1;
		}
		return b;
	}
}
