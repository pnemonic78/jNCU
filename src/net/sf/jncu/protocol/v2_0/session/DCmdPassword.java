package net.sf.jncu.protocol.v2_0.session;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDPassword</tt><br>
 * This command returns the key received in the <tt>kDInitiateDocking</tt>
 * message encrypted using the password.
 * 
 * <pre>
 * 'pass'
 * length
 * encrypted key
 * </pre>
 * 
 * @author moshew
 */
public class DCmdPassword extends DockCommandFromNewton {

	public static final String COMMAND = "pass";

	private long encryptedKey;

	/**
	 * Creates a new command.
	 */
	public DCmdPassword() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		long keyHi = htonl(data) & 0xFFFFFFFFL;
		long keyLo = htonl(data) & 0xFFFFFFFFL;
		setEncryptedKey((keyHi << 32) | keyLo);
	}

	/**
	 * Get the encrypted key.
	 * 
	 * @return the encrypted key.
	 */
	public long getEncryptedKey() {
		return encryptedKey;
	}

	/**
	 * Set the encrypted key.
	 * 
	 * @param encryptedKey
	 *            the encrypted key.
	 */
	protected void setEncryptedKey(long encryptedKey) {
		this.encryptedKey = encryptedKey;
	}

	public boolean verify(long challenge) {
		// TODO implement me!
		return true;
	}
}
