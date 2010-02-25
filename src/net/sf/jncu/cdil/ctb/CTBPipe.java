package net.sf.jncu.cdil.ctb;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;

/**
 * Macintosh Communication Tool Serial pipe.
 * 
 * @author moshew
 */
public class CTBPipe extends CDPipe {

	private final String toolName;
	private final String configString;

	/**
	 * Creates a new CTB pipe.
	 * 
	 * @param layer
	 *            the owner layer.
	 * @param toolName
	 *            the name of the communication tool.
	 * @param configString
	 *            a tool-dependent configuration string.
	 */
	public CTBPipe(CDLayer layer, String toolName, String configString) {
		super(layer);
		this.toolName = toolName;
		this.configString = configString;
	}

	@Override
	protected OutputStream getOutput() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
