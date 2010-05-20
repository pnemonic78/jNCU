/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSetSoupGetInfo</tt><br>
 * This command is like a combination of <tt>kDSetCurrentSoup</tt> and
 * <tt>kDGetChangedInfo</tt>. It sets the current soup -- see
 * <tt>kDSetCurrentSoup</tt> for details. A <tt>kDSoupInfo</tt> or
 * <tt>kDRes</tt> command is sent by the Newton in response.
 * 
 * <pre>
 * 'ssgi'
 * length
 * soup name
 * </pre>
 * 
 * @author moshew
 */
public class DSetSoupGetInfo extends DockCommandToNewton {

	public static final String COMMAND = "ssgi";

	private String name;

	/**
	 * Creates a new command.
	 */
	public DSetSoupGetInfo() {
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
