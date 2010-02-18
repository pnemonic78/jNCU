package net.sf.jncu.cdil.mnp;

/**
 * MNP Link Request packet.
 * 
 * @author moshew
 */
public class MNPLinkRequestPacket extends MNPPacket {

	private byte framingMode = 0x02;
	private byte maxOutstanding = 0x08;
	private short maxInfoLength = 0x0040;
	private byte dataPhaseOpt = 0x03;

	/**
	 * Creates a new MNP LR packet.
	 */
	public MNPLinkRequestPacket() {
		super(LR, 0x17);
	}

	@Override
	public int deserialize(byte[] payload) {
		int offset = super.deserialize(payload);

		// remove fixed fields
		offset += 9;

		offset += 2;
		setFramingMode(payload[offset++]);

		offset += 2;
		setMaxOutstanding(payload[offset++]);

		offset += 2;
		int maxInfoLength = payload[offset++] & 0xFF;
		maxInfoLength = ((payload[offset++] & 0xFF) << 8) | maxInfoLength;
		setMaxInfoLength((short) maxInfoLength);

		offset += 2;
		setDataPhaseOpt(payload[offset++]);

		return offset;
	}

	@Override
	public byte[] serialize() {
		byte[] payload = new byte[] { 0x17, LR, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, framingMode, 0x03, 0x01,
				maxOutstanding, 0x04, 0x02, (byte) (maxInfoLength & 0xFF), (byte) ((maxInfoLength >> 8) & 0xFF), 0x08, 0x01, dataPhaseOpt };
		return payload;
	}

	/**
	 * Get the framing mode.
	 * 
	 * @return the framingMode the framing mode.
	 */
	public byte getFramingMode() {
		return framingMode;
	}

	/**
	 * Set the framing mode.
	 * 
	 * @param framingMode
	 *            the framing mode.
	 */
	public void setFramingMode(byte framingMode) {
		this.framingMode = framingMode;
	}

	/**
	 * Get the maximum outstanding.
	 * 
	 * @return the maxOutstanding the maximum.
	 */
	public byte getMaxOutstanding() {
		return maxOutstanding;
	}

	/**
	 * Set the maximum outstanding.
	 * 
	 * @param maxOutstanding
	 *            the maximum.
	 */
	public void setMaxOutstanding(byte maxOutstanding) {
		this.maxOutstanding = maxOutstanding;
	}

	/**
	 * Get the maximum information length.
	 * 
	 * @return the maximum length.
	 */
	public short getMaxInfoLength() {
		return maxInfoLength;
	}

	/**
	 * Set the maximum information length.
	 * 
	 * @param maxInfoLength
	 *            the maximum length.
	 */
	public void setMaxInfoLength(short maxInfoLength) {
		this.maxInfoLength = maxInfoLength;
	}

	/**
	 * Get the data phase.
	 * 
	 * @return the data phase.
	 */
	public byte getDataPhaseOpt() {
		return dataPhaseOpt;
	}

	/**
	 * Set the data phase.
	 * 
	 * @param dataPhaseOpt
	 *            the data phase.
	 */
	public void setDataPhaseOpt(byte dataPhaseOpt) {
		this.dataPhaseOpt = dataPhaseOpt;
	}

}
