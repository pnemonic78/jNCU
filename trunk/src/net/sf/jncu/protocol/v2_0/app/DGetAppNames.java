package net.sf.jncu.protocol.v2_0.app;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDGetAppNames</tt><br>
 * This command asks the Newton to send information about the applications
 * installed on the Newton. See the <tt>kDAppNames</tt> description above for
 * details of the information returned. The <tt>return what</tt> parameter
 * determines what information is returned. Here are the choices:
 * <ul>
 * <li>0: return names and soups for all stores
 * <li>1: return names and soups for current store
 * <li>2: return just names for all stores
 * <li>3: return just names for current store
 * </ul>
 * 
 * <pre>
 * 'gapp'
 * length
 * return what
 * </pre>
 * 
 * @author Moshe
 */
public class DGetAppNames extends DockCommandToNewton {

	public static final String COMMAND = "gapp";

	/** Return names and soups for all stores. */
	public static final int NAMES_AND_SOUPS_ALL = 0;
	/** Return names and soups for current store. */
	public static final int NAMES_AND_SOUPS_CURRENT = 1;
	/** Return just names for all stores. */
	public static final int NAMES_ALL = 2;
	/** Return just names for current store. */
	public static final int NAMES_CURRENT = 3;

	private int what = NAMES_AND_SOUPS_ALL;

	/**
	 * Constructs a new command.
	 */
	public DGetAppNames() {
		super(COMMAND);
	}

	/**
	 * Set what to return.
	 * 
	 * @param what
	 *            return what?
	 */
	public void setWhat(int what) {
		this.what = what;
	}

	/**
	 * Get what to return.
	 * 
	 * @return what?
	 */
	public int getWhat() {
		return what;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		ntohl(getWhat(), data);
	}
}
