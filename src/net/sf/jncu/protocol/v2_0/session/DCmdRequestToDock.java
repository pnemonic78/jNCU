package net.sf.jncu.protocol.v2_0.session;

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
	protected void decodeData(byte[] data, int offset, int length) {
		// Bug that sometimes length is read as [0x00,0x01,0x00,0x00,0x04]
		// instead of [0x00,0x00,0x00,0x04]
		if (length == 0x010000) {
			offset -= 3;
			data[offset] = 0;
			length = htonl(data, offset);
			setLength(length);
			offset += LENGTH_WORD;
		}
		int protocol = htonl(data, offset);
		setProtocol(protocol);
		offset += LENGTH_WORD;
	}
}
