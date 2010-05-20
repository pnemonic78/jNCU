/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDRemoveProtocolExtension</tt><br>
 * This command removes a previously installed protocol extension.
 * 
 * <pre>
 * 'prex'
 * length
 * command
 * </pre>
 * 
 * @author moshew
 */
public class DRemoveProtocolExtension extends DockCommandToNewton {

	public static final String COMMAND = "rpex";

	private String extension;

	/**
	 * Creates a new command.
	 */
	public DRemoveProtocolExtension() {
		super(COMMAND);
	}

	/**
	 * Get the extension command.
	 * 
	 * @return the command.
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Set the extension command.
	 * 
	 * @param extension
	 *            the command.
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		char[] cmdName = getExtension().toCharArray();
		data.write(cmdName[0] & 0xFF);
		data.write(cmdName[1] & 0xFF);
		data.write(cmdName[2] & 0xFF);
		data.write(cmdName[3] & 0xFF);
		return data;
	}
}
