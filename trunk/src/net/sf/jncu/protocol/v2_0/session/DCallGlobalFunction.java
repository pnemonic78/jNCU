/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDCallGlobalFunction</tt><br>
 * This command asks the Newton to call the specified function and return it's
 * result. This function must be a global function. The return value from the
 * function is sent to the desktop with a <tt>kDCallResult</tt> command.
 * 
 * <pre>
 * 'cgfn'
 * length
 * function name symbol
 * args array
 * </pre>
 * 
 * @author moshew
 */
public class DCallGlobalFunction extends DockCommandToNewton {

	public static final String COMMAND = "cgfn";

	/**
	 * Creates a new command.
	 */
	public DCallGlobalFunction() {
		super(COMMAND);
	}
}
