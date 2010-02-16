package net.sf.jncu.dil;

/**
 * Invalid parameters - <tt>kDIL_InvalidParameter</tt>.
 * 
 * @author moshew
 */
public class InvalidParameterException extends IllegalArgumentException {

	public InvalidParameterException() {
		super();
	}

	public InvalidParameterException(String message) {
		super(message);
	}

}
