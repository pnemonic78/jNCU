/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;

/**
 * <tt>kDCursorReset</tt><br>
 * Resets the cursor to its initial state. A <tt>kDRes</tt> of <tt>0</tt> is
 * returned.
 * 
 * <pre>
 * 'rset'
 * length
 * cursor id
 * </pre>
 * 
 * @author moshew
 */
public class DCursorReset extends DCursor {

	public static final String COMMAND = "rset";

	/**
	 * Creates a new command.
	 */
	public DCursorReset() {
		super(COMMAND);
	}

}
