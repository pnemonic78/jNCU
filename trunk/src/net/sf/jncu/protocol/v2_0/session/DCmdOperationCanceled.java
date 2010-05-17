package net.sf.jncu.protocol.v2_0.session;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDOperationCanceled</tt><br>
 * This command is sent when the user cancels an operation. Usually no action is
 * required on the receivers part except to return to the "ready" state.
 * 
 * <pre>
 * 'opcn'
 * length = 0
 * </pre>
 */
public class DCmdOperationCanceled extends DockCommandFromNewton {

	public static final String COMMAND = "opcn";

	/**
	 * Creates a new command.
	 */
	public DCmdOperationCanceled() {
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
