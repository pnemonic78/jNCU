/**
 * 
 */
package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDCreateSoup</tt><br>
 * Create a soup.
 * 
 * <pre>
 * 'csop'
 * length
 * soup name
 * index description
 * </pre>
 * 
 * @author moshew
 */
public class DCreateSoup extends DockCommandToNewton {

	public static final String COMMAND = "csop";

	private String name;

	/**
	 * Creates a new command.
	 */
	public DCreateSoup(String cmd) {
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
	protected void writeCommandData(OutputStream data) throws IOException {
		writeString(getName(), data);
	}

}
