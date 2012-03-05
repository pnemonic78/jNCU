package net.sf.jncu.protocol.app;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;

import net.sf.jncu.cdil.mnp.MNPLinkTransferPacket;
import net.sf.jncu.cdil.mnp.MNPPacketFactory;
import net.sf.jncu.protocol.v1_0.app.DLoadPackage;
import net.sf.junit.SFTestCase;

/**
 * Package commands tester.
 * 
 * @author moshe
 */
public class DPackageTester extends SFTestCase {

	public DPackageTester() {
		super("Package Tester");
	}

	public void testDLoadPackage() throws Exception {
		DLoadPackage cmd = new DLoadPackage();
		assertNotNull(cmd);

		cmd.setFile(null);
		assertEquals(0, cmd.getLength());
		InputStream payload = cmd.getPayload();
		assertNotNull(payload);

		MNPPacketFactory factory = MNPPacketFactory.getInstance();
		assertNotNull(factory);
		Iterable<MNPLinkTransferPacket> packets = factory.iterateTransferPackets(payload);
		assertNotNull(packets);
		Iterator<MNPLinkTransferPacket> iter = packets.iterator();
		assertNotNull(iter);

		File f = new File("./_stuff/unixnpi-1.1.4/template/StatusMonitor.pkg");
		assertNotNull(f);
		assertTrue(f.exists());
		cmd.setFile(f);
		int length = (int) f.length();
		assertFalse(0 == length);
		assertEquals(length, cmd.getLength());
		payload = cmd.getPayload();
		assertNotNull(payload);
		packets = factory.iterateTransferPackets(payload);
		assertNotNull(packets);
		iter = packets.iterator();
		assertNotNull(iter);
		assertTrue(iter.hasNext());
		int count = 0;
		int numPackets = length / MNPPacketFactory.MAX_DATA_LENGTH;
		if ((length % MNPPacketFactory.MAX_DATA_LENGTH) > 0)
			numPackets++;
		assertFalse(0 == numPackets);
		byte[] b;
		for (MNPLinkTransferPacket packet : packets) {
			b = packet.serialize();
			assertNotNull(b);
			assertFalse(0 == b.length);
			count++;
		}
		assertEquals(numPackets, count);
	}
}
