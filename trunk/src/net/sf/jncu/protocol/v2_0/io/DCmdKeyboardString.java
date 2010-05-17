package net.sf.jncu.protocol.v2_0.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
public class DCmdKeyboardString extends DockCommandToNewton {

	public static final String COMMAND = "kbds";

	private String s;

	/**
	 * Creates a new command.
	 */
	public DCmdKeyboardString() {
		super(COMMAND);
	}

	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		String s = getString();
		if (s != null) {
			byte[] utf16 = s.getBytes("UTF-16");
			// The 1st and 2nd bytes are UTF-16 header 0xFE and 0xFF.
			data.write(utf16, 2, utf16.length - 2);
		}
		// Null-terminated strings.
		data.write(0);
		data.write(0);
		return data;
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
