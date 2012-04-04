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

import java.io.BufferedInputStream;
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
	protected final Map<Byte, CommandPiece> queueOut = new TreeMap<Byte, CommandPiece>();

	/**
	 * Command chunk that is sent in a LT packet.
	 * 
	 * @author moshe
	 */
	private static class CommandPiece {

		public final IDockCommandToNewton command;
		@SuppressWarnings("unused")
		public final MNPLinkTransferPacket packet;
		public int length;
		public int progress;

		public CommandPiece(IDockCommandToNewton command, MNPLinkTransferPacket packet) throws IOException {
			this.command = command;
			this.packet = packet;
			this.length = command.getCommandPayloadLength();
			this.progress = packet.getData().length;
		}

	}

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
			this.in = new BufferedInputStream(new PipedInputStream(commandPackets), 1024);
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
		final InputStream payload = cmd.getCommandPayload();
		if (payload == null)
			return;
		int length = cmd.getCommandPayloadLength();
		int progress = 0;
		Byte seq;
		CommandPiece piece;
		try {
			Iterable<MNPLinkTransferPacket> packets = MNPPacketFactory.getInstance().createTransferPackets(payload, length);
			for (MNPLinkTransferPacket packet : packets) {
				seq = packet.getSequence();
				progress += packet.getData().length;
				piece = new CommandPiece(cmd, packet);
				piece.progress = progress;
				queueOut.put(seq, piece);
				((MNPPacketLayer) packetLayer).sendQueued(packet);
			}
		} finally {
			try {
				payload.close();
			} catch (Exception e) {
				// ignore
			}
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
		// Do not use queueOut.remove(seq) because we want to catch ignore
		// packets.
		CommandPiece piece = queueOut.get(seq);
		if (piece != null) {
			queueOut.put(seq, null);
			final IDockCommandToNewton cmd = piece.command;
			final int progress = piece.progress;
			final int total = piece.length;
			if (progress < total)
				fireCommandSending(cmd, progress, total);
			else {
				fireCommandSending(cmd, total, total);
				fireCommandSent(cmd);
			}
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
			synchronized (in) {
				in.notifyAll();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
