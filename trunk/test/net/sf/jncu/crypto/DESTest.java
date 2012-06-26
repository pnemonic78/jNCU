package net.sf.jncu.crypto;

import javax.crypto.Cipher;

import org.junit.Test;

import net.sf.junit.SFTestCase;

/**
 * DES test case.
 * 
 * @author moshew
 */
public class DESTest extends SFTestCase {

	private static final long NEWTON_DEFAULT_KEY = DESNewton.NEWTON_DEFAULT_KEY;

	/**
	 * Creates a new test case.
	 */
	public DESTest() {
		super();
	}

	@Test
	public void testNewtonCipher() throws Exception {
		DESNewton newt = new DESNewton();
		assertNotNull(newt);
		newt.init(Cipher.ENCRYPT_MODE);
		Cipher cipher = newt.getCipher();
		assertNotNull(cipher);

		long plaintext = 0x000f1f6bff82ddcbL;
		long ciphertext = 0xdb2406e5c107365aL;
		long encL = newt.cipher(plaintext);
		assertEquals(ciphertext, encL);
	}

	protected byte[] toBytes(long l) {
		byte[] b = new byte[8];
		assertNotNull(b);
		assertEquals(8, b.length);
		for (int i = 7; i >= 0; i--, l >>>= 8) {
			b[i] = (byte) (l & 0xFF);
		}
		assertEquals(0L, l);
		return b;
	}

	protected long toLong(byte[] b) {
		assertNotNull(b);
		assertTrue(8 <= b.length);
		long l = 0;
		for (int i = 0; i < 8; i++) {
			l <<= 8;
			l |= (b[i] & 0xFFL);
		}
		return l;
	}

	protected String toHex(long l) {
		StringBuffer s = new StringBuffer(16);
		int hi = (int) ((l >> 32) & 0xFFFFFFFFL);
		int lo = (int) ((l >> 0) & 0xFFFFFFFFL);
		s.append(Integer.toHexString(hi));
		s.append(Integer.toHexString(lo));
		while (s.length() < 16) {
			s.insert(0, '0');
		}
		return s.toString();
	}

	@Test
	public void testChallenge() throws Exception {
		long challengeDesktop = 0xf43291f715464670L;
		long challengeNewton = 0x540f04ffc4a990L;
		long challengeNewtonCiphered = 0xf51c31da8aa66d2cL;
		long challenge = 0x81aa29861ea0c5d2L;

		DESNewton newt = new DESNewton();
		assertNotNull(newt);

		newt.init(Cipher.ENCRYPT_MODE, challengeDesktop);
		long encL00 = newt.cipher(challenge);
		assertTrue(encL00 != 0);

		newt.init(Cipher.ENCRYPT_MODE, challengeNewton);
		long encL01 = newt.cipher(challenge);
		assertTrue(encL01 != 0);

		newt.init(Cipher.ENCRYPT_MODE, challengeNewtonCiphered);
		long encL02 = newt.cipher(challenge);
		assertTrue(encL02 != 0);

		newt.init(Cipher.ENCRYPT_MODE, 0);
		long encL10 = newt.cipher(challengeDesktop);
		assertTrue(encL10 != 0);

		newt.init(Cipher.ENCRYPT_MODE, 0);
		long encL11 = newt.cipher(challengeNewton);
		assertTrue(encL11 != 0);

		newt.init(Cipher.ENCRYPT_MODE, 0);
		long encL12 = newt.cipher(challengeNewtonCiphered);
		assertTrue(encL12 != 0);

		newt.init(Cipher.ENCRYPT_MODE, 0);
		long encL13 = newt.cipher(challenge);
		assertTrue(encL13 != 0);

		newt.init(Cipher.ENCRYPT_MODE, challengeDesktop);
		long encL20 = newt.cipher(0);
		assertTrue(encL20 != 0);

		newt.init(Cipher.ENCRYPT_MODE, challengeNewton);
		long encL21 = newt.cipher(0);
		assertTrue(encL21 != 0);

		newt.init(Cipher.ENCRYPT_MODE, challengeNewtonCiphered);
		long encL22 = newt.cipher(0);
		assertTrue(encL22 != 0);

		newt.init(Cipher.ENCRYPT_MODE, challenge);
		long encL23 = newt.cipher(0);
		assertTrue(encL23 != 0);

		newt.init(Cipher.ENCRYPT_MODE, NEWTON_DEFAULT_KEY);
		long encL24 = newt.cipher(0);
		assertTrue(encL24 != 0);

		newt.init(Cipher.ENCRYPT_MODE, NEWTON_DEFAULT_KEY);
		long encL30 = newt.cipher(challengeDesktop);
		assertTrue(encL30 != 0);
		assertEquals(challenge, encL30);

		newt.init(Cipher.ENCRYPT_MODE, NEWTON_DEFAULT_KEY);
		long encL31 = newt.cipher(challengeNewton);
		assertTrue(encL31 != 0);
		assertEquals(challengeNewtonCiphered, encL31);

		newt.init(Cipher.ENCRYPT_MODE, NEWTON_DEFAULT_KEY);
		long encL32 = newt.cipher(challengeNewtonCiphered);
		assertTrue(encL32 != 0);

		newt.init(Cipher.ENCRYPT_MODE, NEWTON_DEFAULT_KEY);
		long encL33 = newt.cipher(challenge);
		assertTrue(encL33 != 0);

		newt.init(Cipher.ENCRYPT_MODE, challengeNewton);
		long encL40 = newt.cipher(challengeDesktop);
		assertTrue(encL40 != 0);

		newt.init(Cipher.ENCRYPT_MODE, challengeNewton);
		long encL41 = newt.cipher(challengeNewton);
		assertTrue(encL41 != 0);

		newt.init(Cipher.ENCRYPT_MODE, challengeNewton);
		long encL42 = newt.cipher(challengeNewtonCiphered);
		assertTrue(encL42 != 0);

		newt.init(Cipher.ENCRYPT_MODE, challengeNewton);
		long encL43 = newt.cipher(challenge);
		assertTrue(encL43 != 0);
	}
}
