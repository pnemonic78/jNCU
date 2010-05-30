/**
 * 
 */
package net.sf.jncu.protocol.v1_0.data;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDGetSoupInfo</tt><br>
 * Get soup information.
 * 
 * <pre>
 * 'gsin'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetSoupInfo extends DockCommandToNewtonBlank {

	public static final String COMMAND = "gsin";

	/**
	 * Creates a new command.
	 */
	public DGetSoupInfo(String cmd) {
		super(COMMAND);
	}

}
