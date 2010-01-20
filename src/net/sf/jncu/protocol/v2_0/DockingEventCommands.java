package net.sf.jncu.protocol.v2_0;

/**
 * Newton Docking Protocol event commands for 2.0 Newton ROM Extensions.<br>
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
 * @author moshew
 */
public class DockingEventCommands {

}
