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
 * MNP packet factory.
 * 
 * @author moshew
 */
public class MNPPacketFactory {

	/**
	 * Maximum date length per packet before having to split into multiple
	 * packets.
	 */
	protected static final short MAX_DATA_LENGTH = 256;

	private static MNPPacketFactory instance;

	/** Outgoing sequence. */
	private byte sequence;

	/**
	 * Creates a new factory.
	 */
	protected MNPPacketFactory() {
		super();
	}

	/**
	 * Get the factory instance.
	 * 
	 * @return the factory.
	 */
	public static MNPPacketFactory getInstance() {
		if (instance == null) {
			instance = new MNPPacketFactory();
		}
		return instance;
	}

	/**
	 * Create a MNP link packet.
	 * 
	 * @param type
	 *            the link type.
	 * @return the packet.
	 * @see MNPPacket#LA
	 * @see MNPPacket#LD
	 * @see MNPPacket#LR
	 * @see MNPPacket#LT
	 */
	public MNPPacket createLinkPacket(byte type) {
		switch (type) {
		case MNPPacket.LA:
			return createLA();
		case MNPPacket.LD:
			return createLD();
		case MNPPacket.LR:
			return createLR();
		case MNPPacket.LT:
			return createLTSend();
		}
		throw new IllegalArgumentException("invalid type " + type);
	}

	/**
	 * Create a MNP link packet, and decode.
	 * 
	 * @param payload
	 *            the payload.
	 * @return the packet.
	 */
	public MNPPacket createLinkPacket(byte[] payload) {
		if ((payload == null) || (payload.length < 2)) {
			return null;
		}
		byte type = payload[1];
		MNPPacket packet = null;
		if (type == MNPPacket.LT) {
			packet = createLT();
		} else {
			packet = createLinkPacket(type);
		}
		packet.deserialize(payload);
		return packet;
	}

	/**
	 * Create MNP link transfer packets.
	 * 
	 * @param data
	 *            the payload data.
	 * @return the array of packets.
	 */
	public MNPLinkTransferPacket[] createTransferPackets(byte[] data) {
		if (data == null)
			return null;
		return createTransferPackets(data, 0, data.length);
	}

	/**
	 * Create MNP link transfer packets.
	 * 
	 * @param data
	 *            the payload data.
	 * @param offset
	 *            the payload offset.
	 * @param length
	 *            the payload length.
	 * @return the array of packets.
	 */
	public MNPLinkTransferPacket[] createTransferPackets(byte[] data, int offset, int length) {
		if (data == null)
			return null;
		int numPackets = length / MAX_DATA_LENGTH;
		if ((length % MAX_DATA_LENGTH) > 0)
			numPackets++;
		MNPLinkTransferPacket[] packets = new MNPLinkTransferPacket[numPackets];
		MNPLinkTransferPacket packet = null;
		int i = 0;
		while (length > MAX_DATA_LENGTH) {
			packet = createLTSend();
			packets[i++] = packet;
			packet.setData(data, offset, MAX_DATA_LENGTH);
			offset += MAX_DATA_LENGTH;
			length -= MAX_DATA_LENGTH;
		}
		if (length > 0) {
			packet = createLTSend();
			packets[i++] = packet;
			packet.setData(data, offset, length);
		}
		return packets;
	}

	/**
	 * Reset the sequence.
	 */
	public void resetSequence() {
		this.sequence = 0;
	}

	/**
	 * Create a Link Acknowledgement packet.
	 * 
	 * @return the packet.
	 */
	protected MNPLinkAcknowledgementPacket createLA() {
		return new MNPLinkAcknowledgementPacket();
	}

	/**
	 * Create a Link Disconnect packet.
	 * 
	 * @return the packet.
	 */
	protected MNPLinkDisconnectPacket createLD() {
		return new MNPLinkDisconnectPacket();
	}

	/**
	 * Create a Link Request packet.
	 * 
	 * @return the packet.
	 */
	protected MNPLinkRequestPacket createLR() {
		MNPLinkRequestPacket packetLR = new MNPLinkRequestPacket();
		packetLR.setMaxInfoLength(MAX_DATA_LENGTH);
		return packetLR;
	}

	/**
	 * Create a Link Transfer packet.
	 * 
	 * @return the packet.
	 */
	protected MNPLinkTransferPacket createLT() {
		return new MNPLinkTransferPacket();
	}

	/**
	 * Create a Link Transfer packet for sending.
	 * 
	 * @return the packet.
	 */
	protected MNPLinkTransferPacket createLTSend() {
		MNPLinkTransferPacket packet = createLT();
		packet.setSequence(++sequence);
		return packet;
	}
}
