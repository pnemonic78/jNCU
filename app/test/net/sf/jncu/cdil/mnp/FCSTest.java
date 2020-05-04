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

import org.junit.Test;

import net.sf.junit.SFTestCase;
import net.sf.util.zip.CRC16;

public class FCSTest extends SFTestCase {

	/**
	 * Test the FCS of Newton's "attempt to connect."
	 */
	@Test
	public void testConnect() {
		byte[] frame = { 0x16, 0x10, 0x02, 0x26, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03, 0x01, 0x08, 0x04, 0x02, 0x40, 0x00,
				0x08, 0x01, 0x03, 0x09, 0x01, 0x01, 0x0E, 0x04, 0x03, 0x04, 0x00, (byte) 0xFA, (byte) 0xC5, 0x06, 0x01, 0x04, 0x00, 0x00, (byte) 0xE1, 0x00, 0x10, 0x03,
				(byte) 0xB9, (byte) 0xBF };
		int fcsWord = 0xBFB9;
		int dataLength = 41;
		int fcsLength = 2;
		assertEquals(MNPPacketLayer.PACKET_HEAD.length + dataLength + MNPPacketLayer.PACKET_TAIL.length + fcsLength, frame.length);

		CRC16 fcs = new CRC16();
		fcs.update(frame, MNPPacketLayer.PACKET_HEAD.length, dataLength);
		fcs.update(MNPPacketLayer.PACKET_TAIL[1]);
		assertEquals(fcsWord, fcs.getValue());
	}
}
