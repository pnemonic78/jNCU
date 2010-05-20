/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSetSoupGetInfo</tt><br>
 * This command is like a combination of <tt>kDSetCurrentSoup</tt> and
 * <tt>kDGetChangedInfo</tt>. It sets the current soup -- see
 * <tt>kDSetCurrentSoup</tt> for details. A <tt>kDSoupInfo</tt> or
 * <tt>kDRes</tt> command is sent by the Newton in response.
 * 
 * <pre>
 * 'ssgi'
 * length
 * soup name
 * </pre>
 * 
 * @author moshew
 */
public class DSetSoupGetInfo extends DockCommandToNewton {

	public static final String COMMAND = "ssgi";

	/**
	 * Creates a new command.
	 */
	public DSetSoupGetInfo() {
		super(COMMAND);
	}

}
