package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

/**
 * <h1>Keyboard commands</h1>
 * <p>
 * Desktop initiated keyboard pass-through would look like this:
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
 * Newton initiated keyboard pass-through would look like this:
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

}
