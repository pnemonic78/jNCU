package net.sf.jncu.cdil.mnp;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/**
 * Wraps a serial port.
 * 
 * @author moshew
 */
public class MNPSerialPort {

	public static final int BAUD_2400 = 2400;
	public static final int BAUD_4800 = BAUD_2400 << 1;
	public static final int BAUD_9600 = BAUD_4800 << 1;
	public static final int BAUD_19200 = BAUD_9600 << 1;
	public static final int BAUD_38400 = BAUD_19200 << 1;
	public static final int BAUD_57600 = 57600;
	public static final int BAUD_115200 = BAUD_57600 << 1;

	private final SerialPort ser;
	private MNPSerialPortReader reader;
	private MNPSerialPortWriter writer;

	/**
	 * Creates a new port.
	 * 
	 * @param portId
	 *            the serial port identifier.
	 * @param baud
	 *            the baud rate.
	 * @param timeout
	 *            the timeout period.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws PortInUseException
	 *             if port is not found.
	 * @throws UnsupportedCommOperationException
	 *             TODO comment me!
	 */
	public MNPSerialPort(CommPortIdentifier portId, int baud, int timeout) throws TooManyListenersException, IOException, PortInUseException,
			UnsupportedCommOperationException {
		this((SerialPort) portId.open(portId.getName(), timeout));
	}

	/**
	 * Creates a new port.
	 * 
	 * @param port
	 *            the serial port.
	 * @param baud
	 *            the baud rate.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws UnsupportedCommOperationException
	 *             TODO comment me!
	 */
	public MNPSerialPort(SerialPort port, int baud) throws TooManyListenersException, IOException, UnsupportedCommOperationException {
		super();
		this.ser = port;
		port.setSerialPortParams(baud, port.getDataBits(), port.getStopBits(), port.getParity());
		this.reader = new MNPSerialPortReader(port);
		this.writer = new MNPSerialPortWriter(port);
		reader.start();
		writer.start();
	}

	/**
	 * Creates a new port at 38,400 baud.
	 * 
	 * @param port
	 *            the serial port.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws UnsupportedCommOperationException
	 *             TODO comment me!
	 */
	public MNPSerialPort(SerialPort port) throws TooManyListenersException, IOException, UnsupportedCommOperationException {
		this(port, BAUD_38400);
	}

	/**
	 * Close the port.
	 */
	public void close() {
		try {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
		reader = null;
		writer = null;
		ser.close();
	}

	/**
	 * Get the serial port.
	 * 
	 * @return the port.
	 */
	public SerialPort getPort() {
		return ser;
	}

	/**
	 * Get the blocking input stream for reading data from the port.
	 * 
	 * @return the stream.
	 */
	public InputStream getInputStream() {
		return reader.getInputStream();
	}

	/**
	 * Get the port output stream.
	 * 
	 * @return the stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public OutputStream getOutputStream() throws IOException {
		return writer.getOutputStream();
	}

}
