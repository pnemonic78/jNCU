/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDBackupSoupDone</tt><br>
 * This command terminates the sequence of commands sent in response to a
 * <tt>kDBackupSoup</tt> command.
 * 
 * <pre>
 * 'bsdn'
 * length
 * </pre>
 * 
 * @author moshew
 */
public class DBackupSoupDone extends DockCommandFromNewton {

	public static final String COMMAND = "bsdn";

	/**
	 * Creates a new command.
	 */
	public DBackupSoupDone(String cmd) {
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
