package net.sf.jncu.cdil.mnp;


/**
 * MNP packet.
 * 
 * @author moshew
 */
public abstract class MNPPacket {

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
	 */
	public MNPPacket() {
		super();
	}

	/**
	 * Parse the packet.
	 * 
	 * @param payload
	 *            the payload.
	 */
	public void deserialize(byte[] payload) {
		if (payload[0] == 255) {
			headerLength = (payload[1] << 8) + payload[2];
			type = payload[3];
		} else {
			headerLength = payload[0];
			type = payload[1];
		}
	}

}
