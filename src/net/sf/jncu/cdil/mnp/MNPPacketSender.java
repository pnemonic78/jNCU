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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import net.sf.jncu.cdil.BadPipeStateException;

/**
 * Send queued MNP packets.
 * 
 * @author moshew
 */
public class MNPPacketSender extends Thread implements MNPPacketListener {

	protected final MNPPipe pipe;
	protected final MNPSerialPacketLayer packetLayer;
	protected final BlockingQueue<MNPPacket> queueSend = new LinkedBlockingQueue<MNPPacket>();
	protected boolean running = false;
	private int sequenceAcknowledged = -1;

	/**
	 * Creates a new packet sender.
	 * 
	 * @param pipe
	 *            the pipe.
	 * @param packetLayer
	 *            the packet layer.
	 */
	public MNPPacketSender(MNPPipe pipe, MNPSerialPacketLayer packetLayer) {
		super();
		setName("PacketSender-" + getId());
		this.pipe = pipe;
		this.packetLayer = packetLayer;
		this.packetLayer.addPacketListener(this);
	}

	/**
	 * Send a packet and wait for acknowledgement.<br>
	 * Do not wait for acknowledgement if the packet is itself an
	 * acknowledgement.
	 * 
	 * @param packet
	 *            the packet to send.
	 * @throws TimeoutException
	 *             if a timeout occurs.
	 */
	public void sendAndAcknowledge(MNPPacket packet) throws TimeoutException {
		try {
			queueSend.put(packet);
		} catch (InterruptedException ie) {
			throw new TimeoutException(ie.getMessage());
		}
	}

	/**
	 * Send packets while waiting for more to become available. Possibly re-send
	 * multiple times until acknowledged. Packets are queued until the first
	 * packet is sent.
	 * 
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	private void runSend() throws TimeoutException {
		MNPPacket packet;
		MNPPacket next = null;
		long timeout; // Enough time to wait for acknowledgement.
		long now;
		int retry;
		boolean resend = true;
		int sequenceToAcknowledge;

		while (running) {
			try {
				next = queueSend.take();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}

			if (next != null) {
				// LA and LD packets don't need acknowledgement.
				if (next.getType() == MNPPacket.LR) {
					sequenceToAcknowledge = 0;
				} else if (next.getType() == MNPPacket.LT) {
					sequenceToAcknowledge = ((MNPLinkTransferPacket) next).getSequence();
				} else {
					sequenceToAcknowledge = -1;
				}
				retry = 5;
				resend = true;
				packet = next;

				do {
					try {
						timeout = System.currentTimeMillis() + 2000L;
						packetLayer.send(packet);
						// TODO move this section to #packetSent
						if (sequenceToAcknowledge >= 0) {
							// Wait for acknowledgement.
							do {
								yield();
								resend &= (sequenceAcknowledged < sequenceToAcknowledge);
								now = System.currentTimeMillis();
							} while (resend && (now < timeout));
						} else {
							resend = false;
						}
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

					resend &= running;
					resend &= pipe.canSend();
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

	@Override
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

	@Override
	public void packetSent(MNPPacket packet) {
		// Nothing to do.
	}

	@Override
	public void packetEOF() {
		cancel();
	}

	/**
	 * Cancel all sending requests.
	 */
	public void cancel() {
		queueSend.clear();
		packetLayer.removePacketListener(this);
		running = false;
	}

	@Override
	public void run() {
		running = true;
		try {
			runSend();
		} catch (TimeoutException te) {
			te.printStackTrace();
			// TODO notify the pipe that error occurred.
		}
	}

}
