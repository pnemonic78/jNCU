package net.sf.jncu.io;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import junit.framework.Assert;
import net.sf.junit.SFTestCase;

import org.junit.Test;

public class BufferedPipeTest extends SFTestCase {

	@Test
	public void testAvail() throws Exception {
		BufferedInputStream bi;
		PipedOutputStream po;
		PipedInputStream pi;

		po = new PipedOutputStream();
		assertNotNull(po);

		pi = new PipedInputStream(po);
		assertNotNull(pi);
		assertEquals(0, pi.available());
		bi = new BufferedInputStream(pi);
		assertNotNull(bi);
		assertEquals(0, bi.available());

		for (int i = 0; i < 100; i++)
			po.write(i);
		assertEquals(100, bi.available());
		assertEquals(100, pi.available());
		po.flush();
		assertEquals(100, pi.available());
		assertEquals(100, bi.available());
	}

	@Test
	public void testRead() throws Exception {
		BufferedInputStream bi;
		PipedOutputStream po;
		PipedInputStream pi;

		po = new PipedOutputStream();
		assertNotNull(po);

		pi = new PipedInputStream(po);
		assertNotNull(pi);
		assertEquals(0, pi.available());
		bi = new BufferedInputStream(pi);
		assertNotNull(bi);
		assertEquals(0, bi.available());

		for (int i = 0; i < 100; i++)
			po.write(i);
		assertEquals(100, pi.available());
		assertEquals(100, bi.available());
		byte[] b = new byte[100];
		int count = bi.read(b);
		assertEquals(100, count);
		assertEquals(0, pi.available());
		assertEquals(0, bi.available());

		TestWriter writer = new TestWriter(po, 100);
		writer.start();
		count = 0;
		int read;
		do {
			read = bi.read(b);
			if (read > 0)
				count += read;
		} while ((read >= 0) && (count < 100));
		assertEquals(100, count);
		assertEquals(0, pi.available());
		assertEquals(0, bi.available());
	}

	private class TestWriter extends Thread {

		private final OutputStream out;
		private final int count;

		public TestWriter(OutputStream out, int count) {
			this.out = out;
			this.count = count;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < count; i++) {
					out.write(i);
					sleep(30);
				}
			} catch (Exception e) {
				Assert.fail(e.getMessage());
			}
		}
	}
}
