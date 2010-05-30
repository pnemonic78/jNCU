/**
 * 
 */
package net.sf.jncu.protocol.v1_0.query;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDGetInheritance</tt><br>
 * Get inheritance.
 * 
 * <pre>
 * 'ginh'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetInheritance extends DockCommandToNewtonBlank {

	public static final String COMMAND = "ginh";

	/**
	 * Creates a new command.
	 */
	public DGetInheritance(String cmd) {
		super(COMMAND);
	}

}
