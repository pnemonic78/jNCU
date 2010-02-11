package net.sf.jncu.protocol.v2_0.session;

import javax.xml.ws.ProtocolException;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * Request to dock.
 * 
 * @author moshew
 */
public class DRequestToDock extends DockCommandFromNewton {

	private int protocol;

	/**
	 * Creates a new request to dock.
	 */
	public DRequestToDock() {
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
	protected void decode(byte[] frame, int offset) {
		int length = readWord(frame, offset);
		if (length != LENGTH_WORD) {
			throw new ProtocolException();
		}
		offset += LENGTH_WORD;
		int protocol = readWord(frame, offset);
		offset += LENGTH_WORD;
		setLength(length);
		setProtocol(protocol);
	}
}
