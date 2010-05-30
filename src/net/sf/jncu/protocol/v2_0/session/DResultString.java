/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDResultString</tt><br>
 * Reports a desktop error to the Newton. The string is included since the
 * Newton doesn't know how to decode all the desktop errors (especially since
 * the Macintosh and Windows errors are different). <tt>ErrorString</tt> is a
 * ref.
 * 
 * <pre>
 * 'ress'
 * length
 * errorNumber
 * errorStringRef
 * </pre>
 * 
 * @author moshew
 */
public class DResultString extends DockCommandToNewton {

	public static final String COMMAND = "ress";

	private int errorNumber;
	private String errorString;

	/**
	 * Creates a new command.
	 */
	public DResultString() {
		super(COMMAND);
	}

	/**
	 * Get the error number.
	 * 
	 * @return the error.
	 */
	public int getErrorNumber() {
		return errorNumber;
	}

	/**
	 * Set the error number.
	 * 
	 * @param errorNumber
	 *            the error.
	 */
	public void setErrorNumber(int errorNumber) {
		this.errorNumber = errorNumber;
	}

	/**
	 * Get the error string.
	 * 
	 * @return the error.
	 */
	public String getErrorString() {
		return errorString;
	}

	/**
	 * Set the error string.
	 * 
	 * @param errorString
	 *            the error.
	 */
	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		ntohl(getErrorNumber(), data);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(new NSOFString(getErrorString()), data);
	}
}
