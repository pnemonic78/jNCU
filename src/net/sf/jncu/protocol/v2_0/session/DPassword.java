package net.sf.jncu.protocol.v2_0.session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;

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
public class DPassword extends DockCommandFromNewton implements IDockCommandToNewton {

	public static final String COMMAND = "pass";

	/** <tt>kDBadPassword</tt>. */
	public static final int ERROR_BAD_PASSWORD = -28022;
	/** <tt>kDPWWrong</tt>. */
	public static final int ERROR_RETRY_PASSWORD = -28023;

	private long encryptedKey;

	/**
	 * Creates a new command.
	 */
	public DPassword() {
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

	public byte[] getPayload() {
		IDockCommandToNewton cmd = new DockCommandToNewton(COMMAND) {

			@Override
			protected ByteArrayOutputStream getCommandData() throws IOException {
				ByteArrayOutputStream data = new ByteArrayOutputStream();
				ntohl(getEncryptedKey(), data);
				return data;
			}
		};
		return cmd.getPayload();
	}
}
