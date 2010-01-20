package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

/**
 * Examples: When a 2.0 ROM Newton is communicating with a 3.0 Connection the
 * session would start like this:
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
 * kDSetTimeout if it doesn't want to set the timeout). If no password has been
 * specied, the key is returned unencrypted. When a 2.0 ROM Newton is
 * communicating with NPI 1.0, NTK 1.0 or 2.0 Connection the session would look
 * like this:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td></td>
 * <td><- kDRequestToDock</td>
 * </tr>
 * <tr>
 * <td>kDInitiateDocking -></td>
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

	public static final class DesktopToNewton {
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
		/** Define which icons are shown. */
		public static final String kDWhichIcons = "wicn";
		/**
		 * This command is sent to the desktop after the connection is
		 * established using AppleTalk, serial, etc. (when the user taps the
		 * connect button). The protocol version is the version of the messaging
		 * protocol that's being used and should always be set to the number 9
		 * for the version of the protocol defined here.
		 * 
		 * <pre>
		 * 'rtdk'
		 * length = 4
		 * protocol version = 9
		 * </pre>
		 */
		public static final String kDRequestToDock = "rtdk";
	}

	public static final class NewtonToDesktop {
		/**
		 * <pre>
		 * 'pass'
		 * data = encrypted key
		 * </pre>
		 */
		public static final String kDPassword = "pass";
		/**
		 * <pre>
		 * 'name'
		 * length
		 * version info
		 * name
		 * </pre>
		 */
		public static final String kDNewtonName = "name";
		public static final String kDNewtonInfo = "ninf";
	}

	public static final String kDNewton = "newt";

}
