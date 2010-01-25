package net.sf.jncu.protocol.v2_0;

/**
 * <h2>Newton Docking Protocol</h2> Newton Docking Protocol event commands for
 * 2.0 Newton ROM Extensions.<br>
 * Newton communicates with the desktop by exchanging Newton event commands.
 * <p>
 * In the commands below, all data is padded with nulls to 4 byte boundaries.
 * The length associated with each command is the length (in bytes) of the data
 * following the length field. All strings are C strings unless otherwise
 * specified.
 * <p>
 * All commands begin with the sequence -- '<tt>newt</tt>', '<tt>dock</tt>'. <br>
 * Newton communicates with the desktop by exchanging newton event commands. The
 * general command structure looks like this:
 * 
 * <pre>
 * 'newt'
 * 'dock'
 * 'aaaa'   // the specific command
 * length   // the length of the following command
 * data     // data, if any
 * </pre>
 * 
 * <h2>Compatibility</h2> The protocol described above for 2.0 is a superset of
 * the 1.0 protocol. However, the protocol version has been incremented so old
 * versions of Newton Connection will no longer work with this version. The
 * reason for this is that, although the protocol itself is upwardly compatible,
 * the data structures in other parts of the 2.0 newton have changed to such a
 * degree that old versions of Newton Connection will no longer work.
 * 
 * @author moshew
 */
public class DockingEventCommands {

}
