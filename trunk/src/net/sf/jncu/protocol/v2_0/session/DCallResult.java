/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;

/**
 * <tt>kDCallResult</tt><br>
 * This command is sent in response to a <tt>kDCallGlobalfunction</tt> or
 * <tt>kDCallRootMethod</tt> command. The ref is the return value from the
 * function or method called.
 * 
 * <pre>
 * 'cres'
 * length
 * ref
 * </pre>
 * 
 * @author moshew
 */
public class DCallResult extends DockCommandFromNewtonScript {

	public static final String COMMAND = "cres";

	/**
	 * Creates a new command.
	 */
	public DCallResult() {
		super(COMMAND);
	}

}
