package net.sf.jncu.cdil.tcp;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.ServiceNotSupportedException;

/**
 * TCP pipe.
 * 
 * @author moshew
 */
public class TCPPipe extends CDPipe {

	private final int port;

	/**
	 * Creates a new TCP pipe.
	 * 
	 * @param layer
	 *            the owner layer.
	 * @param port
	 *            the TCP port to listen on. Note that once the connection is
	 *            made, data transfer actually occurs on a different, randomly
	 *            chosen, port. This frees up the port specified in this
	 *            parameter for future connections.
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 */
	public TCPPipe(CDLayer layer, int port) throws ServiceNotSupportedException {
		super(layer);
		this.port = port;
	}

}
