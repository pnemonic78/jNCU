/**
 * 
 */
package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDReturnChangedEntry</tt><br>
 * This command is sent when the desktop wants to retrieve a changed entry from
 * the current soup.
 * 
 * <pre>
 * 'rcen'
 * length
 * id
 * </pre>
 * 
 * @author moshew
 */
public class DReturnChangedEntry extends DockCommandToNewton {

	public static final String COMMAND = "rcen";

	/**
	 * Creates a new command.
	 */
	public DReturnChangedEntry(String cmd) {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
