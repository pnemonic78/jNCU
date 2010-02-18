package net.sf.jncu.cdil.mnp;

/**
 * MNP Link Request packet.
 * 
 * @author moshew
 */
public class MNPLinkRequestPacket extends MNPPacket {

	private byte framing_mode = 0x02;
	private byte max_outstanding = 0x08;
	private short max_info_length = 0x0040;
	private byte data_phase_opt = 0x03;

	/**
	 * Creates a new MNP LR packet.
	 */
	public MNPLinkRequestPacket() {
		super();
	}

	@Override
	public int deserialize(byte[] payload) {
		int offset = super.deserialize(payload);

		// remove fixed fields
		offset += 9;

		offset += 2;
		framing_mode = payload[offset++];

		offset += 2;
		max_outstanding = payload[offset++];

		offset += 2;
		max_info_length = payload[offset++];
		max_info_length = (short) (((payload[offset++] & 0xFF) << 8) | (max_info_length & 0xFF));

		offset += 2;
		data_phase_opt = payload[offset++];

		return offset;
	}

	@Override
	public byte[] serialize() {
		byte[] payload = new byte[] { 0x17, LR, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, framing_mode, 0x03, 0x01,
				max_outstanding, 0x04, 0x02, (byte) (max_info_length & 0xFF), (byte) ((max_info_length >> 8) & 0xFF), 0x08, 0x01, data_phase_opt };
		return payload;
	}

}
