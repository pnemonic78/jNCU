package net.sf.jncu.crypto;

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

}
