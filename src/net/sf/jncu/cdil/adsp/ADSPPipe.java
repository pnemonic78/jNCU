package net.sf.jncu.cdil.adsp;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;

/**
 * AppleTalk pipe.
 * 
 * @author moshew
 */
public class ADSPPipe extends CDPipe {

	private final String name;
	private final byte type;

	/**
	 * Creates a new ADSP pipe.
	 * 
	 * @param layer
	 *            the owner layer.
	 * @param name
	 *            the name of the ADSP connection. This string is what appears
	 *            in the Chooser list on the Newton OS device. If you pass
	 *            <tt>NULL</tt> for this parameter, the CDIL uses a default name
	 *            based on your desktop computer's preferences (for instance, on
	 *            a Macintosh, it will use the strings specified in the File
	 *            Sharing control panel).
	 * @param type
	 *            the connection type. This is searched for by the Chooser on
	 *            the Newton OS device. If you pass <tt>NULL</tt> for this
	 *            parameter, the CDIL uses the type specified by the
	 *            Connection/Dock application.
	 */
	public ADSPPipe(CDLayer layer, String name, byte type) {
		super(layer);
		this.name = name;
		this.type = type;
	}

	@Override
	protected OutputStream getOutput() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
