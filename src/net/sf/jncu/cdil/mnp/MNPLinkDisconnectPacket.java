package net.sf.jncu.cdil.mnp;

/**
 * MNP Link Disconnect packet.
 * 
 * @author moshew
 */
public class MNPLinkDisconnectPacket extends MNPPacket {

	private byte reasonCode;
	private byte userCode;

	/**
	 * Creates a new MNP LD packet.
	 */
	public MNPLinkDisconnectPacket() {
		super();
	}

	@Override
	public int deserialize(byte[] payload) {
		int offset = super.deserialize(payload);

		offset += 2;
		reasonCode = payload[offset++];

		if (headerLength == 7) {
			offset += 2;
			userCode = payload[offset++];
		}

		return offset;
	}

	@Override
	public byte[] serialize() {
		if (userCode == 0) {
			return new byte[] { 0x05, LD, 0x00, 0x00, reasonCode };
		}
		return new byte[] { 0x07, LD, 0x00, 0x00, reasonCode, 0x00, 0x00, userCode };
	}
}
