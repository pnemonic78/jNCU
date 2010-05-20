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

}
