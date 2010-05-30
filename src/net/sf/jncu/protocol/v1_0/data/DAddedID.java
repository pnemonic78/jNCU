package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDAddedID</tt><br>
 * This command is sent in response to a <tt>kDAddEntry</tt> command from the
 * desktop. It returns the ID that the entry was given when it was added to the
 * current soup.
 * 
 * <pre>
 * 'adid'
 * length
 * id
 * </pre>
 */
public class DAddedID extends DockCommandFromNewton {

	public static final String COMMAND = "adid";

	/**
	 * Creates a new command.
	 */
	public DAddedID() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
