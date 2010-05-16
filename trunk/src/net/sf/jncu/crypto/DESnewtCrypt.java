package net.sf.jncu.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * @author moshew
 */
public class DESnewtCrypt extends DESCrypt {

	/**
	 * Newton DES cryptography.
	 */
	public DESnewtCrypt() {
		super();
	}

	public static void main(String[] args) {
		try {
			long key = 0x57406860626d7464L;
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

			DESCrypt cipherNcu = new DESnewtCrypt();
			// assertNotNull(cipherNcu);
			cipherNcu.setKey(keySpec);

			long plaintext = 0x000f1f6bff82ddcbL;
			long ciphertext = 0xdb2406e5c107365aL;
			System.out.println("c " + toHex(ciphertext));
			byte[] data = toBytes(plaintext);
			// assertNotNull(data);
			byte[] encSun = cipherSun.doFinal(data);
			// assertNotNull(encSun);
			long encSunL = toLong(encSun);
			System.out.println("s " + toHex(encSunL));

			long encNcuL = cipherNcu.encrypt(plaintext);
			System.out.println("n " + toHex(encNcuL));
			// assertNotNull(encNcu);

			// assertEquals(encSun, encNcu);
		} catch (Exception e) {
			e.printStackTrace();
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

	@Override
	public void setKey(long key) {
		key <<= 1;
		super.setKey(key);

		long tmpKey = encrypt(0L);
		int[] tmpKeyBits = toBitsBE(tmpKey);
		oddParity(tmpKeyBits);
		key = tmpKey | fromBitsBE(tmpKeyBits);

		key <<= 1;
		super.setKey(key);
	}

	private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static String toHex(long l) {
		StringBuffer s = new StringBuffer();
		int nibble;
		char c;
		for (int i = 0; (i < 64) && (l != 0); i += 4) {
			nibble = (int) (l & 0xF);
			c = HEX[nibble];
			s.insert(0, c);
			l >>>= 4;
		}

		return s.toString();
	}

}
