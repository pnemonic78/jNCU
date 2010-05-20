/**
 * 
 */
package net.sf.jncu.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * Docking command from Newton with no result.
 * 
 * @author Moshe
 */
public abstract class DockCommandFromNewtonBlank extends DockCommandFromNewton {

	/**
	 * Constructs a new command.
	 * 
	 * @param cmd
	 *            the command.
	 */
	public DockCommandFromNewtonBlank(String cmd) {
		super(cmd);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.protocol.DockCommandFromNewton#decodeData(java.io.InputStream
	 * )
	 */
	@Override
	protected final void decodeData(InputStream data) throws IOException {
	}

}
