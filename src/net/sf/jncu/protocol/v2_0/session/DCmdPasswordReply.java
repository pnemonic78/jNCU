package net.sf.jncu.protocol.v2_0.session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDPassword</tt>
 * 
 * <pre>
 * 'pass'
 * length
 * encrypted key
 * </pre>
 * 
 * @author moshew
 */
public class DCmdPasswordReply extends DockCommandToNewton {

	public static final String COMMAND = DCmdPassword.COMMAND;

	private long encryptedKey;

	/**
	 * Creates a new command.
	 */
	public DCmdPasswordReply() {
		super(COMMAND);
	}

	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ntohl(getEncryptedKey(), data);
		return data;
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
	public void setEncryptedKey(long encryptedKey) {
		this.encryptedKey = encryptedKey;
	}

}
