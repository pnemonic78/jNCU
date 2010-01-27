package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

/**
 * <h1>Package loading</h1>
 * <p>
 * Package loading uses the file browsing interface described above. After the
 * user taps the load package button, the following commands are used.
 * 
 <h2>Examples</h2>
 * <p>
 * After the load package button is tapped:
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
public class DockCommandPackage extends DockingEventCommands {

	/** Desktop to Newton. */
	public static class DesktopToNewton extends DockingEventCommands.DesktopToNewton {
		/**
		 * This command tells the Newton to delete a package. It can be used
		 * during selective restore or any other time.
		 * 
		 * <pre>
		 * 'rmvp'
		 * length
		 * name ref
		 * </pre>
		 */
		public static final String kDRemovePackage = "rmvp";
		/**
		 * The package info for the specified package is returned. See the
		 * <tt>kDPackageInfo</tt> command described below Note that multiple
		 * packages could be returned because there may be multiple packages
		 * with the same title but different package ids. Note that this finds
		 * packages only in the current store.
		 * 
		 * <pre>
		 * 'gpin'
		 * length
		 * title ref
		 * </pre>
		 */
		public static final String kDGetPackageInfo = "gpin";
		/**
		 * This command asks the Newton to send information about the
		 * applications installed on the Newton. See the <tt>kDAppNames</tt>
		 * description above for details of the information returned. The
		 * <tt>return what</tt> parameter determines what information is
		 * returned. Here are the choices:
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
		 */
		public static final String kDGetAppNames = "gapp";
		/**
		 * This command is sent from the desktop when the desktop wants to start
		 * a load package operation, when both the Newton and the desktop were
		 * waiting for the user to specify an operation.
		 * 
		 * <pre>
		 * 'rins'
		 * length = 0
		 * </pre>
		 */
		public static final String kDRequestToInstall = "rins";
	}

	/** Newton to Desktop. */
	public static class NewtonToDesktop extends DockingEventCommands.NewtonToDesktop {
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
		/**
		 * This command returns the names of the applications present on the
		 * Newton. It also, optionally, returns the names of the soups
		 * associated with each application. The array looks like this:
		 * <code>[{name: "app name", soups: ["soup1", "soup2"]},<br/>
		 * &nbsp;{name: "another app name", ...}, ...]</code>
		 * <p>
		 * Some built-in names are included. "System information" includes the
		 * system and directory soups. If there are packages installed, a
		 * "Packages" item is listed. If a card is present and has a backup
		 * there will be a "Card backup" item. If there are soups that don't
		 * have an associated application (or whose application I can't figure
		 * out) there's an "Other information" entry.
		 * <p>
		 * The soup names are optionally returned depending on the value
		 * received with <tt>kDGetAppNames</tt>.
		 * 
		 * <pre>
		 * 'appn'
		 * length
		 * result frame
		 * </pre>
		 */
		public static final String kDAppNames = "appn";
		/**
		 * This command is sent in response to a <tt>kDGetPackageInfo</tt>
		 * command. An array is returned that contains a frame for each package
		 * with the specified name (there may be more than one package with the
		 * same name but different package id). The returned frame looks like
		 * this:<br>
		 * <code>
		 * {<br>
		 * &nbsp;&nbsp;name: "The name passed in",<br>
		 * &nbsp;&nbsp;packagesize: 123,<br>
		 * &nbsp;&nbsp;packageid: 123,<br>
		 * &nbsp;&nbsp;packageversion: 1,<br>
		 * &nbsp;&nbsp;format: 1,<br>
		 * &nbsp;&nbsp;devicekind: 1,<br>
		 * &nbsp;&nbsp;devicenumber: 1,<br>
		 * &nbsp;&nbsp;deviceid: 1,<br>
		 * &nbsp;&nbsp;modtime: 123213213,<br>
		 * &nbsp;&nbsp;iscopyprotected: true,<br>
		 * &nbsp;&nbsp;length: 123,<br>
		 * &nbsp;&nbsp;safetoremove: true<br>
		 * }</code>
		 * 
		 * <pre>
		 * 'pinf'
		 * length
		 * info ref
		 * </pre>
		 */
		public static final String kDPackageInfo = "pinf";
	}
}
