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

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

import net.sf.jncu.cdil.CDPacketLayer;
import net.sf.jncu.cdil.CDPacketListener;
import net.sf.lang.ControlCharacter;
import net.sf.util.zip.CRC16;

/**
 * MNP packet layer.
 * 
 * @author moshew
 */
public class MNPPacketLayer extends CDPacketLayer<MNPPacket> implements CDPacketListener<MNPPacket> {

	/** Packet-starting delimiter. */
	protected static final byte[] PACKET_HEAD = { ControlCharacter.SYN, ControlCharacter.DLE, ControlCharacter.STX };
	/** Packet-ending delimiter. */
	protected static final byte[] PACKET_TAIL = { ControlCharacter.DLE, ControlCharacter.ETX };
	/** Packet escape character. */
	protected static final byte DELIMITER_ESCAPE = ControlCharacter.DLE;

	/** Send packets. */
	protected MNPPacketSender sender;

	/**
	 * Creates a new packet layer.
	 * 
	 * @param pipe
	 *            the pipe.
	 */
	public MNPPacketLayer(MNPPipe pipe) {
		super(pipe);
		setName("MNPPacketLayer-" + getId());
		this.sender = new MNPPacketSender(pipe, this);
		this.sender.start();
		addPacketListener(this);
	}

	@Override
	protected InputStream getInput() throws IOException {
		return null;
	}

	@Override
	protected OutputStream getOutput() throws IOException {
		return null;
	}

	@Override
	protected byte[] read() throws EOFException, IOException {
		return read(getInput());
	}

	/**
	 * Receive a MNP packet payload.
	 * 
	 * @param in
	 *            the input.
	 * @return the payload - {@code null} otherwise.
	 * @throws EOFException
	 *             if end of stream is reached.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected byte[] read(InputStream in) throws EOFException, IOException {
		int delimiterLength = PACKET_HEAD.length;
		int state = 0;
		int b;

		/* Read header. */
		while (state < delimiterLength) {
			b = readByte(in);
			if (b == PACKET_HEAD[state]) {
				state++;
			} else {
				state = 0;
			}
		}

		/* Read up to tail. */
		boolean isEscape = false;
		CRC16 crc = new CRC16();
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		delimiterLength = PACKET_TAIL.length;
		state = 0;
		while (state < delimiterLength) {
			b = readByte(in);
			if (b == DELIMITER_ESCAPE) {
				if (isEscape) {
					buf.write(b);
					crc.update(b);
					state = 0;
				} else if (b == PACKET_TAIL[state]) {
					state++;
				}
				isEscape = !isEscape;
			} else if (b == PACKET_TAIL[state]) {
				state++;
			} else if (state == 0) {
				buf.write(b);
				crc.update(b);
			} else {
				// throw new ProtocolException();
				return null;
			}
		}
		buf.close();
		byte[] payload = buf.toByteArray();
		crc.update(PACKET_TAIL, 1, PACKET_TAIL.length - 1);

		/* Read the FCS. */
		b = readByte(in);
		long crcWord = b;
		b = readByte(in);
		crcWord = (b << 8) | crcWord;
		if (crcWord != crc.getValue()) {
			// throw new ProtocolException("CRC error on input framing");
			return null;
		}

		return payload;
	}

	@Override
	protected void write(byte[] payload, int offset, int length) throws IOException {
		int b;
		CRC16 crc = new CRC16();
		OutputStream out = getOutput();
		if (out == null)
			return;

		/* Write header. */
		out.write(PACKET_HEAD);

		/* Write up to tail. */
		for (int i = 0, o = offset; i < length; i++, o++) {
			b = payload[o] & 0xFF;
			out.write(b);
			if (b == DELIMITER_ESCAPE) {
				out.write(b);
			}
			crc.update(b);
		}
		out.write(PACKET_TAIL);
		crc.update(PACKET_TAIL, 1, PACKET_TAIL.length - 1);

		/* Write the FCS. */
		b = (int) crc.getValue();
		out.write(b & 0xFF);
		out.write((b >> 8) & 0xFF);
	}

	/**
	 * Send a packet.
	 * 
	 * @param payload
	 *            the payload.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void write(InputStream payload) throws IOException {
		int b;
		CRC16 crc = new CRC16();
		OutputStream out = getOutput();
		if (out == null)
			return;

		/* Write header. */
		out.write(PACKET_HEAD);

		/* Write up to tail. */
		b = payload.read();
		while (b != -1) {
			out.write(b);
			if (b == DELIMITER_ESCAPE) {
				out.write(b);
			}
			crc.update(b);
			b = payload.read();
		}
		out.write(PACKET_TAIL);
		crc.update(PACKET_TAIL, 1, PACKET_TAIL.length - 1);

		/* Write the FCS. */
		b = (int) crc.getValue();
		out.write(b & 0xFF);
		out.write((b >> 8) & 0xFF);
	}

	@Override
	protected MNPPacket createPacket(byte[] payload) {
		return MNPPacketFactory.getInstance().createLinkPacket(payload);
	}

	@Override
	public void close() {
		super.close();
		sender.cancel();
		removePacketListener(this);
	}

	/**
	 * Send a packet and wait for acknowledgement.
	 * 
	 * @param packet
	 *            the packet to send.
	 * @throws TimeoutException
	 *             if a timeout occurs.
	 */
	public void sendQueued(MNPPacket packet) throws TimeoutException {
		sender.sendQueued(packet);
	}

	@Override
	public void packetReceived(MNPPacket packet) {
		if (packet.getType() == MNPPacket.LT) {
			if (allowAcknowledge()) {
				MNPLinkTransferPacket packetLT = (MNPLinkTransferPacket) packet;
				MNPLinkAcknowledgementPacket ack = (MNPLinkAcknowledgementPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LA);
				ack.setSequence(packetLT.getSequence());
				try {
					send(ack);
				} catch (Exception e) {
					e.printStackTrace();
					firePacketEOF();
				}
			}
		}
	}

	@Override
	public void packetSent(MNPPacket packet) {
		// Nothing to do.
	}

	@Override
	public void packetEOF() {
		// Nothing to do.
	}

	/**
	 * Can send a link acknowledgement packet after receiving a link transfer
	 * packet?
	 * 
	 * @return {@code true} if can send.
	 */
	protected boolean allowAcknowledge() {
		return true;
	}
}
