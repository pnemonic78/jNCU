package net.sf.jncu;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/**
 * NCU
 * 
 * @author moshew
 */
public class NCUCommPorter extends Thread implements SerialPortEventListener {

	public static enum Status {
		CLOSED, OPEN, DISCONNECTED, CONNECTED;
	}

	private static final String ERROR_PORT_USED = "Port in use";
	private static final String ERROR_UNSUPPORTED_COMM_OP = "Unsupported comm. operation";
	private static final String ERROR_TOO_MANY_LISTENERS = "Too many listeners";
	private static final String ERROR_PORT_CLOSED = "Port closed";
	private static final String ERROR_PORT_DISCONNECTED = "Port disconnected";

	private final NCUComm owner;
	private final CommPortIdentifier portId;
	private final int baud;
	private SerialPort port;
	private InputStream in;
	private OutputStream out;
	private Status status = Status.CLOSED;

	public NCUCommPorter(NCUComm owner, CommPortIdentifier portId, int baud) {
		super();
		this.owner = owner;
		this.portId = portId;
		this.baud = baud;
	}

	@Override
	public void run() {
		try {
			initPort();
			openStream();
			while (status == Status.OPEN) {
				read();
				if (status == Status.OPEN) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
						// ignore
					}
				} else if (status == Status.DISCONNECTED) {
					// Stream was rudely disconnected, so keep polling.
					openStream();
				}
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
			port.notifyOnBreakInterrupt(true);
			port.notifyOnFramingError(true);
			port.notifyOnOutputEmpty(true);
			port.notifyOnOverrunError(true);
			port.notifyOnParityError(true);
			port.addEventListener(this);
			if (baud != port.getBaudRate()) {
				port.setSerialPortParams(baud, port.getDataBits(), port.getStopBits(), port.getParity());
			}
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
		if (port != null) {
			closeStream();
			port.removeEventListener();
			port.close();
			status = Status.CLOSED;
			port = null;
		}
	}

	/**
	 * Close the port data stream.
	 */
	protected void closeStream() {
		if (in != null) {
			try {
				in.close();
			} catch (Exception e) {
				// ignore
			}
			in = null;
		}
		if (out != null) {
			try {
				out.close();
			} catch (Exception e) {
				// ignore
			}
			out = null;
		}
		status = Status.DISCONNECTED;
	}

	/**
	 * Get the port data stream.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void openStream() throws IOException {
		in = new BufferedInputStream(port.getInputStream());
		status = Status.OPEN;
	}

	/*
	 * (non-Javadoc)
	 * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
	 */
	public void serialEvent(SerialPortEvent e) {
		String msg = null;

		switch (e.getEventType()) {
		case SerialPortEvent.BI:
			msg = "Break interrupt";
			break;
		// case SerialPortEvent.CD:msg="Break interrupt";break;
		// case SerialPortEvent.CTS:msg="Clear to send";break;
		// case SerialPortEvent.DSR:
		// msg = "Data set ready";
		// break;
		case SerialPortEvent.FE:
			msg = "Framing error";
			break;
		case SerialPortEvent.OE:
			msg = "Overrun error";
			break;
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			msg = "Output buffer empty";
			break;
		case SerialPortEvent.PE:
			msg = "Parity error";
			break;
		// case SerialPortEvent.RI:msg="Ring indicator";break;
		}
		if (msg != null) {
			if (owner == null) {
				System.err.println(msg);
			} else {
				System.out.println(msg);
				// JOptionPane.showMessageDialog(owner, msg, owner.getTitle(),
				// JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Get the status.
	 * 
	 * @return the status.
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Read bytes.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void read() throws IOException {
		if (status == Status.CLOSED) {
			throw new IllegalStateException(ERROR_PORT_CLOSED);
		}
		if (status == Status.DISCONNECTED) {
			throw new IllegalStateException(ERROR_PORT_DISCONNECTED);
		}
		int b = in.read();
		if (b != -1) {
			status = Status.CONNECTED;
			int i = 0;
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
			closeStream();
		}
	}

}
