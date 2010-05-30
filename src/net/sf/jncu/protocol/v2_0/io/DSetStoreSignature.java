package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSetStoreSignature</tt><br>
 * This commands sets the signature of the current store to the specified value.
 * A <tt>kDResult</tt> with value <tt>0</tt> (or the error value if an error
 * occurred) is sent to the desktop in response.
 * 
 * <pre>
 * 'ssig'
 * length
 * new signature
 * </pre>
 * 
 * @author moshew
 */
public class DSetStoreSignature extends DockCommandToNewton {

	public static final String COMMAND = "ssig";

	/**
	 * Creates a new command.
	 */
	public DSetStoreSignature() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
