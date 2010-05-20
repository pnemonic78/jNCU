/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDRequestToSync</tt><br>
 * This command is sent from the desktop when the desktop wants to start a sync
 * operation, when both the Newton and the desktop were waiting for the user to
 * specify an operation.
 * 
 * <pre>
 * 'ssyn'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DRequestToSync extends DockCommandToNewton {

	public static final String COMMAND = "ssyn";

	/**
	 * Creates a new command.
	 */
	public DRequestToSync() {
		super(COMMAND);
	}

}
