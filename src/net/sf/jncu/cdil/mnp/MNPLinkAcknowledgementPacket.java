package net.sf.jncu.cdil.mnp;

/**
 * MNP Link Acknowledgement packet.
 * 
 * @author moshew
 */
public class MNPLinkAcknowledgementPacket extends MNPPacket {

	private byte sequence;
	private byte credit;

	/**
	 * Creates a new MNP Link Acknowledgement packet.
	 */
	public MNPLinkAcknowledgementPacket() {
		super();
	}

}
