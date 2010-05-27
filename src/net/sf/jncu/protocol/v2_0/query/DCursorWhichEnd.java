/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;


/**
 * <tt>kDCursorWhichEnd</tt><br>
 * Returns <tt>kDLongData</tt> with a <tt>0</tt> for unknown, <tt>1</tt> for
 * start and <tt>2</tt> for end.
 * 
 * <pre>
 * 'whch'
 * length
 * cursor id
 * </pre>
 * 
 * @author moshew
 */
public class DCursorWhichEnd extends DCursor {

	public static final String COMMAND = "whch";

	public static enum eCursorWhichEnd {
		/** Cursor is at unknown position. */
		UNKNOWN,
		/** Cursor is at start position. */
		START,
		/** Cursor is at end position. */
		END;
	}

	/**
	 * Creates a new command.
	 */
	public DCursorWhichEnd() {
		super(COMMAND);
	}

}
