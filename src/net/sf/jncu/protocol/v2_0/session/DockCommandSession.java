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
	}

	/** Command prefix. */
	public static final String kDNewtonDock = "newtdock";

}
