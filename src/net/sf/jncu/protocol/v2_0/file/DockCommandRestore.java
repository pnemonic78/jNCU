package net.sf.jncu.protocol.v2_0.file;

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
public class DockCommandRestore extends DockCommandFile {

	/** Newton to Desktop. */
	public static class NewtonToDesktop extends DockCommandFile.NewtonToDesktop {
		/**
		 * This command asks the desktop to restore the file specified by the
		 * last path command and the filename. If the selected item is at the
		 * Desktop level, a frame <code>{name: "Business", whichVol:-1}</code>
		 * is sent. Otherwise, a string is sent.
		 * 
		 * <pre>
		 * 'rsfl'
		 * length
		 * filename
		 * </pre>
		 */
		public static final String kDRestoreFile = "rsfl";
		/**
		 * This command is sent to the desktop if the user wants to do a
		 * selective restore. The desktop should return a
		 * <tt>kDRestoreOptions</tt> command.
		 * 
		 * <pre>
		 * 'grop'
		 * length
		 * </pre>
		 */
		public static final String kDGetRestoreOptions = "grop";
		/**
		 * This command is sent to the desktop if the user elects to restore all
		 * information. <tt>Merge</tt> is <tt>0</tt> to not merge, <tt>1</tt> to
		 * merge.
		 * 
		 * <pre>
		 * 'rall'
		 * length
		 * merge
		 * </pre>
		 */
		public static final String kDRestoreAll = "rall";
	}

	/**
	 * This command is sent to the Newton to specify which applications and
	 * packages can be restored. It is sent in response to a
	 * <tt>kDRestoreFile</tt> command from the Newton. If the user elects to do
	 * a selective restore the Newton returns a similar command to the desktop
	 * indicating what should be restored.
	 * <p>
	 * Example: <tt>restoreWhich</tt> = <code>{storeType: kRestoreToNewton,
	 * 	packages: ["pkg1", ...],
	 * 	applications: ["app1", ...]}</code> <br>
	 * <tt>storeType</tt> slot indicates whether the data will be restored to a
	 * card (<tt>kRestoreToCard = 1</tt>) or the Newton (
	 * <tt>kRestoreToNewton = 0</tt>).
	 * 
	 * <pre>
	 * 'ropt'
	 * length
	 * restoreWhich
	 * </pre>
	 */
	public static final String kDRestoreOptions = "ropt";
	/**
	 * This command sends all the entries associated with a package to the
	 * Newton in a single array. Packages are made up of at least 2 entries: one
	 * for the package info and one for each part in the package. All of these
	 * entries must be restored at the same time to restore a working package. A
	 * <tt>kDResult</tt> is returned after the package has been successfully
	 * restored.
	 * 
	 * <pre>
	 * 'rpkg'
	 * length
	 * package array
	 * </pre>
	 */
	public static final String kDRestorePackage = "rpkg";

}