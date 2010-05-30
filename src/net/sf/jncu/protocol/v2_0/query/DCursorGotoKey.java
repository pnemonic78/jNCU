/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <tt>kDCursorGotoKey</tt><br>
 * The entry at the specified key location is returned. <tt>Nil</tt> is returned
 * if there is no entry with the specified key.
 * 
 * <pre>
 * 'goto'
 * length
 * cursor id
 * key
 * </pre>
 * 
 * @author moshew
 */
public class DCursorGotoKey extends DCursor {

	public static final String COMMAND = "goto";

	private int key;

	/**
	 * Creates a new command.
	 */
	public DCursorGotoKey() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.protocol.DockCommandToNewton#getCommandData()
	 */
	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		ntohl(getKey(), data);
	}

	/**
	 * Get the key location.
	 * 
	 * @return the key.
	 */
	public int getKey() {
		return key;
	}

	/**
	 * Set the key location.
	 * 
	 * @param key
	 *            the key.
	 */
	public void setKey(int key) {
		this.key = key;
	}

}
