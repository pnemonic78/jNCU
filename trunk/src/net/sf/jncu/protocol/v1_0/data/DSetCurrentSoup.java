/**
 * 
 */
package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSetCurrentSoup</tt><br>
 * This command sets the current soup. Most of the other commands pertain to
 * this soup so this command must precede any command that uses the current
 * soup. If the soup doesn't exist a <tt>kDSoupNotFound</tt> error is returned
 * but the connection is left alive so the desktop can create the soup if
 * necessary. Soup names must be < 25 characters.
 * 
 * <pre>
 * 'ssou'
 * length
 * soup name
 * </pre>
 * 
 * @author moshew
 */
public class DSetCurrentSoup extends DockCommandToNewton {

	public static final String COMMAND = "ssou";

	/**
	 * Creates a new command.
	 */
	public DSetCurrentSoup(String cmd) {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
