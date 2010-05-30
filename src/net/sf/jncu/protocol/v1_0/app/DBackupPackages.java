/**
 * 
 */
package net.sf.jncu.protocol.v1_0.app;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDBackupPackages</tt><br>
 * This command is sent to backup any packages that are installed on the Newton.
 * It expects a <tt>kDPackage</tt> command or a kDResponse with an error of
 * <tt>0</tt> (to indicate that there are no more packages) in response.
 * 
 * <pre>
 * 'bpkg'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DBackupPackages extends DockCommandToNewtonBlank {

	public static final String COMMAND = "bpkg";

	/**
	 * Creates a new command.
	 */
	public DBackupPackages(String cmd) {
		super(COMMAND);
	}

}
