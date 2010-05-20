package net.sf.jncu.protocol;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.nio.ByteBuffer;

import net.sf.jncu.cdil.mnp.MNPPacket;
import net.sf.jncu.cdil.mnp.MNPPacketLayer;
import net.sf.util.zip.CRC16;

/**
 * Docking frame.
 * <p>
 * The tail delimiter is included in the FCS calculation, except for the
 * <tt>1</tt><sup>st</sup> character.
 * 
 * @author moshew
 */
@Deprecated
public class DockingFrame {

	/** Frame escape character. */
	public static final byte DELIMITER_ESCAPE = MNPPacketLayer.DELIMITER_ESCAPE;
	/** Frame-starting delimiter. */
	public static final byte[] DELIMITER_PREAMBLE = MNPPacketLayer.PACKET_HEAD;
	/** Frame-ending delimiter. */
	public static final byte[] DELIMITER_TAIL = MNPPacketLayer.PACKET_TAIL;

	/** Receive error message. */
	private static final String ERROR_RECEIVE = "Error in reading from Newton device!";
	/** Not a command error message. */
	private static final String ERROR_NOT_COMMAND = "Expected a command";

	/** Maximum length of data. */
	private static final int MAX_DATA_LENGTH = 256;

	/** Index of the frame length. */
	public static int INDEX_LENGTH = 0;
	/** Index of the frame type. */
	public static int INDEX_TYPE = 1;
	/** Index of the frame sequence. */
	public static int INDEX_SEQUENCE = 2;
	/** Index of the start of a command. */
	public static int INDEX_COMMAND = 3;

	/** Desktop to Newton handshake response #1. */
	public static final byte[] PAYLOAD_DTN_HANDSHAKE_1 = { 0x1D, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03,
			0x01, 0x08, 0x04, 0x02, 0x40, 0x00, 0x08, 0x01, 0x03, 0x0E, 0x04, 0x03, 0x04, 0x00, (byte) 0xFA };

	/** Desktop to Newton handshake response #2. */
	public static final byte[] PAYLOAD_DTN_LA = { 0x03, /* Length of header */
	MNPPacket.LA, /* Type indication LA frame */
	0x00, /* Sequence number */
	0x01 };

	/**
	 * Constructs a new docking frame.
	 */
	public DockingFrame() {
		super();
	}

	/**
	 * Receive a frame. Ignores the header and tail.
	 * 
	 * @param in
	 *            the input.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @return the payload.
	 */
	public byte[] receive(InputStream in) throws IOException {
		int delimiterLength = DELIMITER_PREAMBLE.length;
		int state = 0;
		int b;

		/* Read header. */
		while (state < delimiterLength) {
			b = in.read();
			if (b < 0) {
				throw new EOFException(ERROR_RECEIVE);
			}
			if (b == DELIMITER_PREAMBLE[state]) {
				state++;
			} else {
				state = 0;
			}
		}

		/* Read up to tail. */
		boolean isEscape = false;
		CRC16 fcs = new CRC16();
		ByteArrayOutputStream buf = new ByteArrayOutputStream(MAX_DATA_LENGTH);
		delimiterLength = DELIMITER_TAIL.length;
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
				} else if (b == DELIMITER_TAIL[state]) {
					state++;
				}
				isEscape = !isEscape;
			} else if (b == DELIMITER_TAIL[state]) {
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
		if (payload.length < payload[INDEX_LENGTH]) {
			throw new ProtocolException("expected frame length: " + payload[INDEX_LENGTH] + " but was: " + payload.length);
		}
		fcs.update(DELIMITER_TAIL, 1, DELIMITER_TAIL.length - 1);

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
	 * Send a data frame.
	 * 
	 * @param out
	 *            the output.
	 * @param payload
	 *            the payload.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void send(OutputStream out, ByteBuffer payload) throws IOException {
		if (payload.hasArray()) {
			send(out, payload.array(), 0, payload.limit());
		} else {
			byte[] dst = new byte[payload.limit()];
			payload.rewind();
			payload.get(dst);
			send(out, dst);
		}
	}

	/**
	 * Send a data frame.
	 * 
	 * @param out
	 *            the output.
	 * @param payload
	 *            the payload.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void send(OutputStream out, byte[] payload) throws IOException {
		send(out, payload, 0, payload.length);
	}

	/**
	 * Send a data frame.
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
	public void send(OutputStream out, byte[] payload, int offset, int length) throws IOException {
		int b;
		CRC16 fcs = new CRC16();

		/* Write header. */
		out.write(DELIMITER_PREAMBLE);

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
		out.write(DELIMITER_TAIL);
		fcs.update(DELIMITER_TAIL, 1, DELIMITER_TAIL.length - 1);

		/* Write the FCS. */
		b = (int) (fcs.getValue() & 0xFFFF);
		out.write(b & 0xFF);
		out.write(b >> 8);
	}

	/**
	 * Wait for a frame type.
	 * 
	 * @param in
	 *            the input.
	 * @param type
	 *            the payload type.
	 * @return the payload.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public byte[] waitForType(InputStream in, byte type) throws IOException {
		byte[] data;
		do {
			data = receive(in);
		} while (data[DockingFrame.INDEX_TYPE] != type);
		return data;
	}

	/**
	 * Send a command to the Newton.
	 * 
	 * @param out
	 *            the output.
	 * @param cmd
	 *            the command.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void sendCommand(OutputStream out, DockCommandToNewton cmd) throws IOException {
		send(out, cmd.getPayload());
	}

	/**
	 * Receive a docking command.
	 * 
	 * @param in
	 *            the input.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws ProtocolException
	 *             if the expected frame is not a command.
	 * @return the command - <tt>null</tt> otherwise.
	 */
	public IDockCommandFromNewton receiveCommand(InputStream in) throws IOException, ProtocolException {
		byte[] frame = waitForType(in, MNPPacket.LT);
		if (!DockCommandFromNewton.isCommand(frame)) {
			throw new ProtocolException(ERROR_NOT_COMMAND);
		}
		IDockCommandFromNewton cmd = DockCommandFromNewton.deserialize(frame);
		if (cmd == null) {
			throw new ProtocolException(ERROR_NOT_COMMAND);
		}
		// TODO send PAYLOAD_DTN_LA;
		return cmd;
	}

}
