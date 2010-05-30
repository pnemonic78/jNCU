/**
 * 
 */
package net.sf.jncu.protocol.v1_0.query;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDLastSyncTime</tt><br>
 * The time of the last sync.
 * 
 * <pre>
 * 'stme'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DLastSyncTime extends DockCommandToNewtonBlank {

	public static final String COMMAND = "stme";

	/**
	 * Creates a new command.
	 */
	public DLastSyncTime(String cmd) {
		super(COMMAND);
	}

}
