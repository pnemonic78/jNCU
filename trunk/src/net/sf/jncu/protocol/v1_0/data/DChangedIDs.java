package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDChangedIDs</tt><br>
 * This command is sent in response to a <tt>kDGetChangedIDs</tt> command. It
 * returns all the ids with <tt>mod</tt> time > the last sync time. If the last
 * sync time is 0, no changed entries are returned (this would happen on the
 * first sync).
 * 
 * <pre>
 * 'cids'
 * length
 * count
 * array of ids for the soup
 * </pre>
 */
public class DChangedIDs extends DockCommandFromNewton {

	public static final String COMMAND = "cids";

	/**
	 * Creates a new command.
	 */
	public DChangedIDs() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
