package net.sf.jncu.protocol.v1_0;

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * <tt>kDDisconnect</tt><br>
 * This command is sent to the Newton when the docking operation is complete.
 * 
 * <pre>
 * 'disc'
 * length = 0
 * </pre>
 */
public class DDisconnect extends DockCommandFromNewtonBlank {

	public static final String COMMAND = "disc";

	/**
	 * Creates a new command.
	 */
	public DDisconnect() {
		super(COMMAND);
	}

}
