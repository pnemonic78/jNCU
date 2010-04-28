package net.sf.jncu.protocol.v2_0.session;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * Command to request to dock.
 * 
 * @author moshew
 */
public class DCmdRequestToDock extends DockCommandFromNewton {

	private int protocol;

	/**
	 * Creates a new command.
	 */
	public DCmdRequestToDock() {
		super(DockCommandSession.NewtonToDesktop.kDRequestToDock);
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
		if (protocol != 9) {
			throw new ProtocolException();
		}
	}

}
