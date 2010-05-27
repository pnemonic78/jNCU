/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;

/**
 * <tt>kDCursorEntry</tt><br>
 * Returns the entry at the current cursor.
 * 
 * <pre>
 * 'crsr'
 * length
 * cursor id
 * </pre>
 * 
 * @author moshew
 */
public class DCursorEntry extends DCursor {

	public static final String COMMAND = "crsr";

	/**
	 * Creates a new command.
	 */
	public DCursorEntry() {
		super(COMMAND);
	}

}
