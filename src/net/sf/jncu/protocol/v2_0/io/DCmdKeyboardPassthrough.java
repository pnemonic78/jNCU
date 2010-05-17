package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDStartKeyboardPassthrough</tt><br>
 * This command is sent to enter keyboard pass-through mode. It can be followed
 * by <tt>kDKeyboardChar</tt>, <tt>kDKeyboardString</tt>, <tt>kDHello</tt> and
 * <tt>kDOperationComplete</tt> commands.
 * 
 * <pre>
 * 'kybd'
 * length
 * </pre>
 * 
 * @author moshew
 */
public class DCmdKeyboardPassthrough extends DockCommandFromNewton {

	public static final String COMMAND = "kybd";

	/**
	 * Creates a new command.
	 */
	public DCmdKeyboardPassthrough() {
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
