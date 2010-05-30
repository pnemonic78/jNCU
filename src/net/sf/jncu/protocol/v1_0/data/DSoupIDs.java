package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDSoupIDs</tt><br>
 * This command is sent in response to a <tt>kDGetSoupIDs</tt> command. It
 * returns all the IDs from the current soup.
 * 
 * <pre>
 * 'sids'
 * length
 * count
 * array of ids for the soup
 * </pre>
 */
public class DSoupIDs extends DockCommandFromNewton {

	public static final String COMMAND = "sids";

	/**
	 * Creates a new command.
	 */
	public DSoupIDs() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
