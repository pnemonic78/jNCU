/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt></tt><br>
 * Show a progress notification.
 * 
 * @author moshew
 */
public class DShowProgress extends DockCommandToNewtonBlank {

	public static final String COMMAND = "dsnc";

	/**
	 * Creates a new command.
	 */
	public DShowProgress() {
		super(COMMAND);
	}

}
