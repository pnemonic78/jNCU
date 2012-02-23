package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;

/**
 * Sent by the Newton when the user clicked the Backup icon. <br>
 * Sent again by the Newton when the user clicked the Backup button.
 * <p>
 * The result object is an immediate value.
 * <p>
 * This command is undocumented.
 * 
 * @author Moshe
 */
public class DNewtonTICommand extends DockCommandFromNewtonScript<NSOFImmediate> {

	public static final String COMMAND = "ntic";

	/**
	 * Creates a new command.
	 */
	public DNewtonTICommand() {
		super(COMMAND);
	}
}
