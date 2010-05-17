package net.sf.jncu.protocol.v2_0.session;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDOpCanceledAck</tt><br>
 * This command is sent in response to a <tt>kDOperationCanceled</tt>.
 * 
 * <pre>
 * 'ocaa'
 * length = 0
 * </pre>
 */
public class DCmdOperationCanceledAck extends DockCommandFromNewton {

	public static final String COMMAND = "ocaa";

	/**
	 * Creates a new command.
	 */
	public DCmdOperationCanceledAck() {
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
