/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDImporting</tt><br>
 * This command is sent to let the Newton know that an import operation is
 * starting. The Newton will display an appropriate message after it gets this
 * message.
 * 
 * <pre>
 * 'dimp'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DImporting extends DockCommandToNewtonBlank {

	public static final String COMMAND = "dimp";

	/**
	 * Constructs a new command.
	 */
	public DImporting() {
		super(COMMAND);
	}

}
