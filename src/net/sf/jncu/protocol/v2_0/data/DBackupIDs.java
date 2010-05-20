/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDBackupIDs</tt><br>
 * This command is sent in response to a <tt>kDBackupSoup</tt> command (see that
 * command for command sequence details). The length for this command is always
 * set to <tt>-1</tt> indicating that the length is unknown. The ids are
 * specified as a compressed array of 16 bit numbers. Each id should be offset
 * by any value specified by a previous <tt>kDSetBaseID</tt> command (this is
 * how we can specify a 32 bit value in 15 bits). Each id is a number between
 * <tt>0</tt> and <tt>0x7FFF (32767)</tt>. Negative numbers specify a count of
 * the number of entries above the previous number before the next break
 * (non-contiguous id). The sequence is ended by a <tt>0x8000</tt> word. So, if
 * the Newton contains ids <tt>0, 1, 2, 3, 4, 10, 20, 21, 30, 31, 32</tt> the
 * array would look like <tt>0, -4, 10, 20, -1, 30, -2, 0x8000</tt><br>
 * Thus we send 8 words instead of 11 longs. Is it worth it? If there are a lot
 * of entries it should be.
 * 
 * <pre>
 * 'bids'
 * length = -1
 * ids
 * </pre>
 * 
 * @author moshew
 */
public class DBackupIDs extends DockCommandFromNewton {

	public static final String COMMAND = "bids";

	/**
	 *Creates a new command.
	 */
	public DBackupIDs(String cmd) {
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
