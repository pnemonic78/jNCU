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

	protected final DockingFrame frame = new DockingFrame();
	protected final String cmd;
	protected final byte[] cmdBytes;
	private int length;

	/**
	 * Creates a new docking command.
	 * 
	 * @param cmd
	 *            the command.
	 */
	protected DockCommand(String cmd) {
		super();
		if (cmd.length() != COMMAND_LENGTH) {
			throw new IllegalArgumentException("command length must be " + COMMAND_LENGTH);
		}
		this.cmd = cmd;
		this.cmdBytes = cmd.getBytes();
	}

	/**
	 * Creates a new docking command.
	 * 
	 * @param cmdBytes
	 *            the command.
	 */
	protected DockCommand(byte[] cmdBytes) {
		super();
		if (cmdBytes.length != COMMAND_LENGTH) {
			throw new IllegalArgumentException("command length must be " + COMMAND_LENGTH);
		}
		this.cmdBytes = cmdBytes;
		this.cmd = new String(cmdBytes);
	}

	/**
	 * Get the command.
	 * 
	 * @return the command.
	 */
	public String getCommand() {
		return cmd;
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
