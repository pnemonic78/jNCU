package net.sf.jncu.cdil.mnp;

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
public class MNPSerialPortReader extends Thread implements Closeable {

	protected final SerialPort port;
	private MNPSerialPortEventListener listener;
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
	public MNPSerialPortReader(SerialPort port) throws TooManyListenersException, IOException {
		super();
		this.port = port;
		this.listener = new MNPSerialPortEventListener(port, q);
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
