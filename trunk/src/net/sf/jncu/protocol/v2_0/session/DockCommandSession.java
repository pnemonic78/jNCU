package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

/**
 * <h1>Session commands</h1>
 * <p>
 * <h2>Starting a session</h2>
 * <p>
 * 2.0 Newton supports a new set of protocols to enhance the connection
 * capabilities. However, since it's desirable to also support package
 * downloading from NPI, NTK 1.0 and Connection 2.0 the ROMs will also support
 * the old protocol for downloading packages. To make this work the 2.0 ROMs
 * will pretend that they are talking the old protocol when they send the
 * <tt>kDRequestToDock</tt> message. If a new connection (or other application)
 * is on the other end the protocol will be negotiated up to the current
 * version. Only package loading is supported with the old protocol.
 * <p>
 * No matter what the intent of the desktop or the Newton, these commands must
 * always start a session (the desktop can substitute a <tt>kDResult</tt> for
 * the <tt>kDSetTimeout</tt> if it doesn't want to set the timeout).
 * <p>
 * <h2>Examples</h2>
 * <p>
 * When a 2.0 ROM Newton is communicating with a 3.0 Connection the session
 * would start like this:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDRequestToDock</td>
 * </tr>
 * <tr>
 * <td>kDInitiateDocking</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDNewtonName</td>
 * </tr>
 * <tr>
 * <td>kDDesktopInfo</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDNewtonInfo</td>
 * </tr>
 * <tr>
 * <td>kDWhichIcons</td>
 * <td>-></td>
 * <td>// optional</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDSetTimeout</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDPassword</td>
 * </tr>
 * <tr>
 * <td>kDPassword</td>
 * <td>-></td>
 * </tr>
 * </table>
 * <br>
 * If the password sent from the Newton is wrong it would look like this
 * instead. The password exchange can occur up to 3 times before the desktop
 * gives up.
 * <table>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDPassword</td>
 * </tr>
 * <tr>
 * <td>kDPWWrong</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDPassword</td>
 * </tr>
 * <tr>
 * <td>kDPWWrong</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDPassword</td>
 * </tr>
 * <tr>
 * <td>kDPassword</td>
 * <td>-></td>
 * </tr>
 * </table>
 * <br>
 * If the password sent from the Desktop is wrong the Newton signals an error
 * immediately.
 * <table>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDPassword</td>
 * </tr>
 * <tr>
 * <td>kDPassword</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * </table>
 * <br>
 * If the desktop decides that the Newton has had enough guesses a kDResult can
 * be sent instead of a kDPWWrong. A kDBadPassword error should be specified. No
 * matter what the intent of the desktop or the Newton, these commands must
 * always start a session (the desktop can substitute a kDResult for the
 * kDSetTimeout if it doesn't want to set the timeout).
 * <p>
 * If no password has been specified, the key is returned unencrypted.
 * <p>
 * When a 2.0 ROM Newton is communicating with NPI 1.0, NTK 1.0 or 2.0
 * Connection the session would look like this:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDRequestToDock</td>
 * </tr>
 * <tr>
 * <td>kDInitiateDocking</td>
 * <td>-></td>
 * <td>// session type must be loadpackage</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDNewtonName</td>
 * </tr>
 * <tr>
 * <td>kDSetTimeout</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
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
 * <tr>
 * <td>kDDisconnect</td>
 * <td>-></td>
 * </tr>
 * </table>
 * <br>
 */
public class DockCommandSession extends DockingEventCommands {

	/** Desktop to Newton. */
	public static class DesktopToNewton extends DockingEventCommands.DesktopToNewton {
		/**
		 * Ask Newton to start docking process. <br>
		 * This command should be sent to the Newton in response to a
		 * kDRequestToDock command. Session type should be 4 to load a package.
		 * 
		 * <pre>
		 * 'dock'
		 * length = 4
		 * session type = 4
		 * </pre>
		 */
		public static final String kDInitiateDocking = "dock";
		/**
		 * This command is used to customise the set of icons shown on the
		 * Newton. The <tt>iconMask</tt> is a long that indicates which icons
		 * should be shown. For example, to show all icons you would use this:
		 * 
		 * <code>kBackupIcon + kSyncIcon + kInstallIcon + kRestoreIcon + kImportIcon + kKeyboardIcon</code>
		 * Where:
		 * <ul>
		 * <li>kBackupIcon = 1
		 * <li>kRestoreIcon = 2
		 * <li>kInstallIcon = 4
		 * <li>kImportIcon = 8
		 * <li>kSyncIcon = 16
		 * <li>kKeyboardIcon = 32
		 * </ul>
		 * 
		 * <pre>
		 * 'wicn'
		 * length
		 * iconMask
		 * </pre>
		 */
		public static final String kDWhichIcons = "wicn";
		/**
		 * This command displays the password slip to let the user enter a
		 * password. The string is displayed as the title of the slip. A
		 * <tt>kDPassword</tt> command is returned.
		 * 
		 * <pre>
		 * 'gpwd'
		 * length
		 * string ref
		 * </pre>
		 */
		public static final String kDGetPassword = "gpwd";
		/**
		 * This command is used to negotiate the real protocol version. The
		 * protocol version sent with the <tt>kDRequestToDock</tt> command is
		 * now fixed at version 9 (the version used by the 1.0 ROMs) so we can
		 * support package loading with NPI 1.0, Connection 2.0 and NTK 1.0.
		 * Connection 3.0 will send this command with the real protocol version
		 * it wants to use to talk to the Newton. The Newton will respond with a
		 * number equal to or lower than the number sent to it by the desktop.
		 * The desktop can then decide whether it can talk the specified
		 * protocol or not.
		 * <p>
		 * The desktop type is a long that identifies the sender‚ <tt>0</tt> for
		 * the Macintosh and <tt>1</tt> for Windows. The password key is used as
		 * part of the password verification.
		 * <p>
		 * Session type will be the real session type and should override what
		 * was sent in <tt>kDInitiateDocking</tt>. In fact, it will either be
		 * the same as was sent in <tt>kDInitiateDocking</tt> or "settingUp" to
		 * indicate that although the desktop has accepted a connection, the
		 * user has not yet specified an operation.
		 * <p>
		 * <tt>AllowSelectiveSync</tt> is a boolean. The desktop should say no
		 * when the user hasn't yet done a full sync and, therefore, can't do a
		 * selective sync.
		 * <p>
		 * <tt>desktopApps</tt> is an array of frames that describes who the
		 * Newton is talking with. Each frame in the array looks like this:
		 * <code>{name: "Newton Backup Utility", id: 1, version: 1}</code> There
		 * might be more than one item in the array if the Newton is connecting
		 * with a DIL application. The built-in Connection application expects 1
		 * item in the array that has id:
		 * <ul>
		 * <li>1: NBU
		 * <li>2: NCU
		 * </ul>
		 * It won't allow connection with any other id. NCK 2.0, NTK and NPI use
		 * old revisions of the protocol and aren't considered here.
		 * 
		 * <pre>
		 * 'dinf'
		 * length
		 * protocol version
		 * desktopType		// 0 for Mac, 1 for Windows
		 * encrypted key	// 2 longs
		 * session type
		 * allowSelectiveSync // 0 = no, 1 = yes
		 * desktopApps ref
		 * </pre>
		 * 
		 * @see #kNBU
		 * @see #kNCU
		 * @see #kMacintosh
		 * @see #kWindows
		 */
		public static final String kDDesktopInfo = "dinf";

		/** "Backup" icon. */
		public static final int kBackupIcon = 1;
		/** "Restore" icon. */
		public static final int kRestoreIcon = 2;
		/** "Install package" icon. */
		public static final int kInstallIcon = 4;
		/** "Import" icon. */
		public static final int kImportIcon = 8;
		/** "Synchronise" icon. */
		public static final int kSyncIcon = 16;
		/** "Keyboard" icon. */
		public static final int kKeyboardIcon = 32;

		/** Newton Backup Utility. */
		public static final int kNBU = 1;
		/** Newton Connection Utilities. */
		public static final int kNCU = 2;

		/** Apple Macintosh desktop type. */
		public static final int kMacintosh = 0;
		/** Microsoft Windows desktop type. */
		public static final int kWindows = 1;

		/** The protocol version. */
		public static final int kProtocolVersion = 9;
	}

	/** Newton to Desktop. */
	public static class NewtonToDesktop extends DockingEventCommands.NewtonToDesktop {
		/** Newton. */
		public static final String kDNewton = "newt";
		/**
		 * This command returns the key received in the
		 * <tt>kDInitiateDocking</tt> message encrypted using the password.
		 * 
		 * <pre>
		 * 'pass'
		 * length
		 * encrypted key
		 * </pre>
		 */
		public static final String kDPassword = "pass";
		/**
		 * This command is sent in response to a correct
		 * <tt>kDInitiateDocking</tt> command from the docker. The Newton's name
		 * is used to locate the proper synchronise file. The version info
		 * includes things like machine type (e.g. J1), ROM version, etc. Here's
		 * the full list of what the version info includes (all are
		 * <code>long</code>s):
		 * <ol>
		 * <li>length of version info in bytes
		 * <li>NewtonUniqueID - a number uniquely identifying the Newton
		 * <li>manufacturer id
		 * <li>machine type
		 * <li>ROM version
		 * <li>ROM stage
		 * <li>RAM size
		 * <li>screen height
		 * <li>screen width
		 * <li>system update version
		 * <li>Newton object system version
		 * <li>signature of internal store
		 * <li>vertical screen resolution
		 * <li>horizontal screen resolution
		 * <li>screen depth
		 * </ol>
		 * The version info is followed by the name of the Newton sent as a
		 * Unicode string including the terminating zeros at the end. The string
		 * is padded to an even 4 bytes by adding zeros as necessary (the
		 * padding bytes are not included in the length sent as part of the
		 * command header).
		 * 
		 * <pre>
		 * 'name'
		 * length
		 * version info
		 * name
		 * </pre>
		 */
		public static final String kDNewtonName = "name";
		/**
		 * This command is used to negotiate the real protocol version. See
		 * <tt>kDDesktopInfo</tt> below for more info. The password key is used
		 * as part of the password verification.
		 * 
		 * <pre>
		 * 'ninf'
		 * length
		 * protocol version
		 * encrypted key
		 * </pre>
		 */
		public static final String kDNewtonInfo = "ninf";
		/**
		 * This command is sent to the desktop after the connection is
		 * established using AppleTalk, serial, etc. (when the user taps the
		 * "connect" button). The protocol version is the version of the
		 * messaging protocol that's being used and should always be set to the
		 * number 9 for the version of the protocol defined here.
		 * 
		 * <pre>
		 * 'rtdk'
		 * length = 4
		 * protocol version = 9
		 * </pre>
		 */
		public static final String kDRequestToDock = "rtdk";
	}

	/** Command prefix. */
	public static final String kDNewtonDock = "newtdock";

}
