/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.newton.stream.NSOFDecoder;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDGetFileInfo</tt><br>
 * This command asks the desktop to return info about the specified file. See
 * <tt>kDFileInfo</tt> for info about what's returned.
 * <p>
 * If the selected item is at the Desktop level, a frame
 * <code>{Name:"Business", whichVol:-1}</code> will be sent instead of the
 * string to indicate the <tt>volRefNum</tt> for the file.
 * 
 * <pre>
 * 'gfin'
 * length
 * filename string
 * </pre>
 * 
 * @author moshew
 */
public class DGetFileInfo extends DockCommandFromNewton {

	public static final String COMMAND = "gfin";

	private NSOFObject filename;

	public DGetFileInfo() {
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
		NSOFDecoder decoder = new NSOFDecoder();
		setFilename(decoder.decode(data));
	}

	/**
	 * Get the file name.
	 * 
	 * @return the file name.
	 */
	public NSOFObject getFilename() {
		return filename;
	}

	/**
	 * Set the file name.
	 * 
	 * @param filename
	 *            the file name.
	 */
	protected void setFilename(NSOFObject filename) {
		this.filename = filename;
	}

}
