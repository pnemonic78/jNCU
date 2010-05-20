package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDOperationDone</tt><br>
 * This command is sent when an operation is completed. It's only sent in
 * situations where there might be some ambiguity. Currently, there are two
 * situations where this is sent. When the desktop finishes a restore it sends
 * this command. When a sync is finished and there are no sync results
 * (conflicts) to send to the Newton the desktop sends this command.
 * 
 * <pre>
 * 'opdn'
 * length = 0
 * </pre>
 */
public class DOperationDone extends DockCommandToNewtonBlank {

	public static final String COMMAND = "opdn";

	/**
	 * Creates a new command.
	 */
	public DOperationDone() {
		super(COMMAND);
	}

}
