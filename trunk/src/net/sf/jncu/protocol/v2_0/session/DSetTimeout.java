package net.sf.jncu.protocol.v2_0.session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSetTimeout</tt><br>
 * This command sets the timeout for the connection (the time the Newton will
 * wait to receive data for it disconnects). This time is usually set to 30
 * seconds.
 * 
 * <pre>
 * 'stim' 
 * length
 * timeout in seconds
 * </pre>
 * 
 * @author moshew
 */
public class DSetTimeout extends DockCommandToNewton {

	public static final String COMMAND = "stim";

	private int timeout = 30000;

	/**
	 * Creates a new command.
	 */
	public DSetTimeout() {
		super(COMMAND);
	}

	/**
	 * Get the timeout.
	 * 
	 * @return the timeout in seconds.
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Set the timeout.
	 * 
	 * @param timeout
	 *            the timeout in seconds.
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ntohl(getTimeout(), data);
		return data;
	}

}
