package net.sf.jncu.protocol.v2_0.keyboard;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

/**
 * Desktop initiated keyboard passthrough would look like this:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td>kDStartKeyboardPassthrough</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDStartKeyboardPassthrough</td>
 * </tr>
 * <tr>
 * <td>kDKeyboardString</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td>kDKeyboardString</td>
 * <td>-></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>kDOperationCanceled</td>
 * <td>-></td>
 * <td>( from either side)</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDOpCanceledAck</td>
 * </tr>
 * </table>
 * <br>
 * Newton initiated keyboard passthrough would look like this:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDStartKeyboardPassthrough</td>
 * </tr>
 * <tr>
 * <td>kDKeyboardString</td>
 * <td>-></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>kDKeyboardString</td>
 * <td>-></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDOperationCanceled</td>
 * </tr>
 * <tr>
 * <td>kDOpCanceledAck</td>
 * <td>-></td>
 * <td></td>
 * </tr>
 * </table>
 */
public class DockCommandKeyboard extends DockingEventCommands {

	public static final class DesktopToNewton {
		/**
		 * This command sends 1 character to the Newton for processing. The char
		 * is a 2 byte unicode character + a 2 byte state. The state is defined
		 * as follows:
		 * <ol>
		 * <li>Bit 1 = command key down
		 * </ol>
		 * 
		 * <pre>
		 * 'kbdc'
		 * length
		 * char
		 * state
		 * </pre>
		 */
		public static final String kDKeyboardChar = "kbdc";
		/**
		 * This command sends a string of characters to the Newton for
		 * processing. The characters are 2 byte unicode characters. If there
		 * are an odd number of characters the command should be padded, as
		 * usual.
		 * 
		 * <pre>
		 * 'kbds'
		 * length
		 * "string"
		 * </pre>
		 */
		public static final String kDKeyboardString = "kbds";
	}

	/**
	 * This command is sent to enter keyboard passthrough mode. It can be
	 * followed by kDKeyboardChar, kDKeyboardString, kDHello and
	 * kDOperationComplete commands.
	 * 
	 * <pre>
	 * 'kybd'
	 * length
	 * </pre>
	 */
	public static final String kDStartKeyboardPassthrough = "kybd";

}
