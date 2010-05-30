package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDGetInternalStore</tt><br>
 * This command requests the Newton to return info about the internal store. The
 * result is described with the <tt>KDInternalStore</tt> command.
 * 
 * <pre>
 * 'gist'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetInternalStore extends DockCommandToNewtonBlank {

	public static final String COMMAND = "gist";

	/**
	 * Creates a new command.
	 */
	public DGetInternalStore() {
		super(COMMAND);
	}

}
