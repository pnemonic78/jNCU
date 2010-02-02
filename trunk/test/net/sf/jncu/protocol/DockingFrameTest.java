package net.sf.jncu.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

public class DockingFrameTest extends TestCase {

	/**
	 * Test the FCS of Newton's "attempt to connect."
	 * 
	 * @throws IOException
	 */
	public void testConnect() throws IOException {
		byte[] frame = { 0x16, 0x10, 0x02, 0x26, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03, 0x01, 0x08, 0x04,
				0x02, 0x40, 0x00, 0x08, 0x01, 0x03, 0x09, 0x01, 0x01, 0x0E, 0x04, 0x03, 0x04, 0x00, (byte) 0xFA, (byte) 0xC5, 0x06, 0x01, 0x04, 0x00, 0x00,
				(byte) 0xE1, 0x00, 0x10, 0x03, (byte) 0xB9, (byte) 0xBF };
		int dataLength = 41;
		int fcsLength = 2;
		assertEquals(DockingFrame.DELIMITER_PREAMBLE.length + dataLength + DockingFrame.DELIMITER_TAIL.length + fcsLength, frame.length);

		InputStream in = new ByteArrayInputStream(frame);
		DockingFrame df = new DockingFrame();
		df.receive(in);
	}
}
