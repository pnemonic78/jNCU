package net.sf.jncu.protocol.v1_0;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDResult</tt><br>
 * This command is sent in response to any of the commands from the desktop that
 * don't request data. It lets the desktop know that things are still proceeding
 * OK.
 * 
 * <pre>
 * 'dres'
 * length
 * error code
 * </pre>
 * 
 * @author moshew
 */
public class DResult extends DockCommandFromNewton {

	public static final String COMMAND = "dres";

	private int errorCode;

	/**
	 * Creates a new command.
	 */
	public DResult() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.protocol.DockCommandFromNewton#decodeData(java.io.InputStream
	 * )
	 */
	@Override
	protected void decodeData(InputStream data) throws IOException {
		setErrorCode(htonl(data));
	}

	/**
	 * Get the error code.
	 * 
	 * @return the error code.
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * Set the error code.
	 * 
	 * @param errorCode
	 *            the error code.
	 */
	protected void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
