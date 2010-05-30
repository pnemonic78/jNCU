/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDSetPath</tt><br>
 * This command tells the desktop that the user has changed the path. The
 * desktop responds with a new list of files and folders. The path is sent as an
 * array of strings rather than an array of frames as all of the other commands
 * are for performance reasons. For the Macintosh, the array would be something
 * like: <code>["Desktop",{Name:"My hard disk", whichVol:0}, "Business"]</code>
 * to set the path to "<tt>My hard disk:business:</tt>". " <tt>Desktop</tt>"
 * will always be at the start of the list, since that's the way Standard File
 * works. So if the user wanted to set the path to somewhere in the Desktop
 * Folder he would send something like
 * <code>["Desktop",{Name:"Business", whichVol:-1}]</code> to set the path to "
 * <tt>My hard disk:Desktop Folder:business:</tt>"
 * <p>
 * The second item in the array, will always be a frame instead of a string and
 * will contain an additional slot "<tt>whichvol</tt>" to indicate to the
 * desktop whether that item is a name of a volume or a folder in the Desktop
 * Folder and if so it's <tt>volRefNum</tt>.
 * <p>
 * For Windows the array would be something like: <code>["c:\", "business"]</code> to set
 * the path to "<tt>c:\business</tt>".
 * 
 * <pre>
 * 'spth'
 * length
 * array of strings
 * </pre>
 * 
 * @author moshew
 */
public class DSetPath extends DockCommandFromNewton {

	public static final String COMMAND = "spth";

	public DSetPath() {
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
