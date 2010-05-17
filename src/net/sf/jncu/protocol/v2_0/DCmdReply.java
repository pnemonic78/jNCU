package net.sf.jncu.protocol.v2_0;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v1_0.DResult;

/**
 * <tt>kdResult</tt><br>
 * This command is sent in response to any of the commands from the Newton that
 * don't request data. It lets the Newton know that things are still proceeding
 * OK.
 * 
 * <pre>
 * 'dres'
 * length
 * error code
 * </pre>
 * 
 * @see DResult
 * @author moshew
 */
public class DCmdReply extends DockCommandToNewton {

	public static final String COMMAND = DResult.COMMAND;

	private int errorCode;

	/**
	 * Creates a new command.
	 */
	public DCmdReply() {
		super(COMMAND);
	}

	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ntohl(getErrorCode(), data);
		return data;
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
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
