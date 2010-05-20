/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * <tt>kDRestoreOptions</tt><br>
 * This command is sent to the Newton to specify which applications and packages
 * can be restored. It is sent in response to a <tt>kDRestoreFile</tt> command
 * from the Newton. If the user elects to do a selective restore the Newton
 * returns a similar command to the desktop indicating what should be restored.
 * <p>
 * Example: <tt>restoreWhich</tt> = <code>{storeType: kRestoreToNewton,
 * 	packages: ["pkg1", ...],
 * 	applications: ["app1", ...]}</code> <br>
 * <tt>storeType</tt> slot indicates whether the data will be restored to a card
 * (<tt>kRestoreToCard = 1</tt>) or the Newton ( <tt>kRestoreToNewton = 0</tt>).
 * 
 * <pre>
 * 'ropt'
 * length
 * restoreWhich
 * </pre>
 * 
 * @author moshew
 */
public class DRestoreOptions extends DockCommandToNewtonScript {

	public static final String COMMAND = "ropt";

	/**
	 * Creates a new command.
	 */
	public DRestoreOptions() {
		super(COMMAND);
	}

}
