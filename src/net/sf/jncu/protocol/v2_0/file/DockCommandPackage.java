package net.sf.jncu.protocol.v2_0.file;

/**
 * <h1>Package loading</h1>
 * <p>
 * Package loading uses the file browsing interface described above. After the
 * user taps the load package button, the following commands are used.
 * 
 <h2>Examples</h2> After the load package button is tapped:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDLoadPackageFile</td>
 * </tr>
 * <tr>
 * <td>kDLoadPackage</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * </table>
 */
public class DockCommandPackage extends DockCommandImport {

	/** Newton to Desktop. */
	public static class NewtonToDesktop extends DockCommandImport.NewtonToDesktop {
		/**
		 * This command asks the desktop to load the package specified by the
		 * last path command and the filename string. If the selected item is at
		 * the Desktop level, a frame
		 * <code>{Name: "Business", whichVol: -1}</code> is sent. Otherwise, a
		 * string is sent.
		 * 
		 * <pre>
		 * 'lpfl'
		 * length
		 * filename
		 * </pre>
		 */
		public static final String kDLoadPackageFile = "lpfl";
	}
}
