package net.sf.jncu.protocol;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.nio.ByteBuffer;

import net.sf.lang.ControlCharacter;

/**
 * Docking frame.
 * 
 * @author moshew
 */
public class DockingFrame {

	/** Frame preamble delimiter. */
	public static final byte[] DELIMITER_PREAMBLE = { ControlCharacter.SYN, ControlCharacter.DLE, ControlCharacter.STX };
	/** Frame-ending delimiter. */
	public static final byte[] DELIMITER_TAIL = { ControlCharacter.DLE, ControlCharacter.ETX };

	/** Receive error message. */
	private static final String ERROR_RECEIVE = "Error in reading from Newton device - connection stopped!";

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
		FrameCheckSequence fcs = new FrameCheckSequence();
		ByteBuffer frame = ByteBuffer.allocate(MAX_DATA_LENGTH);

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
		delimiterLength = DELIMITER_TAIL.length;
		state = 0;
		while (state < delimiterLength) {
			b = in.read();
			if (b < 0) {
				throw new EOFException(ERROR_RECEIVE);
			}
			if (b == DELIMITER_TAIL[state]) {
				state++;
			} else {
				// Did we receive some data that just looks like the tail?
				if (state > 0) {
					frame.put(DELIMITER_TAIL, 0, state);
					fcs.update(DELIMITER_TAIL, 0, state);
					state = 0;
				}
				frame.put((byte) b);
				fcs.update(b);
			}
		}
		fcs.update(DELIMITER_TAIL, 1, delimiterLength - 1);

		/* Read the FCS. */
		b = in.read();
		if (b < 0) {
			throw new EOFException(ERROR_RECEIVE);
		}
		int fcsWord = b;
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
}
