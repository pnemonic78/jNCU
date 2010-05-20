/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * <tt>kDRefTest</tt><br>
 * This command is first sent from the desktop to the Newton. The Newton
 * immediately echos the object back to the desktop. The object can be any
 * NewtonScript object (anything that can be sent through the object
 * read/write).
 * <p>
 * This command can also be sent with no ref attached. If the length is 0 the
 * command is echoed back to the desktop with no ref included.
 * 
 * <pre>
 * 'rtst'
 * length
 * object
 * </pre>
 * 
 * @author moshew
 */
public class DRefTest extends DockCommandToNewtonScript {

	public static final String COMMAND = "rtst";

	/**
	 * Creates a new command.
	 */
	public DRefTest() {
		super(COMMAND);
	}
}
