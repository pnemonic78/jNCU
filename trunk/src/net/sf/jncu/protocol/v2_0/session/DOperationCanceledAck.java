package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * <tt>kDOpCanceledAck</tt><br>
 * This command is sent in response to a <tt>kDOperationCanceled</tt>.
 * 
 * <pre>
 * 'ocaa'
 * length = 0
 * </pre>
 */
public class DOperationCanceledAck extends DockCommandFromNewtonBlank {

	public static final String COMMAND = "ocaa";

	/**
	 * Creates a new command.
	 */
	public DOperationCanceledAck() {
		super(COMMAND);
	}

}
