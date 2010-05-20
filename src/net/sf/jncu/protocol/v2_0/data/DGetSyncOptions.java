/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDGetSyncOptions</tt><br>
 * This command is sent when the desktop wants to get the selective sync or
 * selective restore info from the Newton.
 * 
 * <pre>
 * 'gsyn'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetSyncOptions extends DockCommandToNewton {

	public static final String COMMAND = "gsyn";

	/**
	 * Creates a new command.
	 */
	public DGetSyncOptions() {
		super(COMMAND);
	}

}
