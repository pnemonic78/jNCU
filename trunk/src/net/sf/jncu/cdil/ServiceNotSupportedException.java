package net.sf.jncu.cdil;

import net.sf.jncu.dil.DILException;

/**
 * Service not supported error - <tt>kCD_ServiceNotSupported</tt>.
 * 
 * @author moshew
 */
public class ServiceNotSupportedException extends DILException {

	public ServiceNotSupportedException() {
		super();
	}

	public ServiceNotSupportedException(String message) {
		super(message);
	}

	public ServiceNotSupportedException(Throwable cause) {
		super(cause);
	}
}
