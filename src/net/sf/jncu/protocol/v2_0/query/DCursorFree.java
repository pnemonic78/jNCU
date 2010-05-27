/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;


/**
 * <tt>kDCursorFree</tt><br>
 * Disposes the cursor and returns a <tt>kDRes</tt> with a <tt>0</tt> or error.
 * 
 * <pre>
 * 'cfre'
 * length
 * cursor id
 * </pre>
 * 
 * @author moshew
 */
public class DCursorFree extends DCursor {

	public static final String COMMAND = "cfre";

	/**
	 * Creates a new command.
	 */
	public DCursorFree() {
		super(COMMAND);
	}

}
