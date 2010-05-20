/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.IDockCommandFromNewton;

/**
 * <tt>kDUnknownCommand</tt><br>
 * This command is sent when a message is received that is unknown. When the
 * desktop receives this command it can either install a protocol extension and
 * try again or return an error to the Newton. If the built-in Newton code
 * receives this command it always signals an error. The bad command parameter
 * is the 4 char command that wasn't recognised. The data is not returned.
 * 
 * <pre>
 * 'unkn'
 * length
 * bad command
 * </pre>
 * 
 * @author moshew
 */
public class DUnknownCommand extends DockCommandToNewton implements IDockCommandFromNewton {

	public static final String COMMAND = "unkn";

	private String badCommand;

	/**
	 *Creates a new command.
	 */
	public DUnknownCommand() {
		super(COMMAND);
	}

	/**
	 * Get the bad command.
	 * 
	 * @return the bad command.
	 */
	public String getBadCommand() {
		return badCommand;
	}

	/**
	 * Set the bad command.
	 * 
	 * @param badCommand
	 *            the bad command.
	 */
	public void setBadCommand(String badCommand) {
		this.badCommand = badCommand;
	}

	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		char[] cmdName = getBadCommand().toCharArray();
		data.write(cmdName[0] & 0xFF);
		data.write(cmdName[1] & 0xFF);
		data.write(cmdName[2] & 0xFF);
		data.write(cmdName[3] & 0xFF);
		return data;
	}

	@Override
	public void decode(InputStream frame) throws IOException {
		int length = DockCommandFromNewton.htonl(frame);
		setLength(length);
		if ((length != -1) && (frame.available() < length)) {
			throw new ArrayIndexOutOfBoundsException();
		}
		decodeData(frame);
	}

	/**
	 * Decode the command data.
	 * 
	 * @param data
	 *            the data.
	 * @throws IOException
	 *             if read past data buffer.
	 */
	protected void decodeData(InputStream data) throws IOException {
		char[] cmdName = new char[getLength()];
		cmdName[0] = (char) (data.read() & 0xFF);
		cmdName[1] = (char) (data.read() & 0xFF);
		cmdName[2] = (char) (data.read() & 0xFF);
		cmdName[3] = (char) (data.read() & 0xFF);

		setBadCommand(new String(cmdName));
	}

}
