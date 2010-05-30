package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDAliasResolved</tt><br>
 * This command is sent by the desktop in response to the
 * <tt>kDResolveAlias</tt> command. If the value is 0, the alias can't be
 * resolved. If the data is <tt>1</tt> (or non-zero) the alias can be resolved.
 * 
 * <pre>
 * 'alir'
 * length
 * resolved
 * </pre>
 * 
 * @author moshew
 */
public class DAliasResolved extends DockCommandToNewton {

	public static final String COMMAND = "alir";

	/**
	 * Creates a new command.
	 */
	public DAliasResolved() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
