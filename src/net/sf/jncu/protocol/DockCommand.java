package net.sf.jncu.protocol;

import net.sf.jncu.protocol.v2_0.session.DockCommandSession;

/**
 * Docking Command.
 * 
 * @author moshew
 */
public abstract class DockCommand {

	/** Number of bytes for a word. */
	protected static final int LENGTH_WORD = 4;
	/** Number of command characters. */
	protected static final int COMMAND_LENGTH = LENGTH_WORD;

	protected static final byte[] kDNewtonDockBytes = DockCommandSession.kDNewtonDock.getBytes();
	protected static final int kDNewtonDockLength = kDNewtonDockBytes.length;

	protected final String command;
	protected final byte[] commandBytes;
	private int length;

	/**
	 * Creates a new docking command.
	 * 
	 * @param command
	 *            the command.
	 */
	protected DockCommand(String command) {
		super();
		if (command.length() != COMMAND_LENGTH) {
			throw new IllegalArgumentException("command length must be " + COMMAND_LENGTH);
		}
		this.command = command;
		this.commandBytes = command.getBytes();
	}

	/**
	 * Creates a new docking command.
	 * 
	 * @param cmdBytes
	 *            the command.
	 */
	protected DockCommand(byte[] commandBytes) {
		super();
		if (commandBytes.length != COMMAND_LENGTH) {
			throw new IllegalArgumentException("command length must be " + COMMAND_LENGTH);
		}
		this.commandBytes = commandBytes;
		this.command = new String(commandBytes);
	}

	/**
	 * Get the command.
	 * 
	 * @return the command.
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Get the length.
	 * 
	 * @return the length. Default value is <tt>0</tt>.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Set the length.
	 * 
	 * @param length
	 *            the length.
	 */
	protected void setLength(int length) {
		this.length = length;
	}

}
