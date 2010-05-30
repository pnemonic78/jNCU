/**
 * 
 */
package net.sf.jncu.protocol.v1_0.data;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDGetChangedIDs</tt><br>
 * This command is sent to request a list of changed IDs for the current soup.
 * It expects to receive a <tt>kDChangedIDs</tt> command in response.
 * 
 * <pre>
 * 'gids'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetChangedIDs extends DockCommandToNewtonBlank {

	public static final String COMMAND = "gcid";

	/**
	 * Creates a new command.
	 */
	public DGetChangedIDs(String cmd) {
		super(COMMAND);
	}

}
