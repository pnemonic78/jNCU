package net.sf.jncu.protocol.v2_0.app;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDLoadPackageFile</tt><br>
 * This command asks the desktop to load the package specified by the last path
 * command and the filename string. If the selected item is at the Desktop
 * level, a frame <code>{Name: "Business", whichVol: -1}</code> is sent.
 * Otherwise, a string is sent.
 * 
 * <pre>
 * 'lpfl'
 * length
 * filename
 * </pre>
 * 
 * @author Moshe
 */
public class DLoadPackageFile extends DockCommandFromNewton {

	public static final String COMMAND = "lpfl";

	/**
	 * Creates a new command.
	 */
	public DLoadPackageFile() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
