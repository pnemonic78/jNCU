package net.sf.jncu.protocol;

import net.sf.junit.SFTestCase;

public class FCSTest extends SFTestCase {

	/**
	 * Test the FCS of Newton's "attempt to connect."
	 */
	public void testConnect() {
		byte[] frame = { 0x16, 0x10, 0x02, 0x26, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03, 0x01, 0x08, 0x04,
				0x02, 0x40, 0x00, 0x08, 0x01, 0x03, 0x09, 0x01, 0x01, 0x0E, 0x04, 0x03, 0x04, 0x00, (byte) 0xFA, (byte) 0xC5, 0x06, 0x01, 0x04, 0x00, 0x00,
				(byte) 0xE1, 0x00, 0x10, 0x03, (byte) 0xB9, (byte) 0xBF };
		int fcsWord = 0xBFB9;
		int dataLength = 41;
		int fcsLength = 2;
		assertEquals(DockingFrame.DELIMITER_PREAMBLE.length + dataLength + DockingFrame.DELIMITER_TAIL.length + fcsLength, frame.length);

		FrameCheckSequence fcs = new FrameCheckSequence();
		fcs.update(frame, DockingFrame.DELIMITER_PREAMBLE.length, dataLength);
		fcs.update(DockingFrame.DELIMITER_TAIL[1]);
		assertEquals(fcsWord, fcs.getValue());
	}
}
