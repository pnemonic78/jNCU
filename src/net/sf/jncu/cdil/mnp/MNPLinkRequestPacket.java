/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 * 
 * http://sourceforge.net/projects/jncu
 * 
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 * 
 */
package net.sf.jncu.cdil.mnp;

/**
 * MNP Link Request packet.
 * 
 * @author moshew
 */
public class MNPLinkRequestPacket extends MNPPacket {

	protected static final byte TYPE1 = 0x01;
	protected static final byte FRAMING_MODE = 0x02;
	protected static final byte MAX_OUTSTANDING = 0x03;
	protected static final byte MAX_INFO_LENGTH = 0x04;
	protected static final byte DATA_PHASE_OPT = 0x08;
	protected static final byte PROTOCOL = 0x0E;
	protected static final byte TYPEC5 = (byte) 0xC5;

	protected static final byte TYPE1_LENGTH = 6;
	protected static final byte FRAMING_MODE_LENGTH = 1;
	protected static final byte MAX_OUTSTANDING_LENGTH = 1;
	protected static final byte MAX_INFO_LENGTH_LENGTH = 2;
	protected static final byte DATA_PHASE_OPT_LENGTH = 1;
	protected static final byte PROTOCOL_LENGTH = 4;
	protected static final byte TYPEC5_LENGTH = 6;

	/** Connect using unknown protocol. */
	public static final byte PROTOCOL_NONE = 0x00;
	/** Connect using "Toolkit" protocol. */
	public static final byte PROTOCOL_NTK = 0x02;
	/** Connect using "Docking" protocol. */
	public static final byte PROTOCOL_NCU = 0x03;

	private byte type1 = 0x01;
	private byte framingMode = 0x02;
	private byte maxOutstanding = MNPLinkAcknowledgementPacket.CREDIT;
	private short maxInfoLength = MNPLinkTransferPacket.MAX_DATA_LENGTH;
	private byte dataPhaseOpt = 0x03;
	private byte protocol = PROTOCOL_NCU;

	/**
	 * Creates a new MNP LR packet.
	 */
	public MNPLinkRequestPacket() {
		super(LR, 0x1D);
	}

	@Override
	public int deserialize(byte[] payload) {
		int offset = super.deserialize(payload);

		// remove fixed fields
		offset++;

		byte type;
		int length;
		int maxInfoLength;

		while (offset < payload.length) {
			type = payload[offset++];
			length = payload[offset++] & 0xFF;

			switch (type) {
			case FRAMING_MODE:
				setFramingMode(payload[offset]);
				break;
			case MAX_OUTSTANDING:
				setMaxOutstanding(payload[offset]);
				break;
			case MAX_INFO_LENGTH:
				maxInfoLength = payload[offset] & 0xFF;
				if (length > 1)
					maxInfoLength = ((payload[offset + 1] & 0xFF) << 8) | maxInfoLength;
				setMaxInfoLength((short) maxInfoLength);
				break;
			case DATA_PHASE_OPT:
				setDataPhaseOpt(payload[offset]);
				break;
			case PROTOCOL:
				setProtocol(payload[offset]);
				break;
			}

			offset += length;
		}

		return offset;
	}

	@Override
	public byte[] serialize() {
		byte[] payload = new byte[] { 0x00, LR, 0x02, TYPE1, TYPE1_LENGTH, type1, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, FRAMING_MODE, FRAMING_MODE_LENGTH, framingMode,
				MAX_OUTSTANDING, MAX_OUTSTANDING_LENGTH, maxOutstanding, MAX_INFO_LENGTH, MAX_INFO_LENGTH_LENGTH, (byte) (maxInfoLength & 0xFF),
				(byte) ((maxInfoLength >> 8) & 0xFF), DATA_PHASE_OPT, DATA_PHASE_OPT_LENGTH, dataPhaseOpt
		/* , PROTOCOL, PROTOCOL_LENGTH, protocol, 0x04, 0x00, (byte) 0xFA */};
		payload[0] = (byte) (payload.length - 1);
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

	/**
	 * Get the protocol type.
	 * 
	 * @return the protocol.
	 */
	public byte getProtocol() {
		return protocol;
	}

	/**
	 * Set protocol type.
	 * 
	 * @param protocol
	 *            the protocol.
	 */
	public void setProtocol(byte protocol) {
		this.protocol = protocol;
	}

}
