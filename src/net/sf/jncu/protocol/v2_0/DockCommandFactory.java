package net.sf.jncu.protocol.v2_0;

import java.util.HashMap;
import java.util.Map;

import net.sf.jncu.protocol.DockCommand;
import net.sf.jncu.protocol.v1_0.DCmdDisconnect;
import net.sf.jncu.protocol.v1_0.DCmdHello;
import net.sf.jncu.protocol.v1_0.DCmdResult;
import net.sf.jncu.protocol.v2_0.io.DCmdKeyboardChar;
import net.sf.jncu.protocol.v2_0.io.DCmdKeyboardString;
import net.sf.jncu.protocol.v2_0.session.DCmdDesktopInfo;
import net.sf.jncu.protocol.v2_0.session.DCmdInitiateDocking;
import net.sf.jncu.protocol.v2_0.session.DCmdNewtonInfo;
import net.sf.jncu.protocol.v2_0.session.DCmdNewtonName;
import net.sf.jncu.protocol.v2_0.session.DCmdOperationCanceled;
import net.sf.jncu.protocol.v2_0.session.DCmdOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DCmdOperationDone;
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
	private static final Map<String, Class<? extends DockCommand>> registry = new HashMap<String, Class<? extends DockCommand>>();

	/**
	 * Creates a new command factory.
	 */
	protected DockCommandFactory() {
		super();

		if (registry.isEmpty()) {
			registry.put(DCmdDesktopInfo.COMMAND, DCmdDesktopInfo.class);
			registry.put(DCmdDisconnect.COMMAND, DCmdDisconnect.class);
			registry.put(DCmdHello.COMMAND, DCmdHello.class);
			registry.put(DCmdInitiateDocking.COMMAND, DCmdInitiateDocking.class);
			registry.put(DCmdKeyboardChar.COMMAND, DCmdKeyboardChar.class);
			registry.put(DCmdKeyboardString.COMMAND, DCmdKeyboardString.class);
			registry.put(DCmdNewtonInfo.COMMAND, DCmdNewtonInfo.class);
			registry.put(DCmdNewtonName.COMMAND, DCmdNewtonName.class);
			registry.put(net.sf.jncu.protocol.v1_0.DCmdOperationCanceled.COMMAND, net.sf.jncu.protocol.v1_0.DCmdOperationCanceled.class);
			registry.put(DCmdOperationCanceled.COMMAND, DCmdOperationCanceled.class);
			registry.put(DCmdOperationCanceledAck.COMMAND, DCmdOperationCanceledAck.class);
			registry.put(DCmdOperationDone.COMMAND, DCmdOperationDone.class);
			registry.put(DCmdPassword.COMMAND, DCmdPassword.class);
			registry.put(DCmdRequestToDock.COMMAND, DCmdRequestToDock.class);
			registry.put(DCmdResult.COMMAND, DCmdResult.class);
			registry.put(DCmdSetTimeout.COMMAND, DCmdSetTimeout.class);
			registry.put(DCmdWhichIcons.COMMAND, DCmdWhichIcons.class);
		}
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
		Class<? extends DockCommand> clazz = registry.get(cmdName);

		if (clazz != null) {
			DockCommand cmd = null;
			try {
				cmd = clazz.newInstance();
			} catch (InstantiationException ie) {
				ie.printStackTrace();
			} catch (IllegalAccessException iae) {
				iae.printStackTrace();
			}
			return cmd;
		}

		return null;
	}
}
