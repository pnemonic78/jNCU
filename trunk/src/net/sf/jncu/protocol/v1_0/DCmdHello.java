package net.sf.jncu.protocol.v1_0;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDHello</tt><br>
 * This command is sent during long operations to let the Newton or desktop know
 * that the connection hasn't been dropped.
 * 
 * <pre>
 * 'helo'
 * length = 0
 * </pre>
 */
public class DCmdHello extends DockCommandFromNewton {

	public static final String COMMAND = "helo";

	/**
	 * Creates a new command.
	 */
	public DCmdHello() {
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
