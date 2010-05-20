/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDRestoreFile</tt><br>
 * This command asks the desktop to restore the file specified by the last path
 * command and the filename. If the selected item is at the Desktop level, a
 * frame <code>{name: "Business", whichVol:-1}</code> is sent. Otherwise, a
 * string is sent.
 * 
 * <pre>
 * 'rsfl'
 * length
 * filename
 * </pre>
 * 
 * @author moshew
 */
public class DRestoreFile extends DockCommandFromNewton {

	public static final String COMMAND = "rsfl";

	/**
	 * Creates a new command.
	 */
	public DRestoreFile() {
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
