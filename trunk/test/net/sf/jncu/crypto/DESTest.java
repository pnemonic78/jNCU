package net.sf.jncu.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;

import net.sf.junit.SFTestCase;

/**
 * DES test case.
 * 
 * @author moshew
 */
public class DESTest extends SFTestCase {

	private static final long DEFAULT_KEY = 0x433EFD1C549D76D5L;// 0x4cc7618a9849c4e6L;
	private static final long NEWTON_DEFAULT_KEY = 0x57406860626D7464L;

	/**
	 * Creates a new test case.
	 */
	public DESTest() {
		super();
	}

	public void testDES() throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("DES");
		assertNotNull(generator);
		SecretKey sk = generator.generateKey();
		assertNotNull(sk);
		byte[] skEnc = sk.getEncoded();
		assertNotNull(skEnc);
		assertEquals(DESKeySpec.DES_KEY_LEN, skEnc.length);

		Cipher cipherSun = Cipher.getInstance("DES");
		assertNotNull(cipherSun);
		cipherSun.init(Cipher.ENCRYPT_MODE, sk);

		DESCrypt cipherNcu = new DESCrypt();
		assertNotNull(cipherNcu);
		// cipherNcu.init(Cipher.ENCRYPT_MODE, sk);
		cipherNcu.setKey(sk);

		byte[] data = toBytes(0x123456789ABCDEFL);
		assertNotNull(data);
		byte[] encSun = cipherSun.doFinal(data);
		assertNotNull(encSun);
		long encSunL = toLong(encSun);

		long dataL = toLong(data);
		long encNcuL = cipherNcu.encrypt(dataL);
		byte[] encNcu = toBytes(encNcuL);
		assertNotNull(encNcu);

		assertEquals(encSunL, encNcuL);
	}

	public void testNewtonCipher() throws Exception {
		final long NEWTON_DEFAULT_KEY = 0x57406860626D7464L;
		long key = NEWTON_DEFAULT_KEY;

		DESNewton newt = new DESNewton();
		assertNotNull(newt);
		newt.init(Cipher.ENCRYPT_MODE, key);
		Cipher cipher = newt.getCipher();
		assertNotNull(cipher);

		long plaintext = 0x000f1f6bff82ddcbL;
		long ciphertext = 0xdb2406e5c107365aL;
		byte[] data = toBytes(plaintext);
		assertNotNull(data);
		byte[] enc = cipher.doFinal(data);
		assertNotNull(enc);
		long encL = toLong(enc);
		assertEquals(ciphertext, encL);
	}

	private byte[] toBytes(long l) {
		byte[] b = new byte[8];
		assertNotNull(b);
		assertEquals(8, b.length);
		for (int i = 7; i >= 0; i--, l >>>= 8) {
			b[i] = (byte) (l & 0xFF);
		}
		assertEquals(0L, l);
		return b;
	}

	private long toLong(byte[] b) {
		assertNotNull(b);
		assertTrue(8 <= b.length);
		long l = 0;
		for (int i = 0; i < 8; i++) {
			l <<= 8;
			l |= (b[i] & 0xFFL);
		}
		return l;
	}

	private String toHex(long l) {
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

}
