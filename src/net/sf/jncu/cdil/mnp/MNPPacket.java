package net.sf.jncu.cdil.mnp;

import java.io.Serializable;

/**
 * MNP packet.
 * 
 * @author moshew
 */
public abstract class MNPPacket implements Serializable {

	/** Link Request packet. */
	public static final byte LR = 0x01;
	/** Link Disconnect packet. */
	public static final byte LD = 0x02;
	/** Link Transfer packet. */
	public static final byte LT = 0x04;
	/** Link Acknowledgement packet. */
	public static final byte LA = 0x05;

	protected byte type;
	protected int headerLength;
	private int transmitted;

	/**
	 * Creates a new MNP packet.
	 */
	public MNPPacket() {
		super();
	}

	/**
	 * Parse the packet.
	 * 
	 * @param payload
	 *            the payload.
	 * @return the array offset.
	 */
	public int deserialize(byte[] payload) {
		int offset = 0;
		if (payload[offset++] == 255) {
			headerLength = (payload[offset++] << 8) + payload[offset++];
			type = payload[offset++];
		} else {
			headerLength = payload[offset++];
			type = payload[offset++];
		}
		return offset;
	}

	/**
	 * Serialise the packet.
	 * 
	 * @return the payload.
	 */
	public abstract byte[] serialize();

}
