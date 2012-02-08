/**
 * 
 */
package net.sf.jncu.cdil.mnp;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import net.sf.jncu.cdil.mnp.CommPorts;
import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.junit.SFTestCase;

/**
 * Use Virtual COM ports for testing. COM2 attaches to NCU. COM3 attaches to
 * this tester.
 * 
 * @author moshew
 */
public class NullModem extends SFTestCase {

	public void testNModem() throws Exception {
		CommPorts commPorts = new CommPorts();
		Collection<CommPortIdentifier> ports = commPorts.getPortIdentifiers(CommPortIdentifier.PORT_SERIAL);
		if (ports.size() == 0) {
			throw new NoSuchPortException();
		}
		CommPortIdentifier portId = null;
		for (Iterator<CommPortIdentifier> iter = ports.iterator(); iter.hasNext();) {
			CommPortIdentifier cpi = iter.next();
			if ("COM1".equals(cpi.getName())) {
				portId = cpi;
				break;
			}
		}
		if (portId == null) {
			throw new NoSuchPortException();
		}
		MNPSerialPort port = new MNPSerialPort(portId, MNPSerialPort.BAUD_38400, 30000);
		Reader reader = new Reader(port);
		reader.start();

		Thread.sleep(10000);
		reader.close();
		port.close();
	}

	/**
	 * Read bytes and populates the queue.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void poll(MNPSerialPort port) throws IOException {
		InputStream in = port.getInputStream();
		int i = 0;
		int b;
		do {
			b = in.read();
			if (b == -1) {
				throw new EOFException();
			}
			if ((i & 15) == 0) {
				System.out.println();
			} else {
				System.out.print(' ');
			}
			System.out.print("0x" + (b < 0x10 ? "0" : "") + Integer.toHexString(b));
			i++;
		} while (true);
	}

	private class Reader extends Thread {
		private boolean running;
		private InputStream in;

		Reader(MNPSerialPort port) {
			super();
			this.in = port.getInputStream();
		}

		@Override
		public void run() {
			try {
				this.running = true;

				int b;
				do {
					b = in.read();
					if (b == -1) {
						break;
					}
					logRead(b);
				} while (running);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		public void close() {
			running = false;
			try {
				in.close();
			} catch (Exception e) {
				// ignore
			}
		}
	}

	private int r = 0;
	private int w = 0;

	protected void logRead(int b) {
		if (((r & 15) == 0) || (w > 0)) {
			System.out.println();
			System.out.println('<');
			r = 0;
			w = 0;
		} else {
			System.out.print(',');
		}
		System.out.print("0x" + (b < 0x10 ? "0" : "") + Integer.toHexString(b));
		r++;
	}

	protected void logWrite(int b) {
		if (((w & 15) == 0) || (r > 0)) {
			System.out.println();
			System.out.println('>');
			r = 0;
			w = 0;
		} else {
			System.out.print(',');
		}
		System.out.print("0x" + (b < 0x10 ? "0" : "") + Integer.toHexString(b));
		w++;
	}
}
