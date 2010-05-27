/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;

/**
 * <tt>kDCursorPrev</tt><br>
 * Moves the cursor to the previous entry in the set of entries referenced by
 * the cursor and returns the entry. If the cursor is moved before the first
 * entry nil is returned.
 * 
 * <pre>
 * 'prev'
 * length
 * cursor id
 * </pre>
 * 
 * @author moshew
 */
public class DCursorPrev extends DCursor {

	public static final String COMMAND = "prev";

	/**
	 * Creates a new command.
	 */
	public DCursorPrev() {
		super(COMMAND);
	}

}
