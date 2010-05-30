/**
 * 
 */
package net.sf.jncu.protocol.v1_0.app;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDGetPackageIDs</tt><br>
 * This command is sent to request a list of package ids. This list is used to
 * remove any packages from the desktop that have been deleted on the Newton.
 * 
 * <pre>
 * 'gpid'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetPackageIDs extends DockCommandToNewtonBlank {

	public static final String COMMAND = "gpid";

	/**
	 * Creates a new command.
	 */
	public DGetPackageIDs(String cmd) {
		super(COMMAND);
	}

}
