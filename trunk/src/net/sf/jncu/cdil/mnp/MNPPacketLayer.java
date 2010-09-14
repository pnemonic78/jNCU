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
import java.util.ArrayList;
import java.util.Collection;

import net.sf.lang.ControlCharacter;
import net.sf.util.zip.CRC16;

/**
 * MNP packet layer.
 * 
 * @author moshew
 */
public class MNPPacketLayer {

	/** Packet-starting delimiter. */
	protected static final byte[] PACKET_HEAD = { ControlCharacter.SYN, ControlCharacter.DLE, ControlCharacter.STX };
	/** Packet-ending delimiter. */
	protected static final byte[] PACKET_TAIL = { ControlCharacter.DLE, ControlCharacter.ETX };
	/** Packet escape character. */
	protected static final byte DELIMITER_ESCAPE = ControlCharacter.DLE;

	/** List of packet listeners. */
	protected final Collection<MNPPacketListener> listeners = new ArrayList<MNPPacketListener>();

	/**
	 * Creates a new packet layer.
	 */
	public MNPPacketLayer() {
		super();
	}

	/**
	 * Receive a packet payload.
	 * 
	 * @param in
	 *            the input.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @return the payload - <tt>null</tt> otherwise.
	 */
	protected byte[] read(InputStream in) throws IOException {
		int delimiterLength = PACKET_HEAD.length;
		int state = 0;
		int b;

		/* Read header. */
		while (state < delimiterLength) {
			b = in.read();
			if (b == -1) {
				throw new EOFException();
			}
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
			b = in.read();
			if (b == -1) {
				throw new EOFException();
			}
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
		b = in.read();
		if (b == -1) {
			throw new EOFException();
		}
		long crcWord = b;
		b = in.read();
		if (b == -1) {
			throw new EOFException();
		}
		crcWord = (b << 8) | crcWord;
		if (crcWord != crc.getValue()) {
			// throw new ProtocolException("CRC error on input framing");
			return null;
		}

		return payload;
	}

	/**
	 * Send a packet.
	 * 
	 * @param out
	 *            the output.
	 * @param payload
	 *            the payload.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void write(OutputStream out, byte[] payload) throws IOException {
		write(out, payload, 0, payload.length);
	}

	/**
	 * Send a packet.
	 * 
	 * @param out
	 *            the output.
	 * @param payload
	 *            the payload.
	 * @param offset
	 *            the frame offset.
	 * @param length
	 *            the frame length.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void write(OutputStream out, byte[] payload, int offset, int length) throws IOException {
		int b;
		CRC16 crc = new CRC16();

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
	 * Receive a packet.
	 * 
	 * @param in
	 *            the input.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @return the packet.
	 */
	public MNPPacket receive(InputStream in) throws IOException {
		byte[] payload = read(in);
		MNPPacket packet = MNPPacketFactory.getInstance().createLinkPacket(payload);
		if (packet != null) {
			firePacketReceived(packet);
		}
		return packet;
	}

	/**
	 * Send a packet.
	 * 
	 * @param out
	 *            the output.
	 * @param packet
	 *            the packet.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void send(OutputStream out, MNPPacket packet) throws IOException {
		byte[] payload = packet.serialize();
		write(out, payload);
		firePacketSent(packet);
	}

	/**
	 * Add a packet listener.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addPacketListener(MNPPacketListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Remove a packet listener.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removePacketListener(MNPPacketListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notify all the listeners that a packet has been received.
	 * 
	 * @param packet
	 *            the received packet.
	 */
	protected void firePacketReceived(MNPPacket packet) {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<MNPPacketListener> listenersCopy = new ArrayList<MNPPacketListener>(listeners);
		for (MNPPacketListener listener : listenersCopy) {
			listener.packetReceived(packet);
		}
	}

	/**
	 * Notify all the listeners that a packet has been sent.
	 * 
	 * @param packet
	 *            the sent packet.
	 */
	protected void firePacketSent(MNPPacket packet) {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<MNPPacketListener> listenersCopy = new ArrayList<MNPPacketListener>(listeners);
		for (MNPPacketListener listener : listenersCopy) {
			listener.packetSent(packet);
		}
	}

	/**
	 * Listen for incoming packets until no more packets are available.
	 * 
	 * @param in
	 *            the input.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void listen(InputStream in) throws IOException {
		MNPPacket packet;
		do {
			packet = receive(in);
		} while (packet != null);
	}

	/**
	 * Close the layer and release resources.
	 */
	public void close() {
		listeners.clear();
	}
}
