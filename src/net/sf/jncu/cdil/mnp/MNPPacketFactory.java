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

	private static MNPPacketFactory instance;

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
			return new MNPLinkAcknowledgementPacket();
		case MNPPacket.LD:
			return new MNPLinkDisconnectPacket();
		case MNPPacket.LR:
			return new MNPLinkRequestPacket();
		case MNPPacket.LT:
			return new MNPLinkTransferPacket();
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
		MNPPacket packet = createLinkPacket(payload[1]);
		packet.deserialize(payload);
		return packet;
	}
}
