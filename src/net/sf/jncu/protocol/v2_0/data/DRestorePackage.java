/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * <tt>kDRestorePackage</tt><br>
 * This command sends all the entries associated with a package to the Newton in
 * a single array. Packages are made up of at least 2 entries: one for the
 * package info and one for each part in the package. All of these entries must
 * be restored at the same time to restore a working package. A
 * <tt>kDResult</tt> is returned after the package has been successfully
 * restored.
 * 
 * <pre>
 * 'rpkg'
 * length
 * package array
 * </pre>
 * 
 * @author moshew
 */
public class DRestorePackage extends DockCommandToNewtonScript {

	public static final String COMMAND = "rpkg";

	/**
	 * Creates a new command.
	 */
	public DRestorePackage() {
		super(COMMAND);
	}

}
