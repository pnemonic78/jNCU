/**
 * 
 */
package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDReturnEntry</tt><br>
 * This command is sent when the desktop wants to retrieve an entry from the
 * current soup.
 * 
 * <pre>
 * 'rete'
 * length
 * id
 * </pre>
 * 
 * @author moshew
 */
public class DReturnEntry extends DockCommandToNewton {

	public static final String COMMAND = "rete";

	/**
	 * Creates a new command.
	 */
	public DReturnEntry(String cmd) {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
