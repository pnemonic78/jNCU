package net.sf.jncu.cdil;

import java.io.IOException;

/**
 * Pipe disconnected - <tt>kCD_PipeDisconnected</tt>.
 * 
 * @author moshew
 */
public class PipeDisconnectedException extends IOException {

	public PipeDisconnectedException() {
		super();
	}

	public PipeDisconnectedException(String message) {
		super(message);
	}

	public PipeDisconnectedException(Throwable cause) {
		super(cause);
	}

}
