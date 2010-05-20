/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDGetRestoreOptions</tt><br>
 * This command is sent to the desktop if the user wants to do a selective
 * restore. The desktop should return a <tt>kDRestoreOptions</tt> command.
 * 
 * <pre>
 * 'grop'
 * length
 * </pre>
 * 
 * @author moshew
 */
public class DGetRestoreOptions extends DockCommandFromNewton {

	public static final String COMMAND = "grop";

	/**
	 * Creates a new command.
	 */
	public DGetRestoreOptions() {
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
