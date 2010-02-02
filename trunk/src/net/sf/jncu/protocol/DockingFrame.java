package net.sf.jncu.protocol;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
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
	 * Receive a frame. Ignores garbage data.
	 * 
	 * @param in
	 *            the input.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void receive(InputStream in) throws IOException {
		int delimiterLength = DELIMITER_PREAMBLE.length;
		int state = 0;
		int b;
		FrameCheckSequence fcs = new FrameCheckSequence();
		ByteBuffer frame = ByteBuffer.allocate(MAX_HEADER_LENGTH + MAX_DATA_LENGTH);

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
				frame.put((byte) b);
				fcs.update(b);
			}
		}
	}
}
