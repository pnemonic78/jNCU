package net.sf.jncu.crypto;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Newton DES cryptography.
 * 
 * @author moshew
 */
public class DESNewton {

	/** Cipher block size. */
	private static final int CIPHER_BLOCK_SIZE = 64;

	private SecretKeyFactory keyFactory;
	private Cipher cipher;

	/**
	 * Creates a new DES for Newton.
	 */
	public DESNewton() {
		super();
	}

	/**
	 * Get the cipher.
	 * 
	 * @return the cipher.
	 */
	public Cipher getCipher() {
		return cipher;
	}

	/**
	 * Initializes this cipher with a key.
	 * 
	 * @param opmode
	 *            the operation mode of this cipher.
	 * @param key
	 *            the key.
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 */
	public void init(int opmode, long key) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException,
			BadPaddingException, NoSuchPaddingException {
		if (cipher == null) {
			cipher = Cipher.getInstance("DES");
		}

		key <<= 1;
		byte[] keyBytes = toBytesBE(key);
		DESKeySpec keySpec = new DESKeySpec(keyBytes);
		SecretKey skey = getKeyFactory().generateSecret(keySpec);
		cipher.init(opmode, skey);

		byte[] zero = toBytesBE(0L);
		byte[] tmpKeyBytes = cipher.doFinal(zero);
		long tmpKey = toLongBE(tmpKeyBytes);
		int[] tmpKeyBits = toBitsBE(tmpKey);
		oddParity(tmpKeyBits);
		key = tmpKey | fromBitsBE(tmpKeyBits);

		key <<= 1;
		keyBytes = toBytesBE(key);
		keySpec = new DESKeySpec(keyBytes);
		skey = keyFactory.generateSecret(keySpec);
		cipher.init(opmode, skey);
	}

	/**
	 * Initializes this cipher with a key.
	 * 
	 * @param opmode
	 *            the operation mode of this cipher.
	 * @param key
	 *            the key.
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 */
	public void init(int opmode, Key key) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException,
			BadPaddingException, NoSuchPaddingException {
		byte[] keyBytes = key.getEncoded();
		long keyAsLong = toLongBE(keyBytes);
		init(opmode, keyAsLong);
	}

	/**
	 * Initializes this cipher with a key.
	 * 
	 * @param opmode
	 *            the operation mode of this cipher.
	 * @param keySpec
	 *            the key specification.
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 */
	private void init(int opmode, KeySpec keySpec) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException,
			BadPaddingException, NoSuchPaddingException {
		SecretKey skey = getKeyFactory().generateSecret(keySpec);
		init(opmode, skey);
	}

	/**
	 * Get the secret key factory instance.
	 * 
	 * @return the key factory.
	 * @throws NoSuchAlgorithmException
	 */
	private SecretKeyFactory getKeyFactory() throws NoSuchAlgorithmException {
		if (keyFactory == null) {
			keyFactory = SecretKeyFactory.getInstance("DES");
		}
		return keyFactory;
	}

	/**
	 * Convert the 64-bit number of an array of bytes arranged in Big Endian
	 * order.
	 * 
	 * @param l
	 *            the number.
	 * @return the array of 8 bytes.
	 */
	private byte[] toBytesBE(long l) {
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
	 * @param b
	 *            the array of 8 bytes.
	 * @return the 64-bit number.
	 */
	private long toLongBE(byte[] b) {
		long l = 0;
		for (int i = 0; i < 8; i++) {
			l <<= 8;
			l |= (b[i] & 0xFFL);
		}
		return l;
	}

	/**
	 * Convert a 64-bit Big Endian number to 64 separate bits.<br>
	 * MSB is at index <tt>0</tt>.
	 * 
	 * @param l
	 *            the number.
	 * @return the array of bits.
	 */
	private int[] toBitsBE(long l) {
		int[] b = new int[CIPHER_BLOCK_SIZE];
		for (int i = CIPHER_BLOCK_SIZE - 1; i >= 0; i--) {
			b[i] = (int) (l & 0x1L);
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
	private long fromBitsBE(int[] b) {
		long l = 0;
		for (int i = 0; i < CIPHER_BLOCK_SIZE; i++) {
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
	 *            the array of bits.
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
}
