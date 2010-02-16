package net.sf.jncu.dil;

import java.io.IOException;

/**
 * Error reading from pipe - <tt>kDIL_ErrorReadingFromPipe</tt>.
 * 
 * @author moshew
 */
public class ReadingFromPipeException extends IOException {

	public ReadingFromPipeException() {
		super();
	}

	public ReadingFromPipeException(String message) {
		super(message);
	}

	public ReadingFromPipeException(Throwable cause) {
		super(cause);
	}

	public ReadingFromPipeException(String message, Throwable cause) {
		super(message, cause);
	}

}
