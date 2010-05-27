/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * Cursor command.
 * 
 * @author moshew
 */
public abstract class DCursor extends DockCommandToNewton {

	private int cursorId;

	/**
	 * Creates a new cursor command.
	 * 
	 * @param cmd
	 *            the command.
	 */
	public DCursor(String cmd) {
		super(cmd);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.protocol.DockCommandToNewton#getCommandData()
	 */
	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ntohl(getCursorId(), data);
		return data;
	}

	/**
	 * Get the cursor id.
	 * 
	 * @return the cursor id.
	 */
	public int getCursorId() {
		return cursorId;
	}

	/**
	 * Set the cursor id.
	 * 
	 * @param cursorId
	 *            the cursor id.
	 */
	public void setCursorId(int cursorId) {
		this.cursorId = cursorId;
	}

}
