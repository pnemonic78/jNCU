/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDRemoveProtocolExtension</tt><br>
 * This command removes a previously installed protocol extension.
 * 
 * <pre>
 * 'prex'
 * length
 * command
 * </pre>
 * 
 * @author moshew
 */
public class DRemoveProtocolExtension extends DockCommandToNewton {

	public static final String COMMAND = "rpex";

	/**
	 * Creates a new command.
	 */
	public DRemoveProtocolExtension() {
		super(COMMAND);
	}

}
