/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDSynchronize</tt><br>
 * This command is sent to the desktop when the user taps the
 * <tt>Synchronize<tt> button on the Newton.
 * 
 * <pre>
 * 'sync'
 * length
		 * </pre>
 * 
 * @author moshew
 */
public class DSynchronize extends DockCommandFromNewton {

	public static final String COMMAND = "sync";

	/**
	 * Creates a new command.
	 */
	public DSynchronize(String cmd) {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.protocol.DockCommandFromNewton#decodeData(java.io.InputStream
	 * )
	 */
	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
