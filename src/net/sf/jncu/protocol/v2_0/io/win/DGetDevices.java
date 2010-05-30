/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io.win;

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * <tt>kDGetDevices</tt><br>
 * This command asks the desktop system to return an array of device names. This
 * is only used for the Windows platform.
 * 
 * <pre>
 * 'gdev'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetDevices extends DockCommandFromNewtonBlank {

	public static final String COMMAND = "gdev";

	public DGetDevices() {
		super(COMMAND);
	}

}
