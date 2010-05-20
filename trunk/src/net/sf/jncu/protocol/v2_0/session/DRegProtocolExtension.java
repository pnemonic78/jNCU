/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDRegProtocolExtension</tt><br>
 * This command installs a protocol extension into the Newton. The extension
 * lasts for the length of the current connection (in other words, you have to
 * install the extension every time you connect). The function is a Newton
 * script closure that would have to be compiled on the desktop. See the Dante
 * Connection (ROM) API IU document for details. A <tt>kDResult</tt> with value
 * <tt>0</tt> (or the error value if an error occurred) is sent to the desktop
 * in response.
 * 
 * <pre>
 * 'pext'
 * length
 * command
 * function
 * </pre>
 * 
 * @author moshew
 */
public class DRegProtocolExtension extends DockCommandToNewton {

	public static final String COMMAND = "pext";

	/**
	 * Creates a new command.
	 */
	public DRegProtocolExtension() {
		super(COMMAND);
	}

}
