package net.sf.jncu.protocol;

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

	/** Maximum length of header. */
	private static final int MAX_HEADER_LENGTH = 256;
	/** Maximum length of data. */
	private static final int MAX_DATA_LENGTH = 256;

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
	public ByteBuffer receive(InputStream in) throws IOException {
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
		ByteBuffer frame = ByteBuffer.allocate(MAX_DATA_LENGTH);
		delimiterLength = DELIMITER_TAIL.length;
		state = 0;
		while (state < delimiterLength) {
			b = in.read();
			if (b < 0) {
				throw new EOFException(ERROR_RECEIVE);
			}
			if (b == DELIMITER_ESCAPE) {
				if (isEscape) {
					frame.put((byte) b);
					fcs.update(b);
					state = 0;
				} else if (b == DELIMITER_TAIL[state]) {
					state++;
				}
				isEscape = !isEscape;
			} else if (b == DELIMITER_TAIL[state]) {
				state++;
			} else if (state == 0) {
				frame.put((byte) b);
				fcs.update(b);
			} else {
				throw new ProtocolException(ERROR_RECEIVE);
			}
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

}
