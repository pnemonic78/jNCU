/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDGetPassword</tt><br>
 * This command displays the password slip to let the user enter a password. The
 * string is displayed as the title of the slip. A <tt>kDPassword</tt> command
 * is returned.
 * 
 * <pre>
 * 'gpwd'
 * length
 * string ref
 * </pre>
 * 
 * @author moshew
 */
public class DGetPassword extends DockCommandToNewton {

	public static final String COMMAND = "gpwd";

	/**
	 * Creates a new command.
	 */
	public DGetPassword() {
		super(COMMAND);
	}

}
