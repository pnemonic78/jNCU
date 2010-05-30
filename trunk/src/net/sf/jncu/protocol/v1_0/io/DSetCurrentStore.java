/**
 * 
 */
package net.sf.jncu.protocol.v1_0.io;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSetCurrentStore</tt><br>
 * This command sets the current store on the Newton. A store frame is sent to
 * uniquely identify the store to be set: <br>
 * <code>
 * {<br>&nbsp;&nbsp;name: "foo",<br>
 * &nbsp;&nbsp;kind: "bar",<br>
 * &nbsp;&nbsp;signature: 1234,<br>
 * &nbsp;&nbsp;info: {&lt;info frame&gt;}		// This one is optional<br>
 * }</code>
 * 
 * <pre>
 * 'ssto'
 * length
 * store frame
 * </pre>
 * 
 * @author moshew
 */
public class DSetCurrentStore extends DockCommandToNewton {

	public static final String COMMAND = "ssto";

	/**
	 * Creates a new command.
	 */
	public DSetCurrentStore(String cmd) {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
