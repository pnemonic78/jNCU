/**
 * 
 */
package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDAddEntry</tt><br>
 * This command is sent when the desktop wants to add an entry to the current
 * soup.
 * 
 * <pre>
 * 'adde'
 * length
 * entry ref
 * </pre>
 * 
 * @author moshew
 */
public class DAddEntry extends DockCommandToNewton {

	public static final String COMMAND = "adde";

	/**
	 * Creates a new command.
	 */
	public DAddEntry(String cmd) {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
