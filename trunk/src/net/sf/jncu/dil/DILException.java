package net.sf.jncu.dil;

/**
 * CDIL Error - <tt>DIL_Error</tt>.
 * 
 * @author moshew
 */
public class DILException extends Exception {

	/**
	 * Creates a new CDIL exception.
	 */
	public DILException() {
		super();
	}

	/**
	 * Creates a new CDIL exception.
	 * 
	 * @param message
	 *            the detail message.
	 */
	public DILException(String message) {
		super(message);
	}

	/**
	 * Creates a new CDIL exception.
	 * 
	 * @param cause
	 *            the cause.
	 */
	public DILException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new CDIL exception.
	 * 
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause.
	 */
	public DILException(String message, Throwable cause) {
		super(message, cause);
	}

}
