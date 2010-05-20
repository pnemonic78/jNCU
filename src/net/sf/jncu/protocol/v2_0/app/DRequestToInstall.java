package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDRequestToInstall</tt><br>
 * This command is sent from the desktop when the desktop wants to start a load
 * package operation, when both the Newton and the desktop were waiting for the
 * user to specify an operation.
 * 
 * <pre>
 * 'rins'
 * length = 0
 * </pre>
 * 
 * @author Moshe
 */
public class DRequestToInstall extends DockCommandToNewtonBlank {

	public static final String COMMAND = "rins";

	/**
	 * Constructs a new command.
	 */
	public DRequestToInstall() {
		super(COMMAND);
	}

}
