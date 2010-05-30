/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDRequestToBrowse</tt><br>
 * This command is sent to a desktop that the Newton wishes to browse files on.
 * File types can be 'import, 'packages, 'syncFiles' or an array of strings to
 * use for filtering.
 * 
 * <pre>
 * 'rtbr'
 * length
 * file types
 * </pre>
 * 
 * @author moshew
 */
public class DRequestToBrowse extends DockCommandFromNewton {

	public static final String COMMAND = "rtbr";

	public DRequestToBrowse() {
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
		// TODO implement me!
	}

}
