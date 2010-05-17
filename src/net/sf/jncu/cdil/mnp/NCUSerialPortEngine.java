package net.sf.jncu.cdil.mnp;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import net.sf.jncu.NCUComm;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockingFrame;
import net.sf.jncu.protocol.v2_0.DockCommandFactory;
import net.sf.jncu.protocol.v2_0.session.DInitiateDocking;

/**
 * NCU serial port engine.
 * 
 * @author moshew
 */
@Deprecated
public class NCUSerialPortEngine extends Thread {

	private static final String ERROR_PORT_USED = "Port in use";
	private static final String ERROR_UNSUPPORTED_COMM_OP = "Unsupported comm. operation";
	private static final String ERROR_TOO_MANY_LISTENERS = "Too many listeners";
	private static final String ERROR_PORT_CLOSED = "Port closed";
	private static final String ERROR_PORT_DISCONNECTED = "Port disconnected";
	private static final String ERROR_EOF = "No more data";

	private final NCUComm owner;
	private final CommPortIdentifier portId;
	private final int baud;
	private MNPSerialPort port;

	private CDState state = CDState.UNINITIALIZED;

	public NCUSerialPortEngine(NCUComm owner, CommPortIdentifier portId, int baud) {
		super();
		this.owner = owner;
		this.portId = portId;
		this.baud = baud;
	}

	@Override
	public void run() {
		try {
			initPort();
			connectToNewton();
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
			SerialPort serialPort = (SerialPort) portId.open(getClass().getName(), 30000);
			if (baud != serialPort.getBaudRate()) {
				serialPort.setSerialPortParams(baud, serialPort.getDataBits(), serialPort.getStopBits(), serialPort.getParity());
			}
			this.port = new MNPSerialPort(serialPort);
			state = CDState.CONNECTED;
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
		state = CDState.DISCONNECT_PENDING;
		if (port != null) {
			port.close();
			port = null;
			state = CDState.DISCONNECTED;
		}
	}

	/**
	 * Get the state.
	 * 
	 * @return the state.
	 */
	public CDState getCDState() {
		return state;
	}

	public void write(byte[] b) {

	}

	/**
	 * Read bytes and populates the queue.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void poll() throws IOException {
		if (state == CDState.DISCONNECTED) {
			throw new IllegalStateException(ERROR_PORT_DISCONNECTED);
		}
		InputStream in = port.getInputStream();
		state = CDState.CONNECTED;
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
		} while ((state == CDState.CONNECTED) && (port != null));
	}

	// TODO move this method to a class in package net.sf.jncu.protocol
	protected void connectToNewton() throws IOException {
		if (state == CDState.DISCONNECTED) {
			throw new IllegalStateException(ERROR_PORT_CLOSED);
		}
		if (state == CDState.DISCONNECTED) {
			throw new IllegalStateException(ERROR_PORT_DISCONNECTED);
		}
		DockingFrame docking = new DockingFrame();
		InputStream in = port.getInputStream();
		OutputStream out = port.getOutputStream();
		DockCommandFromNewton cmdFromNewton;
		DInitiateDocking cmdInitiateDocking;
		DockCommandFactory factory = DockCommandFactory.getInstance();
		state = CDState.CONNECTED;
		do {
			docking.waitForType(in, MNPPacket.LR);
			docking.send(out, DockingFrame.PAYLOAD_DTN_HANDSHAKE_1);
			do {
				cmdFromNewton = docking.receiveCommand(in);
			} while (!DInitiateDocking.COMMAND.equals(cmdFromNewton.getCommand()));
			docking.send(out, DockingFrame.PAYLOAD_DTN_LA);
			cmdInitiateDocking = (DInitiateDocking) factory.create(DInitiateDocking.COMMAND);
			cmdInitiateDocking.setSession(0);
			docking.sendCommand(out, cmdInitiateDocking);
			docking.waitForType(in, MNPPacket.LA);
			poll();
		} while ((state == CDState.CONNECTED) && (port != null));
	}

	protected void commandReceived(DockCommandFromNewton cmd) {

	}
}
