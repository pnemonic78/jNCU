/**
 * 
 */
package net.sf.jncu.protocol.v1_0.data;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDGetSoupIDs</tt><br>
 * This command is sent to request a list of entry IDs for the current soup. It
 * expects to receive a <tt>kDSoupIDs</tt> command in response.
 * 
 * <pre>
 * 'gids'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetSoupIDs extends DockCommandToNewtonBlank {

	public static final String COMMAND = "gids";

	/**
	 * Creates a new command.
	 */
	public DGetSoupIDs(String cmd) {
		super(COMMAND);
	}

}
