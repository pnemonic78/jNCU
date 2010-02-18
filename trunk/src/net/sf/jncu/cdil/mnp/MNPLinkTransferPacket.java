package net.sf.jncu.cdil.mnp;

/**
 * MNP Link Transfer packet.
 * 
 * @author moshew
 */
public class MNPLinkTransferPacket extends MNPPacket {

	private byte[] data;
	private byte sequence;

	/**
	 * Creates a new MNP LT packet.
	 */
	public MNPLinkTransferPacket() {
		super();
	}

	@Override
	public int deserialize(byte[] payload) {
		int offset = super.deserialize(payload);

		sequence = payload[offset++];
		data = new byte[payload.length - offset];
		System.arraycopy(payload, offset, data, 0, data.length);
		offset += data.length;

		return offset;
	}

	@Override
	public byte[] serialize() {
		byte[] payload = new byte[3 + data.length];
		payload[0] = 2;
		payload[1] = LT;
		payload[2] = sequence;
		System.arraycopy(data, 0, payload, 3, data.length);
		return payload;
	}

}
