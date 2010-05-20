/**
 * 
 */
package net.sf.jncu.cdil;

import java.util.TimerTask;

/**
 * Timeout when listening to Newton.
 * 
 * @author moshew
 */
public class CDTimeout extends TimerTask {

	private final CDPipe pipe;

	/**
	 * Creates a new timeout.
	 * 
	 * @param pipe
	 *            the pipe.
	 */
	public CDTimeout(CDPipe pipe) {
		super();
		this.pipe = pipe;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		pipe.disconnectQuiet();
	}

}
