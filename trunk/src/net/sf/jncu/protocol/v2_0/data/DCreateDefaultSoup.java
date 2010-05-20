/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDCreateDefaultSoup</tt><br>
 * This command creates a soup on the current store. It uses a registered
 * <tt>soupdef</tt> to create the soup meaning that the indexes, etc. are
 * determined by the ROM. If no <tt>soupdef</tt> exists for the specified soup
 * an error is returned. A kDResult is returned.
 * 
 * <pre>
 * 'cdsp'
 * length
 * soup name
 * </pre>
 * 
 * @author moshew
 */
public class DCreateDefaultSoup extends DockCommandToNewton {

	public static final String COMMAND = "cdsp";

	/**
	 * Creates a new command.
	 */
	public DCreateDefaultSoup(String cmd) {
		super(COMMAND);
	}

}
