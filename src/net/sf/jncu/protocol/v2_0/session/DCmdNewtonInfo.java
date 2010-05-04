package net.sf.jncu.protocol.v2_0.session;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDNewtonInfo</tt><br>
 * This command is used to negotiate the real protocol version. See
 * <tt>kDDesktopInfo</tt> for more info. The password key is used as part of the
 * password verification.
 * 
 * <pre>
 * 'ninf'
 * length
 * protocol version
 * encrypted key
 * </pre>
 */
public class DCmdNewtonInfo extends DockCommandFromNewton {

	public static final String COMMAND = "ninf";

	private int protocolVersion;
	private long encryptedKey;

	/**
	 * Creates a new command.
	 */
	public DCmdNewtonInfo() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		setProtocolVersion(htonl(data));
		long keyHi = htonl(data) & 0xFFFFFFFFL;
		long keyLo = htonl(data) & 0xFFFFFFFFL;
		setEncryptedKey((keyHi << 32) | keyLo);
	}

	/**
	 * Get the protocol version.
	 * 
	 * @return the protocol version.
	 */
	public int getProtocolVersion() {
		return protocolVersion;
	}

	/**
	 * Set the protocol version.
	 * 
	 * @param protocolVersion
	 *            the protocol version.
	 */
	protected void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
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

}
