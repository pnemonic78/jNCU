/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDCallRootMethod</tt><br>
 * This command asks the Newton to call the specified root method. The return
 * value from the method is sent to the desktop with a <tt>kDCallResult</tt>
 * command.
 * 
 * <pre>
 * 'crmd'
 * length
 * method name symbol
 * args array
 * </pre>
 * 
 * @author moshew
 */
public class DCallRootMethod extends DockCommandToNewton {

	public static final String COMMAND = "crmd";

	/**
	 * Creates a new command.
	 */
	public DCallRootMethod() {
		super(COMMAND);
	}

}
