/**
 * 
 */
package net.sf.jncu.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * Docking command interface from Newton to desktop.
 * 
 * @author moshew
 */
public interface IDockCommandFromNewton extends IDockCommand {

	/**
	 * Decode the command.
	 * 
	 * @param frame
	 *            the frame data.
	 * @throws IOException
	 *             if read past data buffer.
	 */
	public void decode(InputStream frame) throws IOException;
}
