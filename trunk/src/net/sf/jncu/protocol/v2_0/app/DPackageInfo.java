package net.sf.jncu.protocol.v2_0.app;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDPackageInfo</tt><br>
 * This command is sent in response to a <tt>kDGetPackageInfo</tt> command. An
 * array is returned that contains a frame for each package with the specified
 * name (there may be more than one package with the same name but different
 * package id). The returned frame looks like this:<br>
 * <code>
 * {<br>
 * &nbsp;&nbsp;name: "The name passed in",<br>
 * &nbsp;&nbsp;packagesize: 123,<br>
 * &nbsp;&nbsp;packageid: 123,<br>
 * &nbsp;&nbsp;packageversion: 1,<br>
 * &nbsp;&nbsp;format: 1,<br>
 * &nbsp;&nbsp;devicekind: 1,<br>
 * &nbsp;&nbsp;devicenumber: 1,<br>
 * &nbsp;&nbsp;deviceid: 1,<br>
 * &nbsp;&nbsp;modtime: 123213213,<br>
 * &nbsp;&nbsp;iscopyprotected: true,<br>
 * &nbsp;&nbsp;length: 123,<br>
 * &nbsp;&nbsp;safetoremove: true<br>
 * }</code>
 * 
 * <pre>
 * 'pinf'
 * length
 * info ref
 * </pre>
 * 
 * @author Moshe
 */
public class DPackageInfo extends DockCommandFromNewton {

	public static final String COMMAND = "pinf";

	/**
	 * Creates a new command.
	 */
	public DPackageInfo() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
