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
 * @author moshew
 */
public class NCUCommTalk extends Thread implements SerialPortEventListener {

	public static enum Status {
		CLOSED, OPEN, DISCONNECTED, CONNECTED;
	}

	private final NCUComm owner;
	private final CommPortIdentifier portId;
	private final int baud;
	private SerialPort port;
	private InputStream in;
	private OutputStream out;
	private Status status = Status.CLOSED;

	public NCUCommTalk(NCUComm owner, CommPortIdentifier portId, int baud) {
		super();
		this.owner = owner;
		this.portId = portId;
		this.baud = baud;
	}

	@Override
	public void run() {
		try {
			initPort();
			while (status == Status.OPEN) {
				read();
				if (status == Status.OPEN) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
						// ignore
					}
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			if (owner != null) {
//				JOptionPane.showMessageDialog(owner, ioe.getLocalizedMessage(),
//						owner.getTitle(), JOptionPane.ERROR_MESSAGE);
			}
		} finally {
			close();
		}
	}

	public void close() {
		closePort();
	}

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
				port.setSerialPortParams(baud, port.getDataBits(), port
						.getStopBits(), port.getParity());
			}
			in = new BufferedInputStream(port.getInputStream());
			status = Status.OPEN;
		} catch (PortInUseException piue) {
			throw new IOException("Port in use", piue);
		} catch (UnsupportedCommOperationException ucoe) {
			throw new IOException("Unsupported comm. operation", ucoe);
		} catch (TooManyListenersException tmle) {
			throw new IOException("Too many listeners", tmle);
		}
	}

	protected void closePort() {
		if (port != null) {
			port.removeEventListener();
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					// ignore
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					// ignore
				}
			}
			status = Status.DISCONNECTED;
			port.close();
			status = Status.CLOSED;
			port = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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
//				JOptionPane.showMessageDialog(owner, msg, owner.getTitle(),
//						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public Status getStatus() {
		return status;
	}

	protected void read() throws IOException {
		if (status == Status.CLOSED) {
			throw new IllegalStateException("Port closed");
		}
		if (status == Status.DISCONNECTED) {
			throw new IllegalStateException("Port disconnected");
		}
		int b = in.read();
		if (b != -1) {
			status = Status.CONNECTED;
			int i = 0;
			while (b != -1) {
				if (i > 0) {
					if ((i & 15) == 0) {
						System.out.println();
					} else {
						System.out.print(',');
					}
				}
				System.out.print(Integer.toHexString(b));
				b = in.read();
				i++;
			}
		}
	}
}
