package net.sf.jncu.protocol.v2_0.data;

/**
 * <h1>Synchronise</h1>
 * <p>
 * <h2>Sync and Selective Sync</h2>
 * <p>
 * <h3>Examples</h3> After the session is started (see above) these commands
 * would be sent.
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * <th width=50%></th>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDRequestToSync</td>
 * </tr>
 * <tr>
 * <td>kDGetSyncOptions</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDSyncOptions</td>
 * </tr>
 * <tr>
 * <td>kDLastSyncTime</td>
 * <td>-></td>
 * <td>// This ones fake just to get the Newton time</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDCurrentTime</td>
 * </tr>
 * <tr>
 * <td>kDSetCurrentStore</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDLastSyncTime</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDCurrentTime</td>
 * </tr>
 * <tr>
 * <td colspan=4>The following would appear only if synchronising system info:</td>
 * </tr>
 * <tr>
 * <td>kDGetPatches</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDPatches</td>
 * </tr>
 * <tr>
 * <td colspan=4>The following would appear only if synchronising packages:</td>
 * </tr>
 * <tr>
 * <td>kDGetPackageIDs -></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDPackageIDList</td>
 * </tr>
 * <tr>
 * <td>kDBackupPackages</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDPackage</td>
 * </tr>
 * <tr>
 * <td>kDBackupPackages</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDPackage</td>
 * </tr>
 * <tr>
 * <td>kDBackupPackages</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td colspan=4>Note that the above only synchronises 1.x style packages on
 * locked 1.x cards. To complete the package sync the packages soup should also
 * by synchronised.</td>
 * </tr>
 * <tr>
 * <td colspan=4><br>
 * The sync would continue like this:</td>
 * </tr>
 * <tr>
 * <td>kDSetSoupGetInfo</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDSoupInfo</td>
 * </tr>
 * <tr>
 * <td>kDLastSyncTime</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDCurrentTime</td>
 * </tr>
 * <tr>
 * <td>kDGetSoupIDs</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDSoupIDs</td>
 * </tr>
 * <tr>
 * <td>kdGetChangedIDs</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDChangedIDs</td>
 * </tr>
 * <tr>
 * <td>kDDeleteEntries</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDAddEntry</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDAddedID</td>
 * </tr>
 * <tr>
 * <td>kDReturnEntry</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDEntry</td>
 * </tr>
 * <tr>
 * <td colspan=4>Repeat the above for each store and soup followed by:</td>
 * </tr>
 * <tr>
 * <td>kDOperationComplete</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td colspan=4><br>
 * Optionally the desktop could send this instead of the operation complete:</td>
 * </tr>
 * <tr>
 * <td>kDSyncResults</td>
 * <td>-></td>
 * </tr>
 * </table>
 */
public class DockCommandSync extends DockCommandData {

	/** Desktop to Newton. */
	public static class DesktopToNewton extends DockCommandData.DesktopToNewton {
		/**
		 * This command is sent from the desktop when the desktop wants to start
		 * a sync operation, when both the Newton and the desktop were waiting
		 * for the user to specify an operation.
		 * 
		 * <pre>
		 * 'ssyn'
		 * length = 0
		 * </pre>
		 */
		public static final String kDRequestToSync = "ssyn";
		/**
		 * This command is sent when the desktop wants to get the selective sync
		 * or selective restore info from the Newton.
		 * 
		 * <pre>
		 * 'gsyn'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetSyncOptions = "gsyn";
		/**
		 * This command can optionally be sent at the end of synchronization. If
		 * it is sent, the results are displayed on the Newton. The array looks
		 * like this: <br>
		 * <code>[["store name", restored, "soup name", count, "soup name" count],<br>
		 * &nbsp;["store name", restored, ...]]</code> <br>
		 * Restored is true if the desktop detected that the Newton had been
		 * restore to since the last sync. Count is the number of conflicting
		 * entries that were found for each soup. Soups are only in the list if
		 * they had a conflict. When a conflict is detected, the Newton version
		 * is saved and the desktop version is moved to the archive file.
		 * 
		 * <pre>
		 * 'sres'
		 * length
		 * results array
		 * </pre>
		 */
		public static final String kDSyncResults = "sres";
		/**
		 * This command is like <tt>kDGetIndexDescription</tt> except that it
		 * only returns the index description if it has been changed since the
		 * time set by the <tt>kDLastSyncTime</tt> command. If the index hasn't
		 * changed a <tt>kDRes</tt> with 0 is returned.
		 * 
		 * <pre>
		 * 'cidx'
		 * length
		 * </pre>
		 */
		public static final String kDGetChangedIndex = "cidx";
		/**
		 * This command is like <tt>kDGetSoupInfo</tt> except that it only
		 * returns the soup info if it has been changed since the time set by
		 * the <tt>kDLastSyncTime</tt> command. If the info hasn't changed a
		 * <tt>kDRes</tt> with 0 is returned.
		 * 
		 * <pre>
		 * 'cinf'
		 * length
		 * </pre>
		 */
		public static final String kDGetChangedInfo = "cinf";
	}

	/** Newton to Desktop. */
	public static class NewtonToDesktop extends DockCommandData.NewtonToDesktop {
		/**
		 * This command is sent to the desktop when the user taps the
		 * <tt>Synchronize<tt> button on the Newton.
		 * 
		 * <pre>
		 * 'sync'
		 * length
		 * </pre>
		 */
		public static final String kDSynchronize = "sync";
		/**
		 * This command is sent whenever the user on the Newton has selected
		 * selective sync. The frame sent completely specifies which information
		 * is to be synchronised.<br>
		 * <code>
		 * {<br>
		 * &nbsp;&nbsp;packages: TRUEREF,<br>
		 * &nbsp;&nbsp;syncAll: TRUEREF,<br>
		 * &nbsp;&nbsp;stores: [{store info}, {store info}]<br>
		 * }</code><br>
		 * Each store frame in the stores array contains the same information
		 * returned by the kDStoreNames command with the addition of soup
		 * information. It looks like this: <br>
		 * <code>{<br>
		 * &nbsp;&nbsp;name: "",<br>
		 * &nbsp;&nbsp;signature: 1234,<br>
		 * &nbsp;&nbsp;totalsize: 1234,<br>
		 * &nbsp;&nbsp;usedsize: 1234,<br>
		 * &nbsp;&nbsp;kind: "",<br>
		 * &nbsp;&nbsp;soups: [soup names],<br>
		 * &nbsp;&nbsp;signatures: [soup signatures]<br>
		 * &nbsp;&nbsp;info: {store info frame},<br>
		 * }</code><br>
		 * If the user has specified to sync all information the frame will look
		 * the same except there won't be a soups slot--all soups are assumed.
		 * <p>
		 * Note that the user can specify which stores to sync while specifying
		 * that all soups should be synchronised.
		 * <p>
		 * If the user specifies that packages should be synchronised the
		 * packages flag will be true and the packages soup will be specified in
		 * the store frame(s).
		 * 
		 * <pre>
		 * 'sopt'
		 * length
		 * frame of info
		 * </pre>
		 */
		public static final String kDSyncOptions = "sopt";
	}

}
