package net.sf.jncu.protocol.v2_0.file;

/**
 * <h1>File importing</h1>
 * <p>
 * File importing uses the file browsing interface described above. After the
 * user taps the import button, the following commands are used.
 * <h2>Examples</h2>
 * <p>
 * When the user taps the import button:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDImportFile</td>
 * </tr>
 * </table>
 * <p>
 * When there's more than one translator available:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td>kDTranslatorList</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDSetTranslator</td>
 * </tr>
 * <tr>
 * <td>kDImporting</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * </table>
 * <p>
 * When there's only one translator available:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td>kDImporting</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * </table>
 * <p>
 * When the data is ready to be sent to the Newton:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td>kDSetStoreToDefault</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDSetCurrentSoup</td>
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
 * <td>kDAddEntry</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDAddedID</td>
 * </tr>
 * <tr>
 * <td>kDSoupsChanged</td>
 * <td>-></td>
 * </tr>
 * </table>
 */
public class DockCommandImport extends DockCommandFile {

	/** Desktop to Newton. */
	public static class DesktopToNewton extends DockCommandFile.DesktopToNewton {
		/**
		 * This command returns an array of translators that can be used with
		 * the specified file. The list can include DataViz translators and tab
		 * templates. The array should be in the order that the translators
		 * should be displayed in the list.
		 * 
		 * <pre>
		 * 'trnl'
		 * length
		 * array of strings
		 * </pre>
		 */
		public static final String kDTranslatorList = "trnl";
		/**
		 * This command is sent to let the Newton know that an import operation
		 * is starting. The Newton will display an appropriate message after it
		 * gets this message.
		 * 
		 * <pre>
		 * 'dimp'
		 * length = 0
		 * </pre>
		 */
		public static final String kDImporting = "dimp";
		/**
		 * This command returns information about what was imported into the
		 * Newton. Each array element specifies a soup and how many entries were
		 * added to it. There will typically be only one frame in the array. The
		 * frame will look like this:<br>
		 * <code>[{soupName: "Notes", count: 7}, {soupName: "Names", count: 3}]</code>
		 * 
		 * <pre>
		 * 'schg'
		 * length
		 * array
		 * </pre>
		 */
		public static final String kDSoupsChanged = "schg";
		/**
		 * This command can be used instead of <tt>kDSetCurrentStore</tt>. It
		 * sets the current store to the one the user has picked as the default
		 * store (internal or card).
		 * 
		 * <pre>
		 * 'sdef'
		 * length = 0
		 * </pre>
		 */
		public static final String kDSetStoreToDefault = "sdef";
	}

	/** Newton to Desktop. */
	public static class NewtonToDesktop extends DockCommandFile.NewtonToDesktop {
		/**
		 * This command asks the desktop to import the file specified by the
		 * last path command and the filename string. The response to this can
		 * be either a list of translators (if there is more than one applicable
		 * translator) or an indication that importing is in progress. If the
		 * selected item is at the Desktop level, a frame
		 * <code>{Name: "Business", whichVol: -1}</code> is sent. Otherwise, a
		 * string is sent.
		 * 
		 * <pre>
		 * 'impt'
		 * length
		 * filename string
		 * </pre>
		 */
		public static final String kDImportFile = "impt";
		/**
		 * This command specifies which translator the desktop should use to
		 * import the file. The translator index is the index into the
		 * translator list sent by the desktop in the <tt>kDTranslatorList</tt>
		 * command. The desktop should acknowledge this command with an
		 * indication that the import is proceeding.
		 * 
		 * <pre>
		 * 'tran'
		 * length
		 * translator index
		 * </pre>
		 */
		public static final String kDSetTranslator = "tran";
	}
}
