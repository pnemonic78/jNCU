/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * <tt>kDGetFilesAndFolders</tt><br>
 * This command requests that the desktop system return the files and folders
 * necessary to open a standard file like dialog.
 * 
 * <pre>
 * 'gfil'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetFilesAndFolders extends DockCommandFromNewtonBlank {

	public static final String COMMAND = "gfil";

	public DGetFilesAndFolders() {
		super(COMMAND);
	}

}
