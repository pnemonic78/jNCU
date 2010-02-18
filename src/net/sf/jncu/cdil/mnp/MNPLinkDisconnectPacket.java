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
		super(LD, 5);
	}

	@Override
	public int deserialize(byte[] payload) {
		int offset = super.deserialize(payload);

		offset += 2;
		setReasonCode(payload[offset++]);

		if (getHeaderLength() == 7) {
			offset += 2;
			setUserCode(payload[offset++]);
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

	/**
	 * Get the reason code.
	 * 
	 * @return the reasonCode the reason code.
	 */
	public byte getReasonCode() {
		return reasonCode;
	}

	/**
	 * Set the reason code.
	 * 
	 * @param reasonCode
	 *            the reason code.
	 */
	public void setReasonCode(byte reasonCode) {
		this.reasonCode = reasonCode;
	}

	/**
	 * Get the user code.
	 * 
	 * @return the userCode the user code.
	 */
	public byte getUserCode() {
		return userCode;
	}

	/**
	 * Set the user code.
	 * 
	 * @param userCode
	 *            the user code.
	 */
	public void setUserCode(byte userCode) {
		this.userCode = userCode;
	}

}
