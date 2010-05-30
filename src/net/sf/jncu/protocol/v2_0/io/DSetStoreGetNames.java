package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSetStoreGetNames</tt><br>
 * This command is the same as <tt>kDSetCurrentStore</tt> except that it returns
 * the names of the soups on the stores as if you'd send a
 * <tt>kDGetSoupNames</tt> command. It sets the current store on the Newton. A
 * store frame is sent to uniquely identify the store to be set: <br>
 * <code>{<br>
 * &nbsp;&nbsp;name: "foo",<br>
 * &nbsp;&nbsp;kind: "bar",<br>
 * &nbsp;&nbsp;signature: 1234,<br>
 * &nbsp;&nbsp;info: {&lt;info frame&gt;}		// This one is optional<br>
 * }</code>
 * <br>
 * A <tt>kDSoupNames</tt> is sent by the Newton in response.
 * 
 * <pre>
 * 'ssgn'
 * length
 * store frame
 * </pre>
 * 
 * @author moshew
 */
public class DSetStoreGetNames extends DockCommandToNewton {

	public static final String COMMAND = "ssgn";

	/**
	 * Creates a new command.
	 */
	public DSetStoreGetNames() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
