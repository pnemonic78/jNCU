/**
 * 
 */
package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDDeleteEntries</tt><br>
 * This command is sent to delete one or more entries from the current soup.
 * 
 * <pre>
 * 'dele'
 * length
 * count
 * array of ids
 * </pre>
 * 
 * @author moshew
 */
public class DDeleteEntries extends DockCommandToNewton {

	public static final String COMMAND = "dele";

	/**
	 * Creates a new command.
	 */
	public DDeleteEntries(String cmd) {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
