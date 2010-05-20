/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDGetChangedIndex</tt><br>
 * This command is like <tt>kDGetIndexDescription</tt> except that it only
 * returns the index description if it has been changed since the time set by
 * the <tt>kDLastSyncTime</tt> command. If the index hasn't changed a
 * <tt>kDRes</tt> with <tt>0</tt> is returned.
 * 
 * <pre>
 * 'cidx'
 * length
 * </pre>
 * 
 * @author moshew
 */
public class DGetChangedIndex extends DockCommandToNewtonBlank {

	public static final String COMMAND = "cidx";

	/**
	 * Creates a new command.
	 */
	public DGetChangedIndex(String cmd) {
		super(COMMAND);
	}

}
