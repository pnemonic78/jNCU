package net.sf.jncu.protocol;

import net.sf.jncu.protocol.v2_0.session.DRequestToDock;
import net.sf.jncu.protocol.v2_0.session.DockCommandSession;

/**
 * Docking command factory.
 * 
 * @author moshew
 */
public class DockCommandFactory {

	private static DockCommandFactory instance;

	/**
	 * Creates a new command factory.
	 */
	protected DockCommandFactory() {
		super();
	}

	/**
	 * Get the factory instance.
	 * 
	 * @return the factory.
	 */
	public static DockCommandFactory getInstance() {
		if (instance == null) {
			instance = new DockCommandFactory();
		}
		return instance;
	}

	/**
	 * Create a new dock command.
	 * 
	 * @param cmdName
	 *            the command name.
	 * @return the command - <tt>null</tt> otherwise.
	 */
	public DockCommand create(byte[] cmdName) {
		return create(new String(cmdName));
	}

	/**
	 * Create a new dock command.
	 * 
	 * @param cmdName
	 *            the command name.
	 * @return the command - <tt>null</tt> otherwise.
	 */
	public DockCommand create(String cmdName) {
		if (DockCommandSession.NewtonToDesktop.kDRequestToDock.equals(cmdName)) {
			return new DRequestToDock();
		}
		return null;
	}
}
