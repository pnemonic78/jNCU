/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDRestorePatch</tt><br>
 * This command is used to restore the patch backed up with
 * <tt>kDGetPatches</tt>. The Newton returns a <tt>kDResult</tt> of 0 (or an
 * error if appropriate) if the patch wasn't installed. If the patch was
 * installed the Newton restarts.
 * 
 * <pre>
 * 'rpat'
 * length
 * patch
 * </pre>
 * 
 * @author moshew
 */
public class DRestorePatch extends DockCommandToNewton {

	public static final String COMMAND = "rpat";

	/**
	 * Creates a new command.
	 */
	public DRestorePatch() {
		super(COMMAND);
	}

}
