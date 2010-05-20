/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDSoupNotDirty</tt><br>
 * This command is sent in response to a <tt>kDBackupSoup</tt> command if the
 * soup is unchanged from the last backup.
 * 
 * <pre>
 * 'ndir'
 * length
 * </pre>
 * 
 * @author moshew
 */
public class DSoupNotDirty extends DockCommandFromNewton {

	public static final String COMMAND = "ndir";

	/**
	 * Creates a new command.
	 */
	public DSoupNotDirty() {
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
