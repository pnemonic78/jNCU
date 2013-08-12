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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.junit.Test;

import net.sf.jncu.protocol.DockCommand;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.IDockCommand;
import net.sf.jncu.protocol.v2_0.DockCommandFactory;
import net.sf.jncu.protocol.v2_0.session.DDesktopInfo;
import net.sf.jncu.protocol.v2_0.session.DNewtonName;
import net.sf.junit.SFTestCase;

public class MNPPacketLayerTest extends SFTestCase {

	@Test
	public void testMultiCmd() {
		byte[] data = { 'n', 'e', 'w', 't', 'd', 'o', 'c', 'k', 'u', 'n', 'k', 'n', 0x00, 0x00, 0x00, 0x04, 'k', 'b', 'd', 'c', 'n', 'e', 'w', 't', 'd', 'o', 'c', 'k', 'd', 'r',
				'e', 's', 0x00, 0x00, 0x00, 0x04, (byte) 0xff, (byte) 0xff, (byte) 0xb1, (byte) 0xdf };
		assertNotNull(data);
		List<IDockCommand> cmds = DockCommandFactory.getInstance().deserialize(data);
		assertNotNull(cmds);
		assertEquals(2, cmds.size());
	}

	/**
	 * Test the FCS of Newton's "attempt to connect."
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReceive() throws Exception {
		byte[] frame = { 0x16, 0x10, 0x02, 0x26, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03, 0x01, 0x08, 0x04, 0x02, 0x40, 0x00,
				0x08, 0x01, 0x03, 0x09, 0x01, 0x01, 0x0E, 0x04, 0x03, 0x04, 0x00, (byte) 0xFA, (byte) 0xC5, 0x06, 0x01, 0x04, 0x00, 0x00, (byte) 0xE1, 0x00, 0x10, 0x03,
				(byte) 0xB9, (byte) 0xBF };
		int dataLength = 41;
		int fcsLength = 2;
		assertEquals(MNPPacketLayer.PACKET_HEAD.length + dataLength + MNPPacketLayer.PACKET_TAIL.length + fcsLength, frame.length);

		final InputStream in = new ByteArrayInputStream(frame);
		MNPPacketLayer packetLayer = new MNPPacketLayer(null) {
			protected InputStream getInput() throws IOException {
				return in;
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		};
		MNPPacket packet = packetLayer.receive();
		assertNotNull(packet);
		assertEquals(MNPPacket.LR, packet.getType());
		assertTrue(packet instanceof MNPLinkRequestPacket);
		assertEquals(frame[3], packet.getLength());
	}

	/**
	 * Test the FCS of Newton's "attempt to connect."
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSend() throws Exception {
		byte[] frame = { 0x16, 0x10, 0x02, 0x26, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03, 0x01, 0x08, 0x04, 0x02, 0x40, 0x00,
				0x08, 0x01, 0x03, 0x09, 0x01, 0x01, 0x0E, 0x04, 0x03, 0x04, 0x00, (byte) 0xFA, (byte) 0xC5, 0x06, 0x01, 0x04, 0x00, 0x00, (byte) 0xE1, 0x00, 0x10, 0x03,
				(byte) 0xB9, (byte) 0xBF };
		int dataLength = 41;
		int fcsLength = 2;
		assertEquals(MNPPacketLayer.PACKET_HEAD.length + dataLength + MNPPacketLayer.PACKET_TAIL.length + fcsLength, frame.length);

		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		MNPPacketLayer packetLayer = new MNPPacketLayer(null) {
			@Override
			protected OutputStream getOutput() throws IOException {
				return out;
			}
		};
		MNPPacket packet = MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LR);
		assertNotNull(packet);
		assertEquals(MNPPacket.LR, packet.getType());
		assertTrue(packet instanceof MNPLinkRequestPacket);

		packetLayer.send(packet);
		out.close();
		byte[] buf = out.toByteArray();
		assertNotNull(buf);
		assertEquals(0x1D, packet.getLength());
	}

	/**
	 * Test receiving a command "name".
	 */
	@Test
	public void testReceiveName() throws Exception {
		byte[] data = { 110, 101, 119, 116, 100, 111, 99, 107, 110, 97, 109, 101, 0, 0, 0, 122, 0, 0, 0, 72, 18, 126, 21, 116, 1, 0, 0, 0, 16, 0, 48, 0, 0, 2, 0, 2, 0, 0, -128, 0,
				0, 14, 32, 0, 0, 0, 1, -32, 0, 0, 1, 64, 0, 0, 0, 1, 0, 0, 0, 1, 1, -66, 82, -52, 0, 0, 0, 100, 0, 0, 0, 100, 0, 0, 0, 4, 0, 0, 0, 3, 0, 0, 0, 0, 1, -66, 82, -52,
				0, 0, 0, 11, 0, 77, 0, 111, 0, 115, 0, 104, 0, 101, 0, 32, 0, 77, 0, 105, 0, 99, 0, 104, 0, 97, 0, 101, 0, 108, 0, 32, 0, 87, 0, 97, 0, 105, 0, 115, 0, 98, 0, 101,
				0, 114, 0, 103, 0, 0, 0, 0 };

		assertTrue(DockCommand.isCommand(data));
		List<IDockCommand> cmds = DockCommandFactory.getInstance().deserialize(data);
		assertNotNull(cmds);
		assertEquals(1, cmds.size());
		IDockCommand cmd = cmds.get(0);
		assertEquals(DNewtonName.COMMAND, cmd.getCommand());
	}

	/**
	 * Test sending a command "desktop info".
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDesktopInfo() throws Exception {
		DockCommandToNewton cmd = new DDesktopInfo();
		assertNotNull(cmd);
		assertEquals(DDesktopInfo.COMMAND, cmd.getCommand());
		InputStream payload = cmd.getCommandPayload();
		assertNotNull(payload);
		int count = 0;
		int i = payload.read();
		while (i != -1) {
			count++;
			i = payload.read();
		}
		assertEquals(128, count);
		payload.close();
	}
}
