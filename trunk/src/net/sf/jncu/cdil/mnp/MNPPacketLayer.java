package net.sf.jncu.cdil.mnp;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;

import net.sf.lang.ControlCharacter;
import net.sf.util.zip.CRC16;

/**
 * MNP packet layer.
 * 
 * @author moshew
 */
public class MNPPacketLayer {

	/** Packet-starting delimiter. */
	public static final byte[] PACKET_HEAD = { ControlCharacter.SYN, ControlCharacter.DLE, ControlCharacter.STX };
	/** Packet-ending delimiter. */
	public static final byte[] PACKET_TAIL = { ControlCharacter.DLE, ControlCharacter.ETX };
	/** Packet escape character. */
	public static final byte DELIMITER_ESCAPE = ControlCharacter.DLE;

	/** Receive error message. */
	private static final String ERROR_RECEIVE = "Error in reading from Newton device!";
	/** Send error message. */
	private static final String ERROR_SEND = "Error in writing to Newton device!";
	/** Not a command error message. */
	private static final String ERROR_NOT_COMMAND = "Expected a command";

	/**
	 * Creates a new MNP packet layer.
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
	 * @return the payload.
	 */
	protected byte[] read(InputStream in) throws IOException {
		int delimiterLength = PACKET_HEAD.length;
		int state = 0;
		int b;

		/* Read header. */
		while (state < delimiterLength) {
			b = in.read();
			if (b < 0) {
				throw new EOFException(ERROR_RECEIVE);
			}
			if (b == PACKET_HEAD[state]) {
				state++;
			} else {
				state = 0;
			}
		}

		/* Read up to tail. */
		boolean isEscape = false;
		CRC16 fcs = new CRC16();
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		delimiterLength = PACKET_TAIL.length;
		state = 0;
		while (state < delimiterLength) {
			b = in.read();
			if (b < 0) {
				throw new EOFException(ERROR_RECEIVE);
			}
			if (b == DELIMITER_ESCAPE) {
				if (isEscape) {
					buf.write(b);
					fcs.update(b);
					state = 0;
				} else if (b == PACKET_TAIL[state]) {
					state++;
				}
				isEscape = !isEscape;
			} else if (b == PACKET_TAIL[state]) {
				state++;
			} else if (state == 0) {
				buf.write(b);
				fcs.update(b);
			} else {
				throw new ProtocolException(ERROR_RECEIVE);
			}
		}
		buf.close();
		byte[] payload = buf.toByteArray();
		fcs.update(PACKET_TAIL, 1, PACKET_TAIL.length - 1);

		/* Read the FCS. */
		b = in.read();
		if (b < 0) {
			throw new EOFException(ERROR_RECEIVE);
		}
		long fcsWord = b;
		b = in.read();
		if (b < 0) {
			throw new EOFException(ERROR_RECEIVE);
		}
		fcsWord = (b << 8) | fcsWord;
		if (fcsWord != fcs.getValue()) {
			throw new ProtocolException(ERROR_RECEIVE);
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
		CRC16 fcs = new CRC16();

		/* Write header. */
		out.write(PACKET_HEAD);

		/* Write up to tail. */
		for (int i = 0, o = offset; i < length; i++, o++) {
			b = payload[o];
			out.write(b);
			fcs.update(b);
			if (b == DELIMITER_ESCAPE) {
				out.write(b);
				fcs.update(b);
			}
		}
		out.write(PACKET_TAIL);
		fcs.update(PACKET_TAIL, 1, PACKET_TAIL.length - 1);

		/* Write the FCS. */
		b = (int) (fcs.getValue() & 0xFFFF);
		out.write(b & 0xFF);
		out.write(b >> 8);
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
		packet.deserialize(payload);
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
	}
}
