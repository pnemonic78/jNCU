/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;

import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;

/**
 * <tt>kDRefResult</tt><br>
 * Reference result.
 * 
 * <pre>
 * 'ref '
 * length
 * ref
 * </pre>
 * 
 * @author moshew
 */
public class DRefResult extends DockCommandFromNewtonScript {

	public static final String COMMAND = "ref ";

	/**
	 * Creates a new command.
	 */
	public DRefResult() {
		super(COMMAND);
	}

}
