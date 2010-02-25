package net.sf.jncu.cdil.mnp;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

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

	private final SerialPort port;
	private MNPSerialPortReader reader;
	private MNPSerialPortWriter writer;

	/**
	 * Creates a new port.
	 * 
	 * @param portId
	 *            the serial port identifier.
	 * @param baud
	 *            the baud rate.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws PortInUseException
	 *             if port is not found.
	 */
	public MNPSerialPort(CommPortIdentifier portId, int baud) throws TooManyListenersException, IOException, PortInUseException {
		this((SerialPort) portId.open(portId.getName(), baud));
	}

	/**
	 * Creates a new port.
	 * 
	 * @param port
	 *            the serial port.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public MNPSerialPort(SerialPort port) throws TooManyListenersException, IOException {
		super();
		this.port = port;
		this.reader = new MNPSerialPortReader(port);
		this.writer = new MNPSerialPortWriter(port);
		reader.start();
		writer.start();
	}

	/**
	 * Close the port.
	 */
	public void close() {
		try {
			reader.close();
			writer.close();
		} catch (IOException ioe) {
			// ignore
		}
		reader = null;
		writer = null;
		port.removeEventListener();
		port.close();
	}

	/**
	 * Get the serial port.
	 * 
	 * @return the port.
	 */
	public SerialPort getPort() {
		return port;
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
