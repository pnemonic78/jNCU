package net.sf.jncu.protocol.v2_0.data;

/**
 * <h1>Restore originated on Newton</h1>
 * <p>
 * Restore uses the file browsing interface described above. After the user taps
 * the restore button, the following commands are used.
 * <h2>Examples</h2>
 * <p>
 * After the restore button is tapped:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDRestoreFile</td>
 * </tr>
 * <tr>
 * <td>kDRes</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDGetRestoreOptions</td>
 * </tr>
 * <tr>
 * <td>kDRestoreOptions</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDRestoreOptions</td>
 * </tr>
 * <tr>
 * <td>kDSourceVersion</td>
 * <td>-></td>
 * </tr>
 * </table>
 * <p>
 * Selective restore proceeds as a normal restore would except when it wants to
 * restore a package. In this case it does this:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td>kDRemovePackage</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDRestorePackage</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * </table>
 * <p>
 * If the user picks a full restore it proceeds like this:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDRestoreFile</td>
 * </tr>
 * <tr>
 * <td>kDRes</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDRestoreAll</td>
 * </tr>
 * <tr>
 * <td>kDSourceVersion</td>
 * <td>-></td>
 * </tr>
 * </table>
 */
public class DockCommandRestore extends DockCommandData {

}
