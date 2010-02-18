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

	private byte type;
	private int headerLength;
	private int transmitted;

	/**
	 * Creates a new MNP packet.
	 * 
	 * @param type
	 *            the type.
	 * @param headerLength
	 *            the default header length.
	 */
	public MNPPacket(byte type, int headerLength) {
		super();
		this.type = type;
		this.headerLength = headerLength;
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
			headerLength = ((payload[offset++] & 0xFF) << 8) + (payload[offset++] & 0xFF);
			type = payload[offset++];
		} else {
			headerLength = payload[offset++] & 0xFF;
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

	/**
	 * Get the type.
	 * 
	 * @return the type.
	 */
	public byte getType() {
		return type;
	}

	/**
	 * Get the header length.
	 * 
	 * @return the header length.
	 */
	public int getHeaderLength() {
		return headerLength;
	}

	/**
	 * Get the transmitted.
	 * 
	 * @return the transmitted.
	 */
	public int getTransmitted() {
		return transmitted;
	}

	/**
	 * Set the transmitted.
	 * 
	 * @param transmitted
	 *            the transmitted.
	 */
	public void setTransmitted(int transmitted) {
		this.transmitted = transmitted;
	}

}
