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
 * MNP Link Disconnect packet.
 * 
 * @author moshew
 */
public class MNPLinkDisconnectPacket extends MNPPacket {

	private static final int REASON_CODE = 0x01;
	private static final int USER_CODE = 0x02;

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
			return new byte[] { 0x05, LD, REASON_CODE, 0x01, reasonCode };
		}
		return new byte[] { 0x07, LD, REASON_CODE, 0x01, reasonCode, USER_CODE, 0x01, userCode };
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
