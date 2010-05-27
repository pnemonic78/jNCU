/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;


/**
 * <tt>kDCursorResetToEnd</tt><br>
 * Resets the cursor to the rightmost entry in the valid subset. A
 * <tt>kDRes</tt> of <tt>0</tt> is returned.
 * 
 * <pre>
 * 'rend'
 * length
 * cursor id
 * </pre>
 * 
 * @author moshew
 */
public class DCursorResetToEnd extends DCursor {

	public static final String COMMAND = "rend";

	/**
	 * Creates a new command.
	 */
	public DCursorResetToEnd() {
		super(COMMAND);
	}

}
