package net.sf.jncu.protocol;

/**
 * Docking Command.
 * 
 * @author moshew
 */
public abstract class DockCommand implements IDockCommand {

	/** Number of bytes for a word. */
	protected static final int LENGTH_WORD = 4;
	/** Number of command characters. */
	protected static final int COMMAND_LENGTH = LENGTH_WORD;

	/** False. */
	public static final int FALSE = 0;
	/** True. */
	public static final int TRUE = 1;

	/** Command prefix. */
	public static final String kDNewtonDock = "newtdock";
	protected static final byte[] kDNewtonDockBytes = kDNewtonDock.getBytes();
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

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.protocol.IDockCommand#getCommand()
	 */
	public String getCommand() {
		return command;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.protocol.IDockCommand#getLength()
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
