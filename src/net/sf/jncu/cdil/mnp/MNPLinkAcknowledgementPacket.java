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
 * MNP Link Acknowledgement packet.
 * 
 * @author moshew
 */
public class MNPLinkAcknowledgementPacket extends MNPPacket {

	/** Maximum outstanding credit. */
	public static final byte CREDIT = 8;

	private int sequence;
	private byte credit = CREDIT;
	private byte[] data;

	/**
	 * Creates a new MNP LA packet.
	 */
	public MNPLinkAcknowledgementPacket() {
		super(LA, 3);
	}

	@Override
	public int deserialize(byte[] payload) {
		int offset = super.deserialize(payload);

		if (getLength() != 3) {
			throw new ArrayIndexOutOfBoundsException();
		}
		setSequence(payload[offset++]);
		setCredit(payload[offset++]);
		byte[] data = new byte[payload.length - offset];
		System.arraycopy(payload, offset, data, 0, data.length);
		offset += data.length;
		setData(data);

		return offset;
	}

	@Override
	public byte[] serialize() {
		return new byte[] { 3, LA, (byte) sequence, credit };
	}

	/**
	 * Get the sequence number.
	 * 
	 * @return the sequence.
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * Set the sequence number.
	 * 
	 * @param sequence
	 *            the sequence.
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	/**
	 * Set the sequence number.
	 * 
	 * @param sequence
	 *            the sequence.
	 */
	public void setSequence(byte sequence) {
		setSequence(sequence & 0xFF);
	}

	/**
	 * Get the credit.
	 * 
	 * @return the credit.
	 */
	public byte getCredit() {
		return credit;
	}

	/**
	 * Set the credit.
	 * 
	 * @param credit
	 *            the credit.
	 */
	public void setCredit(byte credit) {
		this.credit = credit;
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

}
