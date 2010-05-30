/**
 * 
 */
package net.sf.jncu.protocol.v1_0.data;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDGetSoupNames</tt><br>
 * This command is sent when a list of soup names is needed. It expects to
 * receive a <tt>kDSoupNames</tt> command in response.
 * 
 * <pre>
 * 'gets'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetSoupNames extends DockCommandToNewtonBlank {

	public static final String COMMAND = "gets";

	/**
	 * Creates a new command.
	 */
	public DGetSoupNames(String cmd) {
		super(COMMAND);
	}

}
