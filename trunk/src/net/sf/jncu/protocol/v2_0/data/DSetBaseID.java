/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDSetBaseID</tt><br>
 * This command sets a new base id for the ids sent with subsequent
 * <tt>kDBackupIDs</tt> commands. The new base is a long which should be added
 * to every id in all kDBackupIDs commands until a <tt>kDBackupSoupDone</tt>
 * command is received.
 * 
 * <pre>
 * 'base'
 * length
 * new base
 * </pre>
 * 
 * @author moshew
 */
public class DSetBaseID extends DockCommandFromNewton {

	public static final String COMMAND = "base";

	/**
	 * Creates a new command.
	 */
	public DSetBaseID() {
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
