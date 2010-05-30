/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io.win;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDSetFilter</tt><br>
 * This command changes the current filter being used. A
 * <tt>kDFilesAndFolders</tt> command is expected in return. The index is a long
 * indicating which item in the filters array sent from the desktop should be
 * used as the current filter. Index is 0-based. Windows only.
 * 
 * <pre>
 * 'sflt'
 * length
 * index
 * </pre>
 * 
 * @author moshew
 */
public class DSetFilter extends DockCommandFromNewton {

	public static final String COMMAND = "sflt";

	public DSetFilter() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
