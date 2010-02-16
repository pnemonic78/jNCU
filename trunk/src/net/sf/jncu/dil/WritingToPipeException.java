package net.sf.jncu.dil;

import java.io.IOException;

/**
 * Error writing to pipe - <tt>kDIL_ErrorWritingToPipe</tt>.
 * 
 * @author moshew
 */
public class WritingToPipeException extends IOException {

	public WritingToPipeException() {
		super();
	}

	public WritingToPipeException(String message) {
		super(message);
	}

	public WritingToPipeException(Throwable cause) {
		super(cause);
	}

	public WritingToPipeException(String message, Throwable cause) {
		super(message, cause);
	}

}
