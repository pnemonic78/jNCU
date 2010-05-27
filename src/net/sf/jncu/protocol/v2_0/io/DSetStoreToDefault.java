/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * <tt>kDSetStoreToDefault</tt><br>
 * This command can be used instead of <tt>kDSetCurrentStore</tt>. It sets the
 * current store to the one the user has picked as the default store (internal
 * or card).
 * 
 * <pre>
 * 'sdef'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DSetStoreToDefault extends DockCommandToNewtonBlank {

	public static final String COMMAND = "sdef";

	/**
	 * Constructs a new command.
	 */
	public DSetStoreToDefault() {
		super(COMMAND);
	}

}
