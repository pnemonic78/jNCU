/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDCallResult</tt><br>
 * This command is sent in response to a <tt>kDCallGlobalfunction</tt> or
 * <tt>kDCallRootMethod</tt> command. The ref is the return value from the
 * function or method called.
 * 
 * <pre>
 * 'cres'
 * length
 * ref
 * </pre>
 * 
 * @author moshew
 */
public class DCallResult extends DockCommandFromNewton {

	public static final String COMMAND = "cres";

	/**
	 * Creates a new command.
	 */
	public DCallResult() {
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
		// TODO Auto-generated method stub
	}

}
