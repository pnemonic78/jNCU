/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;


/**
 * <tt>kDCursorCountEntries</tt><br>
 * Returns the count of the entries matching the query specification. A
 * <tt>kDLongData</tt> is returned.
 * 
 * <pre>
 * 'cnt '
 * length
 * cursor id
 * </pre>
 * 
 * @author moshew
 */
public class DCursorCountEntries extends DCursor {

	public static final String COMMAND = "cnt ";

	/**
	 * Creates a new command.
	 */
	public DCursorCountEntries() {
		super(COMMAND);
	}

}
