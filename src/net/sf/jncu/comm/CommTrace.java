package net.sf.jncu.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * Trace serial port traffic.
 * <p>
 * The program traces traffic sent and received between two serial ports. At
 * least of the ports would be a null-port (virtual port), for example:<br>
 * <tt>device</tt> <-> <tt>COM1</tt> <-> <tt>CommTrace</tt> <->
 * <tt>COM2-COM3</tt> <-> <tt>application</tt><br>
 * or<br>
 * <tt>device</tt> <-> <tt>COM2-COM3</tt> <-> <tt>CommTrace</tt> <->
 * <tt>COM4-COM5</tt> <-> <tt>application</tt><br>
 * 
 * @author moshew
 */
public class CommTrace implements SerialPortEventListener {

	private SerialPort port1;
	private SerialPort port2;

	/**
	 * Creates a new trace.
	 */
	public CommTrace() {
		super();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		if ((args == null) || (args.length < 2)) {
			System.out.println("args: port1 port2");
			System.exit(1);
			return;
		}

		CommTrace tracer = new CommTrace();
		try {
			tracer.trace(args[0], args[1]);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		}
	}

	public void trace(String portName1, String portName2) throws NoSuchPortException, PortInUseException {
		CommPortIdentifier portId1 = CommPortIdentifier.getPortIdentifier(portName1);
		CommPortIdentifier portId2 = CommPortIdentifier.getPortIdentifier(portName2);
		trace(portId1, portId2);
	}

	public void trace(CommPortIdentifier portId1, CommPortIdentifier portId2) throws NoSuchPortException, PortInUseException {
		if (portId1.getPortType() != CommPortIdentifier.PORT_SERIAL) {
			throw new NoSuchPortException();
		}
		if (portId2.getPortType() != CommPortIdentifier.PORT_SERIAL) {
			throw new NoSuchPortException();
		}
		String owner = this.getClass().getName();
		SerialPort port1 = (SerialPort) portId1.open(owner, 30000);
		SerialPort port2 = (SerialPort) portId2.open(owner, 30000);
		trace(port1, port2);
	}

	public void trace(SerialPort port1, SerialPort port2) throws PortInUseException {
		this.port1 = port1;
		this.port2 = port2;

		port1.notifyOnDataAvailable(true);
		port2.notifyOnDataAvailable(true);

		try {
			port1.addEventListener(this);
			port2.addEventListener(this);
		} catch (TooManyListenersException tmle) {
			throw new PortInUseException();
		}
	}

	@Override
	public void serialEvent(SerialPortEvent e) {
		SerialPort port = (SerialPort) e.getSource();
		if (e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			boolean directionOneToTwo = false;
			try {
				InputStream in = port.getInputStream();
				OutputStream out = null;

				if (port == port1) {
					directionOneToTwo = true;
					out = port2.getOutputStream();
				} else {
					directionOneToTwo = false;
					out = port1.getOutputStream();
				}

				int b = in.read();
				do {
					if (directionOneToTwo) {
						System.out.println("> " + toHex(b));
					} else {
						System.out.println("<" + toHex(b));
					}
					out.write(b);
					b = in.read();
				} while (b != -1);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	private static final String HEX = "0123456789ABCDEF";

	private String toHex(int b) {
		char h0 = HEX.charAt(b & 0x0F);
		char h1 = HEX.charAt((b >> 4) & 0x0F);
		return "" + h1 + h0;
	}
}
