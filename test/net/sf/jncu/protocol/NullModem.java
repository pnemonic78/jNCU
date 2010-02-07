/**
 * 
 */
package net.sf.jncu.protocol;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import net.sf.jncu.comm.CommPorts;

/**
 * Use Virtual COM ports for testing. COM2 attaches to NCU. COM3 attaches to
 * this tester.
 * 
 * @author moshew
 */
public class NullModem {

	public static void main(String[] args) {
		try {
			new NullModem().test();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void test() throws Exception {
		CommPorts commPorts = new CommPorts();
		Collection<CommPortIdentifier> ports = commPorts.getPortIdentifiers(CommPortIdentifier.PORT_SERIAL);
		if (ports.size() == 0) {
			throw new NoSuchPortException();
		}
		CommPortIdentifier portId = null;
		for (Iterator<CommPortIdentifier> iter = ports.iterator(); iter.hasNext();) {
			CommPortIdentifier cpi = iter.next();
			if ("COM3".equals(cpi.getName())) {
				portId = cpi;
			}
		}
		if (portId == null) {
			throw new NoSuchPortException();
		}
		SerialPort port = (SerialPort) portId.open(getClass().getName(), 30000);
		byte[] frame = { 0x16, 0x10, 0x02, 0x26, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03, 0x01, 0x08, 0x04,
				0x02, 0x40, 0x00, 0x08, 0x01, 0x03, 0x09, 0x01, 0x01, 0x0E, 0x04, 0x03, 0x04, 0x00, (byte) 0xFA, (byte) 0xC5, 0x06, 0x01, 0x04, 0x00, 0x00,
				(byte) 0xE1, 0x00, 0x10, 0x03, (byte) 0xB9, (byte) 0xBF };

		Receiver rcv = new Receiver(port);
		rcv.start();

		OutputStream out = null;
		for (int i = 0; i < 4; i++) {
			out = port.getOutputStream();
			out.write(frame);
			out.close();
			Thread.sleep(5000);
		}
		port.close();
	}

	private class Receiver extends Thread {

		private final SerialPort port;

		public Receiver(SerialPort port) {
			this.port = port;
		}

		@Override
		public void run() {
			try {
				InputStream in = null;
				for (int i = 0; i < 10; i++) {
					System.out.println();
					System.out.println("i=" + i);
					in = new BufferedInputStream(port.getInputStream());
					read(in);
					Thread.sleep(1000);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Read bytes.
		 * 
		 * @throws IOException
		 *             if an I/O error occurs.
		 */
		protected void read(InputStream in) throws IOException {
			int i = 0;
			int b = in.read();
			while (b != -1) {
				if ((i & 15) == 0) {
					System.out.println();
				} else {
					System.out.print(' ');
				}
				System.out.print("0x" + (b < 0x10 ? "0" : "") + Integer.toHexString(b));
				b = in.read();
				i++;
			}
			in.close();
		}
	}
}
