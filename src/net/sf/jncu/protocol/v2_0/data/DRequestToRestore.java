/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDRequestToRestore</tt><br>
 * This command is sent from the desktop when the desktop wants to start a
 * restore operation, when both the Newton and the desktop were waiting for the
 * user to specify an operation.
 * 
 * <pre>
 * 'rrst'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DRequestToRestore extends DockCommandToNewton {

	public static final String COMMAND = "rrst";

	/**
	 * Creates a new command.
	 */
	public DRequestToRestore() {
		super(COMMAND);
	}

}
