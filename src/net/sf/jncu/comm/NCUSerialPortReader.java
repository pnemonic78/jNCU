package net.sf.jncu.comm;

import gnu.io.SerialPort;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.TooManyListenersException;

/**
 * NCU serial port reader.
 * 
 * @author moshew
 */
public class NCUSerialPortReader extends Thread implements Closeable {

	private final SerialPort port;
	private NCUSerialPortEventListener listener;
	private final PipedOutputStream q = new PipedOutputStream();
	private InputStream in;

	/**
	 * Creates a new port reader.
	 * 
	 * @param port
	 *            the serial port.
	 * @throws TooManyListenersException
	 *             if too many listeners.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public NCUSerialPortReader(SerialPort port) throws TooManyListenersException, IOException {
		super();
		this.port = port;
		this.listener = new NCUSerialPortEventListener(port, q);
		this.in = new PipedInputStream(q);
		port.notifyOnDataAvailable(true);
		port.addEventListener(listener);
	}

	@Override
	public void run() {
	}

	public void close() throws IOException {
		if (listener != null) {
			listener.close();
			listener = null;
		}
		if (in != null) {
			try {
				in.close();
			} catch (Exception e) {
				// ignore
			}
			in = null;
		}
	}

	/**
	 * Get the blocking input stream for reading data from the port.
	 * 
	 * @return the stream.
	 */
	public InputStream getInputStream() {
		return in;
	}

}
