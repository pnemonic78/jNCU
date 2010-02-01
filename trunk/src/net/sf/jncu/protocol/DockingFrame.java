package net.sf.jncu.protocol;

import java.io.IOException;
import java.io.InputStream;

import net.sf.lang.ControlCharacter;

/**
 * Docking frame.
 * 
 * @author moshew
 */
public class DockingFrame {

	/** Frame preamble delimiter. */
	public static final byte[] FRAME_PREAMBLE = { ControlCharacter.SYN, ControlCharacter.DLE, ControlCharacter.STX };
	/** Frame-ending delimiter. */
	public static final byte[] FRAME_TAIL = { ControlCharacter.DLE, ControlCharacter.ETX };

	/** Receive error message. */
	private static final String ERROR_RECEIVE = "Error in reading from Newton device - connection stopped!";

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
		int frameLength = FRAME_PREAMBLE.length;
		int state = 0;
		int b;
		FrameCheckSequence fcs = new FrameCheckSequence();

		while (state < frameLength) {
			b = in.read();
			if (b < 0) {
				throw new IOException(ERROR_RECEIVE);
			}
			if (b == FRAME_PREAMBLE[state]) {
				state++;
			} else {
				state = 0;
			}
		}
	}
}
