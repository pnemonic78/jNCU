package net.sf.jncu.protocol.v1_0;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDRequestToDock</tt><br>
 * Ask desktop to start docking process.<br>
 * This command is sent to a docker that the junior wishes to connect with (on
 * the network, serial, etc.). The Newt expects a <tt>kDInitiateDocking</tt>
 * command in response. The protocol version is the version of the messaging
 * protocol that's being used.
 * 
 * <pre>
 * 'rtdk'
 * length
 * protocol version
 * </pre>
 */
public class DRequestToDock extends DockCommandFromNewton {

	public static final String COMMAND = "rtdk";

	/** The protocol version. */
	public static final int kProtocolVersion = 9;

	private int protocol;

	/**
	 * Creates a new command.
	 */
	public DRequestToDock() {
		super(COMMAND);
	}

	/**
	 * Get the protocol version.
	 * 
	 * @return the protocol version.
	 */
	public int getProtocol() {
		return protocol;
	}

	/**
	 * Set the protocol version.
	 * 
	 * @param protocol
	 *            the protocol version.
	 */
	protected void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		int protocol = htonl(data);
		setProtocol(protocol);
		if (protocol != kProtocolVersion) {
			throw new ProtocolException();
		}
	}

}
