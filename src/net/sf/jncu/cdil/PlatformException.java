package net.sf.jncu.cdil;

import net.sf.jncu.dil.DILException;

/**
 * Platform error - <tt>kCD_PlatformError</tt>.
 * 
 * @author moshew
 */
public class PlatformException extends DILException {

	public PlatformException() {
		super();
	}

	public PlatformException(String message) {
		super(message);
	}

	public PlatformException(Throwable cause) {
		super(cause);
	}

	public PlatformException(String message, Throwable cause) {
		super(message, cause);
	}

}
