/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDQuery</tt><br>
 * The parameter frame must contain a <tt>queryspec</tt> slot and may contain a
 * <tt>soupname</tt> slot. Performs the specified query on the current store.
 * The <tt>queryspec</tt> is a full <tt>queryspec</tt> including valid test,
 * etc. functions. Soup name is a string that's used to find a soup in the
 * current store to query. If the soup name is an empty string or a
 * <tt>NILREF</tt> the query is done on the current soup. A <tt>kDLongData</tt>
 * is returned with a cursor ID that should be used with the rest of the remote
 * query commands.
 * 
 * <pre>
 * 'qury'
 * length
 * parameter frame
 * </pre>
 * 
 * @author moshew
 */
public class DQuery extends DockCommandToNewton {

	public static final String COMMAND = "qury";

	/**
	 * Creates a new command.
	 */
	public DQuery() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.protocol.DockCommandToNewton#getCommandData()
	 */
	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
	}

}
