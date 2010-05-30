/**
 * 
 */
package net.sf.jncu.protocol.v1_0.io;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDGetStoreNames</tt><br>
 * This command is sent when a list of store names is needed.
 * 
 * <pre>
 * 'gsto'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetStoreNames extends DockCommandToNewtonBlank {

	public static final String COMMAND = "gsto";

	/**
	 * Creates a new command.
	 */
	public DGetStoreNames(String cmd) {
		super(COMMAND);
	}

}
