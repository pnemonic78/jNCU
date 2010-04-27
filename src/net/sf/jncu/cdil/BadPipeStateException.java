package net.sf.jncu.cdil;

/**
 * Bad pipe state - <tt>kCD_BadPipeState</tt>.
 * 
 * @author moshew
 */
public class BadPipeStateException extends IllegalStateException {

	public BadPipeStateException() {
		super();
	}

	public BadPipeStateException(String message) {
		super(message);
	}

	public BadPipeStateException(Throwable cause) {
		super(cause);
	}

}
