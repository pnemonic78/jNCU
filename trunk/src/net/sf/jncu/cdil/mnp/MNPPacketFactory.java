package net.sf.jncu.cdil.mnp;

/**
 * MNP packet factory.
 * 
 * @author moshew
 */
public class MNPPacketFactory {

	private static MNPPacketFactory instance;

	/**
	 * Creates a new factory.
	 */
	protected MNPPacketFactory() {
		super();
	}

	/**
	 * Get the factory instance.
	 * 
	 * @return the factory.
	 */
	public static MNPPacketFactory getInstance() {
		if (instance == null) {
			instance = new MNPPacketFactory();
		}
		return instance;
	}

	/**
	 * Create a MNP link packet.
	 * 
	 * @param type
	 *            the link type.
	 * @return the packet.
	 * @see MNPPacket#LA
	 * @see MNPPacket#LD
	 * @see MNPPacket#LR
	 * @see MNPPacket#LT
	 */
	public MNPPacket createLinkPacket(int type) {
		switch (type) {
		case MNPPacket.LA:
			return new MNPLinkAcknowledgementPacket();
		case MNPPacket.LD:
			return new MNPLinkDisconnectPacket();
		case MNPPacket.LR:
			return new MNPLinkRequestPacket();
		case MNPPacket.LT:
			return new MNPLinkTransferPacket();
		}
		throw new IllegalArgumentException("invalid type " + type);
	}
}
