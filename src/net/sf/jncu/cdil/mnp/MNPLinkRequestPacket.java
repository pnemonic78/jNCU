package net.sf.jncu.cdil.mnp;

/**
 * MNP Link Request packet.
 * 
 * @author moshew
 */
public class MNPLinkRequestPacket extends MNPPacket {

	private static final int TYPE1 = 0x01;
	private static final int FRAMING_MODE = 0x02;
	private static final int MAX_OUTSTANDING = 0x03;
	private static final int MAX_INFO_LENGTH = 0x04;
	private static final int DATA_PHASE_OPT = 0x08;

	private static final byte TYPE1_LENGTH = 6;
	private static final byte FRAMING_MODE_LENGTH = 1;
	private static final byte MAX_OUTSTANDING_LENGTH = 1;
	private static final byte MAX_INFO_LENGTH_LENGTH = 2;
	private static final byte DATA_PHASE_OPT_LENGTH = 1;

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
		offset++;

		int type;
		int length;
		while (offset < payload.length) {
			type = payload[offset++];
			length = payload[offset++];

			switch (type) {
			case FRAMING_MODE:
				setFramingMode(payload[offset]);
				break;
			case MAX_OUTSTANDING:
				setMaxOutstanding(payload[offset]);
				break;
			case MAX_INFO_LENGTH:
				int maxInfoLength = payload[offset] & 0xFF;
				if (length > 1) {
					maxInfoLength = ((payload[offset + 1] & 0xFF) << 8) | maxInfoLength;
				}
				setMaxInfoLength((short) maxInfoLength);
				break;
			case DATA_PHASE_OPT:
				setDataPhaseOpt(payload[offset]);
				break;
			}

			offset += length;
		}

		return offset;
	}

	@Override
	public byte[] serialize() {
		byte[] payload = new byte[] { 0x17, LR, 0x02, TYPE1, TYPE1_LENGTH, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, FRAMING_MODE, FRAMING_MODE_LENGTH,
				framingMode, MAX_OUTSTANDING, MAX_OUTSTANDING_LENGTH, maxOutstanding, MAX_INFO_LENGTH, MAX_INFO_LENGTH_LENGTH, (byte) (maxInfoLength & 0xFF),
				(byte) ((maxInfoLength >> 8) & 0xFF), DATA_PHASE_OPT, DATA_PHASE_OPT_LENGTH, dataPhaseOpt };
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
