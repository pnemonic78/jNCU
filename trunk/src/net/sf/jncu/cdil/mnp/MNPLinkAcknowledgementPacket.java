package net.sf.jncu.cdil.mnp;

/**
 * MNP Link Acknowledgement packet.
 * 
 * @author moshew
 */
public class MNPLinkAcknowledgementPacket extends MNPPacket {

	private byte sequence;
	private byte credit;
	private byte[] data;

	/**
	 * Creates a new MNP LA packet.
	 */
	public MNPLinkAcknowledgementPacket() {
		super();
	}

	@Override
	public int deserialize(byte[] payload) {
		int offset = super.deserialize(payload);

		sequence = payload[offset++];
		credit = payload[offset++];
		data = new byte[payload.length - offset];
		System.arraycopy(payload, offset, data, 0, data.length);
		offset += data.length;

		return offset;
	}

	@Override
	public byte[] serialize() {
		return new byte[] { 3, LA, sequence, credit };
	}

}
