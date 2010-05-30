package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDFileInfo</tt><br>
 * This command is sent in response to a <tt>kDGetFileInfo</tt> command. It
 * returns a frame that looks like this:<br>
 * <code>{<br>
 * &nbsp;&nbsp;kind: "Microsoft Word document",<br>
 * &nbsp;&nbsp;size: 20480,<br>
 * &nbsp;&nbsp;created: 3921837,<br>
 * &nbsp;&nbsp;modified: 3434923,<br>
 * &nbsp;&nbsp;icon: &lt;binary object of icon&gt;,<br>
 * &nbsp;&nbsp;path: "hd:files:another folder:"<br>
 * }</code>
 * <p>
 * <tt>kind</tt> is a description of the file.<br>
 * <tt>size</tt> is the number of bytes (actual, not the amount used on the
 * disk).<br>
 * <tt>create</tt> is the creation date in Newton date format.<br>
 * <tt>modified</tt> is the modification date of the file.<br>
 * <tt>icon</tt> is an icon to display. This is optional.<br>
 * <tt>path</tt> is the "user understandable" path description<br>
 * 
 * <pre>
 * 'finf'
 * length
 * frame of info
 * </pre>
 * 
 * @author moshew
 */
public class DFileInfo extends DockCommandToNewton {

	public static final String COMMAND = "finf";

	/**
	 * Creates a new command.
	 */
	public DFileInfo() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
