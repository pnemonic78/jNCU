/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDGetChangedInfo</tt><br>
 * This command is like <tt>kDGetSoupInfo</tt> except that it only returns the
 * soup info if it has been changed since the time set by the
 * <tt>kDLastSyncTime</tt> command. If the info hasn't changed a <tt>kDRes</tt>
 * with <tt>0</tt> is returned.
 * 
 * <pre>
 * 'cinf'
 * length
 * </pre>
 * 
 * @author moshew
 */
public class DGetChangedInfo extends DockCommandToNewton {

	public static final String COMMAND = "cinf";

	/**
	 * Creates a new command.
	 */
	public DGetChangedInfo(String cmd) {
		super(COMMAND);
	}

}
