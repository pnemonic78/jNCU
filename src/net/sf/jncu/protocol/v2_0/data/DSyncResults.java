/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * <tt>kDSyncResults</tt><br>
 * This command can optionally be sent at the end of synchronization. If it is
 * sent, the results are displayed on the Newton. The array looks like this: <br>
 * <code>[["store name", restored, "soup name", count, "soup name" count],<br>
 * &nbsp;["store name", restored, ...]]</code> <br>
 * Restored is true if the desktop detected that the Newton had been restore to
 * since the last sync. Count is the number of conflicting entries that were
 * found for each soup. Soups are only in the list if they had a conflict. When
 * a conflict is detected, the Newton version is saved and the desktop version
 * is moved to the archive file.
 * 
 * <pre>
 * 'sres'
 * length
 * results array
 * </pre>
 * 
 * @author moshew
 */
public class DSyncResults extends DockCommandToNewtonScript {

	public static final String COMMAND = "sres";

	/**
	 * Creates a new command.
	 */
	public DSyncResults() {
		super(COMMAND);
	}

}
