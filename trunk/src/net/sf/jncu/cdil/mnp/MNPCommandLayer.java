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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeoutException;

import net.sf.jncu.cdil.CDCommandLayer;
import net.sf.jncu.protocol.IDockCommandToNewton;

/**
 * MNP command layer.
 * 
 * @author moshew
 */
public class MNPCommandLayer extends CDCommandLayer<MNPPacket> {

	/** Stream for packets to populate commands. */
	private final PipedOutputStream commandPackets = new PipedOutputStream();
	/** Stream of commands that have been populated from packets. */
	private InputStream in;
	/** Queue of outgoing commands. */
	protected final Map<Byte, IDockCommandToNewton> queueOut = new TreeMap<Byte, IDockCommandToNewton>();

	/**
	 * Creates a new command layer.
	 * 
	 * @param packetLayer
	 *            the packet layer.
	 */
	public MNPCommandLayer(MNPPacketLayer packetLayer) {
		super(packetLayer);
		setName("MNPCommandLayer-" + getId());
		try {
			this.in = new PipedInputStream(commandPackets);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	protected InputStream getInput() {
		return in;
	}

	@Override
	protected OutputStream getOutput() throws IOException {
		return null;
	}

	@Override
	public void close() {
		try {
			commandPackets.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		interrupt();
		super.close();
	}

	@Override
	public void write(IDockCommandToNewton cmd) throws IOException, TimeoutException {
		final byte[] payload = cmd.getPayload();
		if (payload == null)
			return;
		MNPLinkTransferPacket[] packets = MNPPacketFactory.getInstance().createTransferPackets(payload);
		Byte seq;
		for (MNPLinkTransferPacket packet : packets) {
			seq = packet.getSequence();
			queueOut.put(seq, cmd);
			((MNPPacketLayer) packetLayer).sendQueued(packet);
		}
	}

	@Override
	public void packetReceived(MNPPacket packet) {
		super.packetReceived(packet);

		switch (packet.getType()) {
		case MNPPacket.LA:
			packetReceivedLA((MNPLinkAcknowledgementPacket) packet);
			break;
		case MNPPacket.LD:
			packetReceivedLD((MNPLinkDisconnectPacket) packet);
			break;
		case MNPPacket.LR:
			packetReceivedLR((MNPLinkRequestPacket) packet);
			break;
		case MNPPacket.LT:
			packetReceivedLT((MNPLinkTransferPacket) packet);
			break;
		}
	}

	/**
	 * Received a link acknowledgement packet.
	 * 
	 * @param packet
	 *            the packet.
	 */
	protected void packetReceivedLA(MNPLinkAcknowledgementPacket packet) {
		Byte seq = packet.getSequence();
		IDockCommandToNewton cmd = queueOut.get(seq);
		if (cmd != null) {
			queueOut.remove(seq);
			fireCommandSent(cmd);
		}
	}

	/**
	 * Received a link disconnect packet.
	 * 
	 * @param packet
	 *            the packet.
	 */
	protected void packetReceivedLD(MNPLinkDisconnectPacket packet) {
	}

	/**
	 * Received a link request packet.
	 * 
	 * @param packet
	 *            the packet.
	 */
	protected void packetReceivedLR(MNPLinkRequestPacket packet) {
	}

	/**
	 * Received a link transfer packet.
	 * 
	 * @param packet
	 *            the packet.
	 */
	protected void packetReceivedLT(MNPLinkTransferPacket packet) {
		byte[] payload = packet.getData();

		try {
			commandPackets.write(payload);
			commandPackets.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
