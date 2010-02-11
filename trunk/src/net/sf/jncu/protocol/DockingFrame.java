package net.sf.jncu.protocol;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.nio.ByteBuffer;

import net.sf.lang.ControlCharacter;

/**
 * Docking frame.
 * <p>
 * The tail delimiter is included in the FCS calculation, except for the
 * <tt>1</tt><sup>st</sup> character.
 * 
 * @author moshew
 */
public class DockingFrame {

	/** Frame escape character. */
	public static final byte DELIMITER_ESCAPE = ControlCharacter.DLE;
	/** Frame-starting delimiter. */
	public static final byte[] DELIMITER_PREAMBLE = { ControlCharacter.SYN, DELIMITER_ESCAPE, ControlCharacter.STX };
	/** Frame-ending delimiter. */
	public static final byte[] DELIMITER_TAIL = { DELIMITER_ESCAPE, ControlCharacter.ETX };

	/** Receive error message. */
	private static final String ERROR_RECEIVE = "Error in reading from Newton device!";
	/** Send error message. */
	private static final String ERROR_SEND = "Error in writing to Newton device!";
	/** Not a command error message. */
	private static final String ERROR_NOT_COMMAND = "Expected a command";

	/** Maximum length of header. */
	private static final int MAX_HEADER_LENGTH = 256;
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

	/** Frame type - LR. */
	public static final byte FRAME_TYPE_LR = 0x01;
	/** Frame type - LD. */
	public static final byte FRAME_TYPE_LD = 0x02;
	/** Frame type - . */
	public static final byte FRAME_TYPE_3 = 0x03;
	/** Frame type - LT. */
	public static final byte FRAME_TYPE_LT = 0x04;
	/** Frame type - LA. */
	public static final byte FRAME_TYPE_LA = 0x05;

	/** Desktop to Newton handshake response #1. */
	public static final byte[] FRAME_DTN_HANDSHAKE_1 = { 0x1D, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03, 0x01,
			0x08, 0x04, 0x02, 0x40, 0x00, 0x08, 0x01, 0x03, 0x0E, 0x04, 0x03, 0x04, 0x00, (byte) 0xFA };

	/** Desktop to Newton handshake response #2. */
	public static final byte[] FRAME_DTN_LA = { 0x03, /* Length of header */
	FRAME_TYPE_LA, /* Type indication LA frame */
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
	 * @return the frame data.
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
		FrameCheckSequence fcs = new FrameCheckSequence();
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
		byte[] frame = buf.toByteArray();
		if (frame.length < frame[INDEX_LENGTH]) {
			throw new ProtocolException("expected frame length: " + frame[INDEX_LENGTH] + " but was: " + frame.length);
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

		return frame;
	}

	/**
	 * Send a data frame.
	 * 
	 * @param out
	 *            the output.
	 * @param frame
	 *            the data frame.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void send(OutputStream out, ByteBuffer frame) throws IOException {
		if (frame.hasArray()) {
			send(out, frame.array(), 0, frame.limit());
		} else {
			byte[] dst = new byte[frame.limit()];
			frame.rewind();
			frame.get(dst);
			send(out, dst);
		}
	}

	/**
	 * Send a data frame.
	 * 
	 * @param out
	 *            the output.
	 * @param frame
	 *            the data frame.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void send(OutputStream out, byte[] frame) throws IOException {
		send(out, frame, 0, frame.length);
	}

	/**
	 * Send a data frame.
	 * 
	 * @param out
	 *            the output.
	 * @param frame
	 *            the data frame.
	 * @param offset
	 *            the frame offset.
	 * @param length
	 *            the frame length.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void send(OutputStream out, byte[] frame, int offset, int length) throws IOException {
		int b;
		FrameCheckSequence fcs = new FrameCheckSequence();

		/* Write header. */
		out.write(DELIMITER_PREAMBLE);

		/* Write up to tail. */
		for (int i = 0, o = offset; i < length; i++, o++) {
			b = frame[o];
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
	 *            the frame type.
	 * @return the frame data.
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
		send(out, cmd.getFrame());
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
	public DockCommandFromNewton receiveCommand(InputStream in) throws IOException, ProtocolException {
		byte[] frame = waitForType(in, FRAME_TYPE_LT);
		if (!DockCommandFromNewton.isCommand(frame)) {
			throw new ProtocolException(ERROR_NOT_COMMAND);
		}
		DockCommandFromNewton cmd = DockCommandFromNewton.deserialize(frame);
		if (cmd == null) {
			throw new ProtocolException(ERROR_NOT_COMMAND);
		}
		// TODO send FRAME_DTN_LA;
		return cmd;
	}

}
