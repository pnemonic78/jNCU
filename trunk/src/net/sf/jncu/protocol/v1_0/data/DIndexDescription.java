package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDIndexDescription</tt><br>
 * This command specifies the indexes that should be created for the current
 * soup.
 * 
 * <pre>
 * 'didx'
 * length
 * indexes
 * </pre>
 */
public class DIndexDescription extends DockCommandFromNewton {

	public static final String COMMAND = "indx";

	/**
	 * Creates a new command.
	 */
	public DIndexDescription() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
