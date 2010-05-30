/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io.win;

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * <tt>kDGetFilters</tt><br>
 * This command asks the desktop to send a list of filters to display in the
 * Windows file browser. A <tt>kDFilters</tt> command is expected in response.
 * 
 * <pre>
 * 'gflt'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetFilters extends DockCommandFromNewtonBlank {

	public static final String COMMAND = "gflt";

	public DGetFilters() {
		super(COMMAND);
	}

}
