/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * <tt>kDResolveAlias</tt><br>
 * Resolve alias.
 * 
 * <pre>
 * 'rali'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DResolveAlias extends DockCommandFromNewtonBlank {

	public static final String COMMAND = "rali";

	public DResolveAlias() {
		super(COMMAND);
	}

}
