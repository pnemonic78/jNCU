package net.sf.jncu.protocol.v1_0.session;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDDisconnect</tt><br>
 * This command is sent to the Newton when the docking operation is complete.
 * 
 * <pre>
 * 'disc'
 * length = 0
 * </pre>
 */
public class DDisconnect extends DockCommandToNewtonBlank {

	public static final String COMMAND = "disc";

	/**
	 * Creates a new command.
	 */
	public DDisconnect() {
		super(COMMAND);
	}

}
