/**
 * 
 */
package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDDeleteSoup</tt><br>
 * This command is used by restore to delete a soup if it exists on the Newton
 * but not on the desktop.
 * 
 * <pre>
 * 'dsou'
 * length
 * soup name
 * </pre>
 * 
 * @author moshew
 */
public class DDeleteSoup extends DockCommandToNewton {

	public static final String COMMAND = "dsou";

	/**
	 * Creates a new command.
	 */
	public DDeleteSoup(String cmd) {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
