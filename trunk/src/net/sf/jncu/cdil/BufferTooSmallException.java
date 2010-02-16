package net.sf.jncu.cdil;

import net.sf.jncu.dil.DILException;

/**
 * Buffer too small - <tt>kCD_BufferTooSmall</tt>.
 * 
 * @author moshew
 */
public class BufferTooSmallException extends DILException {

	public BufferTooSmallException() {
		super();
	}

	public BufferTooSmallException(String message) {
		super(message);
	}

}
