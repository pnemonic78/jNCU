package net.sf.jncu.cdil.mnp;

/**
 * MNP packet factory.
 * 
 * @author moshew
 */
public class MNPPacketFactory {

	/** Link Request packet. */
	public static final byte PACKET_TYPE_LR = 0x01;
	/** Link Disconnect packet. */
	public static final byte PACKET_TYPE_LD = 0x02;
	/** Link Transfer packet. */
	public static final byte PACKET_TYPE_LT = 0x04;
	/** Link Acknowledgement packet. */
	public static final byte PACKET_TYPE_LA = 0x05;

	/**
	 * Creates a new factory.
	 */
	public MNPPacketFactory() {
		super();
	}

}
