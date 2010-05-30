/**
 * 
 */
package net.sf.jncu.protocol.v1_0.app;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDDeletePkgDir</tt><br>
 * Delete package dir.
 * 
 * <pre>
 * 'dpkd'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DDeletePkgDir extends DockCommandToNewtonBlank {

	public static final String COMMAND = "dpkd";

	/**
	 * Creates a new command.
	 */
	public DDeletePkgDir(String cmd) {
		super(COMMAND);
	}

}
