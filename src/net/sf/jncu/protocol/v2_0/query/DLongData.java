/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDLongData</tt><br>
 * Newton returns a long value. The interpretation of the data depends on the
 * command which prompted the return of the long value.
 * 
 * <pre>
 * 'ldta'
 * length
 * data
 * </pre>
 * 
 * @author moshew
 */
public class DLongData extends DockCommandFromNewton {

	public static final String COMMAND = "ldta";

	private int data;

	/**
	 * Creates a new command.
	 */
	public DLongData() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		setData(htonl(data));
	}

	/**
	 * Get the data.
	 * 
	 * @return the data.
	 */
	public int getData() {
		return data;
	}

	/**
	 * Set the data.
	 * 
	 * @param data
	 *            the data.
	 */
	public void setData(int data) {
		this.data = data;
	}

}
