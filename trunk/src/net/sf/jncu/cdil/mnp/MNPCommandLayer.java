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
	private final PipedOutputStream packets = new PipedOutputStream();
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
		try {
			this.in = new PipedInputStream(packets);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.CDCommandLayer#getInput()
	 */
	@Override
	protected InputStream getInput() {
		return in;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.CDCommandLayer#getOutput()
	 */
	@Override
	protected OutputStream getOutput() throws IOException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.CDCommandLayer#close()
	 */
	@Override
	public void close() {
		try {
			packets.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		super.close();
	}

	/*
	 * (non-Javadoc)
	 * @seenet.sf.jncu.cdil.CDCommandLayer#write(net.sf.jncu.protocol.
	 * IDockCommandToNewton)
	 */
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

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.cdil.mnp.MNPPacketListener#packetReceived(net.sf.jncu.cdil
	 * .mnp.MNPPacket)
	 */
	@Override
	public void packetReceived(MNPPacket packet) {
		byte type = packet.getType();
		if (type == MNPPacket.LA) {
			packetReceivedLA((MNPLinkAcknowledgementPacket) packet);
		} else if (type == MNPPacket.LD) {
			packetReceivedLD((MNPLinkDisconnectPacket) packet);
		} else if (type == MNPPacket.LT) {
			packetReceivedLT((MNPLinkTransferPacket) packet);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.cdil.mnp.MNPPacketListener#packetSent(net.sf.jncu.cdil.mnp
	 * .MNPPacket)
	 */
	@Override
	public void packetSent(MNPPacket packet) {
		// Nothing to do.
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.mnp.MNPPacketListener#packetEOF()
	 */
	@Override
	public void packetEOF() {
		// Nothing to do.
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
	 * Received a link transfer packet.
	 * 
	 * @param packet
	 *            the packet.
	 */
	protected void packetReceivedLT(MNPLinkTransferPacket packet) {
		byte[] payload = packet.getData();

		try {
			packets.write(payload);
			packets.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
