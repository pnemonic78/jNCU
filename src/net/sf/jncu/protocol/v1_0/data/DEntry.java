package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDEntry</tt><br>
 * This command is sent in response to a <tt>kDReturnEntry</tt> command. The
 * entry in the current soup specified by the ID in the <tt>kDReturnEntry</tt>
 * command is returned.
 * 
 * <pre>
 * 'entr'
 * length
 * entry  // binary data
 * </pre>
 */
public class DEntry extends DockCommandFromNewton {

	public static final String COMMAND = "entr";

	/**
	 * Creates a new command.
	 */
	public DEntry() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
