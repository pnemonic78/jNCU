package net.sf.jncu.cdil;

import net.sf.jncu.dil.DILException;

/**
 * CDIL not initialised - <tt>kCD_CDILNotInitialized</tt>.
 * 
 * @author moshew
 */
public class CDILNotInitializedException extends DILException {

	public CDILNotInitializedException() {
		super();
	}

	public CDILNotInitializedException(String message) {
		super(message);
	}

}
