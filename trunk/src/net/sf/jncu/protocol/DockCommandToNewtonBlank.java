/**
 * 
 */
package net.sf.jncu.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Docking command to the Newton with no data.
 * 
 * @author Moshe
 */
public abstract class DockCommandToNewtonBlank extends DockCommandToNewton {

	/**
	 * Constructs a new command.
	 * 
	 * @param cmd
	 *            the command.
	 */
	public DockCommandToNewtonBlank(String cmd) {
		super(cmd);
	}

	@Override
	protected final ByteArrayOutputStream getCommandData() throws IOException {
		return null;
	}

}
