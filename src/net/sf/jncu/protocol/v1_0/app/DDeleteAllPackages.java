/**
 * 
 */
package net.sf.jncu.protocol.v1_0.app;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDDeleteAllPackages</tt><br>
 * Delete all packages.
 * 
 * <pre>
 * 'dpkg'
 * </pre>
 * 
 * @author moshew
 */
public class DDeleteAllPackages extends DockCommandToNewtonBlank {

	public static final String COMMAND = "dpkg";

	/**
	 * Creates a new command.
	 */
	public DDeleteAllPackages(String cmd) {
		super(COMMAND);
	}

}
