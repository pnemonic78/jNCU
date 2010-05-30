/**
 * 
 */
package net.sf.jncu.protocol.v1_0.query;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDGetPatches</tt><br>
 * Get patches.
 * 
 * <pre>
 * 'gpat'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetPatches extends DockCommandToNewtonBlank {

	public static final String COMMAND = "gpat";

	/**
	 * Creates a new command.
	 */
	public DGetPatches(String cmd) {
		super(COMMAND);
	}

}
