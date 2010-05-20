/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDCreateDefaultSoup</tt><br>
 * This command creates a soup on the current store. It uses a registered
 * <tt>soupdef</tt> to create the soup meaning that the indexes, etc. are
 * determined by the ROM. If no <tt>soupdef</tt> exists for the specified soup
 * an error is returned. A <tt>kDResult</tt> is returned.
 * 
 * <pre>
 * 'cdsp'
 * length
 * soup name
 * </pre>
 * 
 * @author moshew
 */
public class DCreateDefaultSoup extends DockCommandToNewton {

	public static final String COMMAND = "cdsp";

	private String name;

	/**
	 * Creates a new command.
	 */
	public DCreateDefaultSoup(String cmd) {
		super(COMMAND);
	}

	/**
	 * Get the soup name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the soup name.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		writeString(getName(), data);
		return data;
	}

}
