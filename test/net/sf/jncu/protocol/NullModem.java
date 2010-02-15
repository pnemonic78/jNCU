/**
 * 
 */
package net.sf.jncu.protocol;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import net.sf.jncu.comm.CommPorts;
import net.sf.jncu.comm.NCUSerialPort;

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
			if ("COM1".equals(cpi.getName())) {
				portId = cpi;
				break;
			}
		}
		if (portId == null) {
			throw new NoSuchPortException();
		}
		SerialPort serialPort = (SerialPort) portId.open(getClass().getName(), 30000);
		serialPort.setSerialPortParams(NCUSerialPort.BAUD_38400, serialPort.getDataBits(), serialPort.getStopBits(), serialPort.getParity());
		NCUSerialPort port = new NCUSerialPort(serialPort);
		Reader reader = new Reader(port);
		reader.start();

		// byte[] frame = { 0x16, 0x10, 0x02, 0x26, 0x01, 0x02, 0x01, 0x06,
		// 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03,
		// 0x01, 0x08, 0x04,
		// 0x02, 0x40, 0x00, 0x08, 0x01, 0x03, 0x09, 0x01, 0x01, 0x0E, 0x04,
		// 0x03, 0x04, 0x00, (byte) 0xFA, (byte) 0xC5, 0x06, 0x01, 0x04, 0x00,
		// 0x00,
		// (byte) 0xE1, 0x00, 0x10, 0x03, (byte) 0xB9, (byte) 0xBF };
		//
		// OutputStream out = null;
		// // for (int i = 0; i < 4; i++) {
		// out = port.getOutputStream();
		// out.write(frame);
		// out.close();
		// poll(port);
		// // Thread.sleep(5000);
		// // }

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
	protected void poll(NCUSerialPort port) throws IOException {
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
		private final NCUSerialPort port;
		private boolean running;
		private InputStream in;

		Reader(NCUSerialPort port) {
			super();
			this.port = port;
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

	private void logRead(int b) {
		if (((r & 15) == 0) || (w > 0)) {
			System.out.println();
			r = 0;
			w = 0;
		} else {
			System.out.print(',');
		}
		System.out.print("<0x" + (b < 0x10 ? "0" : "") + Integer.toHexString(b));
		r++;
	}

	private void logWrite(int b) {
		if (((w & 15) == 0) || (r > 0)) {
			System.out.println();
			r = 0;
			w = 0;
		} else {
			System.out.print(',');
		}
		System.out.print(">0x" + (b < 0x10 ? "0" : "") + Integer.toHexString(b));
		w++;
	}
}
