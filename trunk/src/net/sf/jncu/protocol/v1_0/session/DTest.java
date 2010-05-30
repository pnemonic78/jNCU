package net.sf.jncu.protocol.v1_0.session;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDTest</tt><br>
 * Test.
 * 
 * <pre>
 * 'test'
 * length
 * data
 * </pre>
 */
public class DTest extends DockCommandToNewton {

	public static final String COMMAND = "test";

	/**
	 * Creates a new command.
	 */
	public DTest() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}
}
