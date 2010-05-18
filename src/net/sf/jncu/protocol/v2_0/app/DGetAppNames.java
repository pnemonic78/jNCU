package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDGetAppNames</tt><br>
 * This command asks the Newton to send information about the applications
 * installed on the Newton. See the <tt>kDAppNames</tt> description above for
 * details of the information returned. The <tt>return what</tt> parameter
 * determines what information is returned. Here are the choices:
 * <ul>
 * <li>0: return names and soups for all stores
 * <li>1: return names and soups for current store
 * <li>2: return just names for all stores
 * <li>3: return just names for current store
 * </ul>
 * 
 * <pre>
 * 'gapp'
 * length
 * return what
 * </pre>
 * 
 * @author Moshe
 */
public class DGetAppNames extends DockCommandToNewton {

	public static final String COMMAND = "gapp";

	/**
	 * Constructs a new command.
	 */
	public DGetAppNames() {
		super(COMMAND);
	}

}
