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
		super(LT, 2);
	}

	@Override
	public int deserialize(byte[] payload) {
		int offset = super.deserialize(payload);

		setSequence(payload[offset++]);
		byte[] data = new byte[payload.length - offset];
		System.arraycopy(payload, offset, data, 0, data.length);
		offset += data.length;
		setData(data);

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

	/**
	 * Get the data.
	 * 
	 * @return the data.
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Set the data.
	 * 
	 * @param data
	 *            the data.
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * Get the sequence number.
	 * 
	 * @return the sequence.
	 */
	public byte getSequence() {
		return sequence;
	}

	/**
	 * Set the sequence number.
	 * 
	 * @param sequence
	 *            the sequence.
	 */
	public void setSequence(byte sequence) {
		this.sequence = sequence;
	}

}
