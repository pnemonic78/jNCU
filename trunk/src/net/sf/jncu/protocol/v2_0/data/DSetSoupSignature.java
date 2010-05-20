/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSetSoupSignature</tt><br>
 * This commands sets the signature of the current soup to the specified value.
 * A <tt>kDResult</tt> with value <tt>0</tt> (or the error value if an error
 * occurred) is sent to the desktop in response.
 * 
 * <pre>
 * 'ssos'
 * length
 * new signature
 * </pre>
 * 
 * @author moshew
 */
public class DSetSoupSignature extends DockCommandToNewton {

	public static final String COMMAND = "ssos";

	/**
	 * Creates a new command.
	 */
	public DSetSoupSignature() {
		super(COMMAND);
	}

}
