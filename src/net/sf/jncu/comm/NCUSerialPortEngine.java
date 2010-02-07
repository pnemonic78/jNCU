package net.sf.jncu.comm;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;

import net.sf.jncu.NCUComm;

/**
 * NCU serial port engine.
 * 
 * @author moshew
 */
public class NCUSerialPortEngine extends Thread {

	public static enum Status {
		CLOSED, OPEN, DISCONNECTED, CONNECTED;
	}

	private static final String ERROR_PORT_USED = "Port in use";
	private static final String ERROR_UNSUPPORTED_COMM_OP = "Unsupported comm. operation";
	private static final String ERROR_TOO_MANY_LISTENERS = "Too many listeners";
	private static final String ERROR_PORT_CLOSED = "Port closed";
	private static final String ERROR_PORT_DISCONNECTED = "Port disconnected";
	private static final String ERROR_EOF = "No more data";

	private final NCUComm owner;
	private final CommPortIdentifier portId;
	private final int baud;
	private SerialPort port;
	private NCUSerialPortReader reader;
	private NCUSerialPortWriter writer;

	private Status status = Status.CLOSED;

	public NCUSerialPortEngine(NCUComm owner, CommPortIdentifier portId, int baud) {
		super();
		this.owner = owner;
		this.portId = portId;
		this.baud = baud;
	}

	@Override
	public void run() {
		System.out.println("@@@ run enter");
		try {
			initPort();
			while (status == Status.OPEN) {
				read();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			if (owner != null) {
				// JOptionPane.showMessageDialog(owner,
				// ioe.getLocalizedMessage(),
				// owner.getTitle(), JOptionPane.ERROR_MESSAGE);
			}
		} finally {
			close();
		}
		System.out.println("@@@ run leave");
	}

	/**
	 * Close communications.
	 */
	public void close() {
		closePort();
	}

	/**
	 * Initialise the port.
	 * 
	 * @throws IOException
	 *             if a port error occurs.
	 */
	protected void initPort() throws IOException {
		try {
			port = (SerialPort) portId.open(getClass().getName(), 30000);
			if (baud != port.getBaudRate()) {
				port.setSerialPortParams(baud, port.getDataBits(), port.getStopBits(), port.getParity());
			}
			this.reader = new NCUSerialPortReader(port);
			reader.start();
			this.writer = new NCUSerialPortWriter(port);
			writer.start();
			status = Status.OPEN;
		} catch (PortInUseException piue) {
			throw new IOException(ERROR_PORT_USED, piue);
		} catch (UnsupportedCommOperationException ucoe) {
			throw new IOException(ERROR_UNSUPPORTED_COMM_OP, ucoe);
		} catch (TooManyListenersException tmle) {
			throw new IOException(ERROR_TOO_MANY_LISTENERS, tmle);
		}
	}

	/**
	 * Close the port.
	 */
	protected void closePort() {
		System.out.println("@@@ closePort enter");
		if (port != null) {
			closeStream();
			port.removeEventListener();
			port.close();
			try {
				reader.close();
				writer.close();
			} catch (IOException ioe) {
				// ignore
			}
			reader = null;
			writer = null;
			status = Status.CLOSED;
			port = null;
		}
		System.out.println("@@@ closePort leave");
	}

	/**
	 * Close the port data stream.
	 */
	protected void closeStream() {
		status = Status.DISCONNECTED;
	}

	/**
	 * Get the status.
	 * 
	 * @return the status.
	 */
	public Status getStatus() {
		return status;
	}

	public void write(byte[] b) {

	}

	/**
	 * Read bytes and populates the queue.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void read() throws IOException {
		System.out.println("@@@ read enter");
		if (status == Status.CLOSED) {
			throw new IllegalStateException(ERROR_PORT_CLOSED);
		}
		if (status == Status.DISCONNECTED) {
			throw new IllegalStateException(ERROR_PORT_DISCONNECTED);
		}
		InputStream in = reader.getInputStream();
		status = Status.CONNECTED;
		int i = 0;
		int b;
		do {
			b = in.read();
			if (b == -1) {
				throw new EOFException(ERROR_EOF);
			}
			if ((i & 15) == 0) {
				System.out.println();
			} else {
				System.out.print(' ');
			}
			System.out.print("0x" + (b < 0x10 ? "0" : "") + Integer.toHexString(b));
			i++;
		} while ((status == Status.CONNECTED) && (reader != null));
		System.out.println("@@@ read leave");
	}
}
