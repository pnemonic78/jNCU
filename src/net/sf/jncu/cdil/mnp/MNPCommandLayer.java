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
import java.util.HashMap;
import java.util.Map;
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
	private OutputStream packetsToCommands;
	/** Stream of commands that have been populated from packets. */
	private InputStream in;
	/** Queue of outgoing commands. */
	protected final Map<Integer, CommandPiece> queueOut = new HashMap<Integer, CommandPiece>();
	/** Current LT sequence id. */
	private int sequenceLT = 0;

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
		PipedOutputStream pipeSource = new PipedOutputStream();
		try {
			this.packetsToCommands = pipeSource;
			this.in = new BufferedInputStream(new PipedInputStream(pipeSource), 1024);
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
			packetsToCommands.close();
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
		Integer seq;
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
		Integer seq = packet.getSequence();
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
		int seq = packet.getSequence();

		// Keep packets in order.
		if ((sequenceLT + 1) == seq) {
			sequenceLT = seq;
			// Byte: 0xFF + 1 == 0x00
			if (sequenceLT == 0xFF)
				sequenceLT = -1;

			try {
				packetsToCommands.write(payload);
				packetsToCommands.flush();
				synchronized (in) {
					in.notifyAll();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
				if ("Read end dead".equals(ioe.getMessage()))
					fireCommandEOF();
			}
		}
	}
}
