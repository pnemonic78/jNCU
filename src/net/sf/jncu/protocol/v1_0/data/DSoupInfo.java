package net.sf.jncu.protocol.v1_0.data;

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * <tt>kDSoupInfo</tt><br>
 * This command is used to send a soup info frame. When received the info for
 * the current soup is set to the specified frame.
 * 
 * <pre>
 * 'sinf'
 * length
 * soup info frame
 * </pre>
 */
public class DSoupInfo extends DockCommandFromNewtonBlank {

	public static final String COMMAND = "sinf";

	/**
	 * Creates a new command.
	 */
	public DSoupInfo() {
		super(COMMAND);
	}

}
