/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <tt>kDCursorMove</tt><br>
 * Moves the cursor forward <tt>count</tt> entries from its current position and
 * returns that entry. Returns nil if the cursor is moved past the last entry.
 * 
 * <pre>
 * 'move'
 * length
 * cursor id
 * count
 * </pre>
 * 
 * @author moshew
 */
public class DCursorMove extends DCursor {

	public static final String COMMAND = "move";

	private int count;

	/**
	 * Creates a new command.
	 */
	public DCursorMove() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.protocol.DockCommandToNewton#getCommandData()
	 */
	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		ntohl(getCount(), data);
	}

	/**
	 * Get the number of entries to move the cursor.
	 * 
	 * @return the count.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Set the number of entries to move the cursor.
	 * 
	 * @param count
	 *            the count.
	 */
	public void setCount(int count) {
		this.count = count;
	}

}
