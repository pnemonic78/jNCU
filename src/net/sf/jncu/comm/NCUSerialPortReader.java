package net.sf.jncu.comm;

import gnu.io.SerialPort;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.sf.jncu.io.QueuedInputSteam;

public class NCUSerialPortReader extends Thread implements Closeable {

	private final SerialPort port;
	private NCUSerialPortEventListener listener;
	private final BlockingQueue<Byte> q = new LinkedBlockingQueue<Byte>();
	private InputStream in;

	public NCUSerialPortReader(SerialPort port) throws TooManyListenersException {
		super();
		this.port = port;
		this.listener = new NCUSerialPortEventListener(port, q);
		this.in = new QueuedInputSteam(q);
		port.notifyOnDataAvailable(true);
		port.addEventListener(listener);
	}

	@Override
	public void run() {
	}

	public void close() throws IOException {
		q.clear();
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
