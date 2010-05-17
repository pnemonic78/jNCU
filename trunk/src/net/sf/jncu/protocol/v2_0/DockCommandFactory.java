package net.sf.jncu.protocol.v2_0;

import net.sf.jncu.protocol.DockCommand;
import net.sf.jncu.protocol.v1_0.DCmdDisconnect;
import net.sf.jncu.protocol.v1_0.DCmdHello;
import net.sf.jncu.protocol.v1_0.DCmdResult;
import net.sf.jncu.protocol.v2_0.session.DCmdDesktopInfo;
import net.sf.jncu.protocol.v2_0.session.DCmdInitiateDocking;
import net.sf.jncu.protocol.v2_0.session.DCmdNewtonInfo;
import net.sf.jncu.protocol.v2_0.session.DCmdNewtonName;
import net.sf.jncu.protocol.v2_0.session.DCmdPassword;
import net.sf.jncu.protocol.v2_0.session.DCmdRequestToDock;
import net.sf.jncu.protocol.v2_0.session.DCmdSetTimeout;
import net.sf.jncu.protocol.v2_0.session.DCmdWhichIcons;

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
		if (DCmdDesktopInfo.COMMAND.equals(cmdName)) {
			return new DCmdDesktopInfo();
		}
		if (DCmdDisconnect.COMMAND.equals(cmdName)) {
			return new DCmdDisconnect();
		}
		if (DCmdHello.COMMAND.equals(cmdName)) {
			return new DCmdHello();
		}
		if (DCmdInitiateDocking.COMMAND.equals(cmdName)) {
			return new DCmdInitiateDocking();
		}
		if (DCmdNewtonInfo.COMMAND.equals(cmdName)) {
			return new DCmdNewtonInfo();
		}
		if (DCmdNewtonName.COMMAND.equals(cmdName)) {
			return new DCmdNewtonName();
		}
		if (DCmdPassword.COMMAND.equals(cmdName)) {
			return new DCmdPassword();
		}
		if (DCmdRequestToDock.COMMAND.equals(cmdName)) {
			return new DCmdRequestToDock();
		}
		if (DCmdResult.COMMAND.equals(cmdName)) {
			return new DCmdResult();
		}
		if (DCmdSetTimeout.COMMAND.equals(cmdName)) {
			return new DCmdSetTimeout();
		}
		if (DCmdWhichIcons.COMMAND.equals(cmdName)) {
			return new DCmdWhichIcons();
		}
		return null;
	}
}
