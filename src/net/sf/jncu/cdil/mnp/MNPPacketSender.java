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
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.cdil.mnp.MNPPipe.MNPState;

/**
 * Send queued MNP packets.
 * 
 * @author moshew
 */
public class MNPPacketSender extends Thread implements MNPPacketListener {

	protected final MNPPipe pipe;
	protected final MNPPacketLayer packetLayer;
	protected final OutputStream out;
	protected final BlockingQueue<MNPPacket> queue = new LinkedBlockingQueue<MNPPacket>();
	protected boolean running = false;
	private int sequenceAcknowledged = -1;

	public MNPPacketSender(MNPPipe pipe, MNPPacketLayer packetLayer, OutputStream out) {
		super();
		this.pipe = pipe;
		this.packetLayer = packetLayer;
		this.packetLayer.addPacketListener(this);
		this.out = out;
	}

	/**
	 * Send a packet and wait for acknowledgement.<br>
	 * Do not wait for acknowledgement if the packet is itself and
	 * acknowledgement.
	 * 
	 * @param packet
	 *            the packet to send.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void send(MNPPacket packet) throws TimeoutException {
		try {
			queue.put(packet);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	/**
	 * Send the packet and wait for more packets. Possibly re-send multiple
	 * times until acknowledged. Packets are queued until the first packet is
	 * sent.
	 * 
	 * @param packet
	 *            the LT packet.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	protected final void sendAndWait() throws TimeoutException {
		MNPPacket packet;
		MNPPacket next = null;
		long timeout; // Enough time to wait for acknowledgement.
		long now;
		int retry = 3;
		boolean resend = true;
		int seqToAck = 0;
		CDState stateCD;

		while (running) {
			try {
				next = queue.take();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}

			if (next != null) {
				// LA and LD packets don't need acknowledgement.
				if (next.getType() == MNPPacket.LR) {
					seqToAck = 0;
				} else if (next.getType() == MNPPacket.LT) {
					seqToAck = ((MNPLinkTransferPacket) next).getSequence();
				} else {
					seqToAck = -1;
				}
				retry = 5;
				resend = true;
				packet = next;

				do {
					try {
						timeout = System.currentTimeMillis() + 2000L;
						packetLayer.send(out, packet);
						if (seqToAck >= 0) {
							// Wait for acknowledgement.
							do {
								yield();
								resend &= (sequenceAcknowledged < seqToAck);
								now = System.currentTimeMillis();
							} while (resend && (now < timeout));
						} else {
							resend = false;
						}
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

					stateCD = pipe.getLayer().getState();
					resend &= running;
					resend &= (pipe.getMNPState() != MNPState.MNP_DISCONNECTED);
					resend &= (stateCD == CDState.CONNECT_PENDING) || (stateCD == CDState.CONNECTED) || (stateCD == CDState.LISTENING);
					if (resend) {
						retry--;
						if (retry < 0) {
							throw new TimeoutException();
						}
					}
				} while (resend);
			}
		}
	}

	public void packetReceived(MNPPacket packet) {
		byte packetType = packet.getType();

		try {
			if (packetType == MNPPacket.LA) {
				MNPLinkAcknowledgementPacket la = (MNPLinkAcknowledgementPacket) packet;
				sequenceAcknowledged = Math.max(sequenceAcknowledged, la.getSequence());
			}
		} catch (BadPipeStateException bpse) {
			bpse.printStackTrace();
		}
	}

	/**
	 * Cancel all sending requests.
	 */
	public void cancel() {
		queue.clear();
		packetLayer.removePacketListener(this);
		running = false;
	}

	@Override
	public void run() {
		running = true;
		try {
			sendAndWait();
		} catch (TimeoutException te) {
			te.printStackTrace();
			// TODO notify the pipe that error occurred.
		}
	}

}
