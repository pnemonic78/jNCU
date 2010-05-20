package net.sf.jncu.protocol.v1_0;

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * <tt>kDHello</tt><br>
 * This command is sent during long operations to let the Newton or desktop know
 * that the connection hasn't been dropped.
 * 
 * <pre>
 * 'helo'
 * length = 0
 * </pre>
 */
public class DHello extends DockCommandFromNewtonBlank {

	public static final String COMMAND = "helo";

	/**
	 * Creates a new command.
	 */
	public DHello() {
		super(COMMAND);
	}

}
