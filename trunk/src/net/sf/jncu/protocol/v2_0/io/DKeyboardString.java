package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDKeyboardString</tt><br>
 * This command sends a string of characters to the Newton for processing. The
 * characters are 2 byte Unicode characters. If there are an odd number of
 * characters the command should be padded, as usual.
 * 
 * <pre>
 * 'kbds'
 * length
 * "string"
 * </pre>
 * 
 * @author moshew
 */
public class DKeyboardString extends DockCommandToNewton {

	public static final String COMMAND = "kbds";

	private String s;

	/**
	 * Creates a new command.
	 */
	public DKeyboardString() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		writeString(getString(), data);
	}

	/**
	 * Get the string.
	 * 
	 * @return the character.
	 */
	public String getString() {
		return s;
	}

	/**
	 * Set the string.
	 * 
	 * @param s
	 *            the string.
	 */
	protected void setString(String s) {
		this.s = s;
	}

}
