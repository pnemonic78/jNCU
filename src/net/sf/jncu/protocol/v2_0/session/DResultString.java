/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDResultString</tt><br>
 * Reports a desktop error to the Newton. The string is included since the
 * Newton doesn't know how to decode all the desktop errors (especially since
 * the Macintosh and Windows errors are different). <tt>ErrorString</tt> is a
 * ref.
 * 
 * <pre>
 * 'ress'
 * length
 * errorNumber
 * errorStringRef
 * </pre>
 * 
 * @author moshew
 */
public class DResultString extends DockCommandToNewton {

	public static final String COMMAND = "ress";

	/**
	 * Creates a new command.
	 */
	public DResultString() {
		super(COMMAND);
	}

}
