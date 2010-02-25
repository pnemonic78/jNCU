package net.sf.jncu.cdil.mnp;

public interface MNPPacketListener {

	/**
	 * Notification that a MNP packet was received.
	 * 
	 * @param packet
	 *            the packet.
	 */
	void packetReceived(MNPPacket packet);

}
