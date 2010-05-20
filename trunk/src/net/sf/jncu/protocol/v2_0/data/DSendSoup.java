/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSendSoup</tt><br>
 * This command requests that all of the entries in a soup be returned to the
 * desktop. The Newton responds with a series of <tt>kDEntry</tt> commands for
 * all the entries in the current soup followed by a <tt>kDBackupSoupDone</tt>
 * command. All of the entries are sent without any request from the desktop (in
 * other words, a series of commands is sent). The process can be interrupted by
 * the desktop by sending a <tt>kDOperationCanceled</tt> command. The cancel
 * will be detected between entries. The <tt>kDEntry</tt> commands are sent
 * exactly as if they had been requested by a <tt>kDReturnEntry</tt> command
 * (they are long padded).
 * 
 * <pre>
 * 'snds'
 * length
 * </pre>
 * 
 * @author moshew
 */
public class DSendSoup extends DockCommandToNewton {

	public static final String COMMAND = "snds";

	/**
	 * Creates a new command.
	 */
	public DSendSoup() {
		super(COMMAND);
	}

}
