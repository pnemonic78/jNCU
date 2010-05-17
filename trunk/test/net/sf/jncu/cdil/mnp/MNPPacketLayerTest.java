package net.sf.jncu.cdil.mnp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v2_0.DockCommandFactory;
import net.sf.jncu.protocol.v2_0.session.DDesktopInfo;
import net.sf.jncu.protocol.v2_0.session.DNewtonName;
import net.sf.junit.SFTestCase;

public class MNPPacketLayerTest extends SFTestCase {

	/**
	 * Test the FCS of Newton's "attempt to connect."
	 * 
	 * @throws IOException
	 */
	public void testReceive() throws IOException {
		byte[] frame = { 0x16, 0x10, 0x02, 0x26, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03, 0x01, 0x08, 0x04,
				0x02, 0x40, 0x00, 0x08, 0x01, 0x03, 0x09, 0x01, 0x01, 0x0E, 0x04, 0x03, 0x04, 0x00, (byte) 0xFA, (byte) 0xC5, 0x06, 0x01, 0x04, 0x00, 0x00,
				(byte) 0xE1, 0x00, 0x10, 0x03, (byte) 0xB9, (byte) 0xBF };
		int dataLength = 41;
		int fcsLength = 2;
		assertEquals(MNPPacketLayer.PACKET_HEAD.length + dataLength + MNPPacketLayer.PACKET_TAIL.length + fcsLength, frame.length);

		InputStream in = new ByteArrayInputStream(frame);
		MNPPacketLayer layer = new MNPPacketLayer();
		MNPPacket packet = layer.receive(in);
		assertNotNull(packet);
		assertEquals(MNPPacket.LR, packet.getType());
		assertTrue(packet instanceof MNPLinkRequestPacket);
		assertEquals(frame[3], packet.getHeaderLength());
	}

	/**
	 * Test the FCS of Newton's "attempt to connect."
	 * 
	 * @throws IOException
	 */
	public void testSend() throws IOException {
		byte[] frame = { 0x16, 0x10, 0x02, 0x26, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03, 0x01, 0x08, 0x04,
				0x02, 0x40, 0x00, 0x08, 0x01, 0x03, 0x09, 0x01, 0x01, 0x0E, 0x04, 0x03, 0x04, 0x00, (byte) 0xFA, (byte) 0xC5, 0x06, 0x01, 0x04, 0x00, 0x00,
				(byte) 0xE1, 0x00, 0x10, 0x03, (byte) 0xB9, (byte) 0xBF };
		int dataLength = 41;
		int fcsLength = 2;
		assertEquals(MNPPacketLayer.PACKET_HEAD.length + dataLength + MNPPacketLayer.PACKET_TAIL.length + fcsLength, frame.length);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		MNPPacketLayer layer = new MNPPacketLayer();
		MNPPacket packet = MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LR);
		assertNotNull(packet);
		assertEquals(MNPPacket.LR, packet.getType());
		assertTrue(packet instanceof MNPLinkRequestPacket);

		layer.send(out, packet);
		out.close();
		byte[] buf = out.toByteArray();
		assertNotNull(buf);
		assertEquals(buf[3], packet.getHeaderLength());
	}

	/**
	 * Test receiving a command "name".
	 */
	public void testReceiveName() {
		byte[] data = { 110, 101, 119, 116, 100, 111, 99, 107, 110, 97, 109, 101, 0, 0, 0, 122, 0, 0, 0, 72, 18, 126, 21, 116, 1, 0, 0, 0, 16, 0, 48, 0, 0, 2,
				0, 2, 0, 0, -128, 0, 0, 14, 32, 0, 0, 0, 1, -32, 0, 0, 1, 64, 0, 0, 0, 1, 0, 0, 0, 1, 1, -66, 82, -52, 0, 0, 0, 100, 0, 0, 0, 100, 0, 0, 0, 4,
				0, 0, 0, 3, 0, 0, 0, 0, 1, -66, 82, -52, 0, 0, 0, 11, 0, 77, 0, 111, 0, 115, 0, 104, 0, 101, 0, 32, 0, 77, 0, 105, 0, 99, 0, 104, 0, 97, 0,
				101, 0, 108, 0, 32, 0, 87, 0, 97, 0, 105, 0, 115, 0, 98, 0, 101, 0, 114, 0, 103, 0, 0, 0, 0 };

		DockCommandFromNewton cmd = DockCommandFromNewton.deserialize(data);
		assertNotNull(cmd);
		assertTrue(DockCommandFromNewton.isCommand(data));
		assertEquals(DNewtonName.COMMAND, cmd.getCommand());
	}

	/**
	 * Test sending a command "desktop info".
	 */
	public void testDesktopInfo() {
		DockCommandToNewton cmd = (DockCommandToNewton) DockCommandFactory.getInstance().create(DDesktopInfo.COMMAND);
		assertNotNull(cmd);
		assertEquals(DDesktopInfo.COMMAND, cmd.getCommand());
		byte[] payload = cmd.getPayload();
		assertNotNull(payload);
	}
}
