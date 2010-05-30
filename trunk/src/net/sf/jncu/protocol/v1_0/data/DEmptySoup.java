/**
 * 
 */
package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDEmptySoup</tt><br>
 * This command is used by restore to remove all entries from a soup before the
 * soup data is restored.
 * 
 * <pre>
 * 'dsou'
 * length
 * soup name
 * </pre>
 * 
 * @author moshew
 */
public class DEmptySoup extends DockCommandToNewton {

	public static final String COMMAND = "esou";

	/**
	 * Creates a new command.
	 */
	public DEmptySoup(String cmd) {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
