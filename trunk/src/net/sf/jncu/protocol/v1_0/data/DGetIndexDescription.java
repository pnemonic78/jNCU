/**
 * 
 */
package net.sf.jncu.protocol.v1_0.data;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDGetIndexDescription</tt><br>
 * This command requests the definition of the indexes that should be created
 * for the current soup.
 * 
 * <pre>
 * 'gidx'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetIndexDescription extends DockCommandToNewtonBlank {

	public static final String COMMAND = "gind";

	/**
	 * Creates a new command.
	 */
	public DGetIndexDescription(String cmd) {
		super(COMMAND);
	}

}
