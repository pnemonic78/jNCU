package net.sf.jncu.protocol.v1_0;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDDisconnect</tt><br>
 * This command is sent to the Newton when the docking operation is complete.
 * 
 * <pre>
 * 'disc'
 * length = 0
 * </pre>
 */
public class DCmdDisconnect extends DockCommandFromNewton {

	public static final String COMMAND = "disc";

	/**
	 * Creates a new command.
	 */
	public DCmdDisconnect() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.protocol.DockCommandFromNewton#decodeData(java.io.InputStream
	 * )
	 */
	@Override
	protected void decodeData(InputStream data) throws IOException {
	}

}
