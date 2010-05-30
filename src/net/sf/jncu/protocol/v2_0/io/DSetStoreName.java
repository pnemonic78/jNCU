package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSetStoreName</tt><br>
 * This command requests that the name of the current store be set to the
 * specified name.
 * 
 * <pre>
 * 'ssna'
 * length
 * name ref
 * </pre>
 * 
 * @author moshew
 */
public class DSetStoreName extends DockCommandToNewton {

	public static final String COMMAND = "ssna";

	/**
	 * Creates a new command.
	 */
	public DSetStoreName() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
