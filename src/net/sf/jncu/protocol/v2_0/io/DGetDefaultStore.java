package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDGetDefaultStore</tt><br>
 * This command returns info about the default store. This info is the same as
 * the info returned by the <tt>kDGetStoreNames</tt> command (see
 * <tt>kDStoreNames</tt> for details). The default store is the one used by
 * LoadPackage.
 * 
 * <pre>
 * 'gdfs'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetDefaultStore extends DockCommandToNewton {

	public static final String COMMAND = "gdfs";

	/**
	 * Creates a new command.
	 */
	public DGetDefaultStore() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
