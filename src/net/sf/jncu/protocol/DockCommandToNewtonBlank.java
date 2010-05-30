/**
 * 
 */
package net.sf.jncu.protocol;

import java.io.IOException;
import java.io.OutputStream;

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
	protected final void writeCommandData(OutputStream data) throws IOException {
	}

}
