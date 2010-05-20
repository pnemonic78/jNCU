package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * <tt>kDOperationCanceled</tt><br>
 * This command is sent when the user cancels an operation. Usually no action is
 * required on the receivers part except to return to the "ready" state.
 * 
 * <pre>
 * 'opcn'
 * length = 0
 * </pre>
 */
public class DOperationCanceled extends DockCommandFromNewtonBlank {

	public static final String COMMAND = "opcn";

	/**
	 * Creates a new command.
	 */
	public DOperationCanceled() {
		super(COMMAND);
	}

}
