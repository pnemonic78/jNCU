package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDPath</tt><br>
 * This command returns the initial strings for the folder pop-up in the
 * Macintosh version of the window and for the directories list in the Windows
 * version. It is also returned after the user taps on a folder alias. In this
 * case the path must be changed to reflect the new location. Each element of
 * the array is a frame that takes this form:<br>
 * <code>{<br>
 * &nbsp;&nbsp;name: "my hard disk",<br>
 * &nbsp;&nbsp;type: disk,<br>
 * &nbsp;&nbsp;disktype: harddrive,<br>
 * &nbsp;&nbsp;whichVol: 0,			// Optional - see below<br>
 * }</code><br>
 * <p>
 * The possible values for type are (desktop = 0, file = 1, folder = 2, disk =
 * 3). If the type is disk, there is an additional slot <tt>disktype</tt> with
 * the values (floppy = 0, hardDrive = 1, cdRom = 2, netDrive = 3). Finally, for
 * the second frame in the array i.e. the one after Desktop, there will be an
 * additional slot <tt>whichvol</tt> , which will be a <tt>0</tt> if the item is
 * disk or a <tt>volRefNum</tt> if the item is a folder on the desktop.
 * <p>
 * For example, the Macintosh might send:<br>
 * <code>[{name: "desktop", type: desktop}, {name: "my hard disk", type: disk, disktype: harddrive, whichvol: 0}, {name: "business", type: folder}]</code>
 * <br>
 * or for some folder on the desktop it it might send:<br>
 * <code>[{name: "desktop", type: desktop}, {name: "business", type: folder, whichvol: -1}, {name: "my folder", type: folder}]</code>
 * <p>
 * For Windows it might be: [{name: "c:\", type: 'folder}, {name: "business",
 * type: 'folder}]
 * 
 * <pre>
 * 'path'
 * length
 * folder array
 * </pre>
 * 
 * @author moshew
 */
public class DPath extends DockCommandToNewton {

	public static final String COMMAND = "path";

	/**
	 * Creates a new command.
	 */
	public DPath() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
