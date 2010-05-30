package net.sf.jncu.protocol.v1_0.query;

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * <tt>kDPatches</tt><br>
 * Patches.
 * 
 * <pre>
 * 'patc'
 * length = 0
 * </pre>
 */
public class DPatches extends DockCommandFromNewtonBlank {

	public static final String COMMAND = "patc";

	/**
	 * Creates a new command.
	 */
	public DPatches() {
		super(COMMAND);
	}

}
