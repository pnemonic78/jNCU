/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 * 
 * http://sourceforge.net/projects/jncu
 * 
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 * 
 */
package net.sf.jncu.protocol.v2_0;

import java.util.HashMap;
import java.util.Map;

import net.sf.jncu.protocol.DockCommand;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v1_0.session.DDisconnect;
import net.sf.jncu.protocol.v1_0.session.DHello;
import net.sf.jncu.protocol.v2_0.io.DKeyboardChar;
import net.sf.jncu.protocol.v2_0.io.DKeyboardString;
import net.sf.jncu.protocol.v2_0.session.DDesktopInfo;
import net.sf.jncu.protocol.v2_0.session.DInitiateDocking;
import net.sf.jncu.protocol.v2_0.session.DNewtonInfo;
import net.sf.jncu.protocol.v2_0.session.DNewtonName;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.jncu.protocol.v2_0.session.DPassword;
import net.sf.jncu.protocol.v2_0.session.DRequestToDock;
import net.sf.jncu.protocol.v2_0.session.DSetTimeout;
import net.sf.jncu.protocol.v2_0.session.DWhichIcons;

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
			registry.put(DDesktopInfo.COMMAND, DDesktopInfo.class);
			registry.put(DDisconnect.COMMAND, DDisconnect.class);
			registry.put(DHello.COMMAND, DHello.class);
			registry.put(DInitiateDocking.COMMAND, DInitiateDocking.class);
			registry.put(DKeyboardChar.COMMAND, DKeyboardChar.class);
			registry.put(DKeyboardString.COMMAND, DKeyboardString.class);
			registry.put(DNewtonInfo.COMMAND, DNewtonInfo.class);
			registry.put(DNewtonName.COMMAND, DNewtonName.class);
			registry.put(net.sf.jncu.protocol.v1_0.session.DOperationCanceled.COMMAND, net.sf.jncu.protocol.v1_0.session.DOperationCanceled.class);
			registry.put(DOperationCanceled.COMMAND, DOperationCanceled.class);
			registry.put(DOperationCanceledAck.COMMAND, DOperationCanceledAck.class);
			registry.put(DOperationDone.COMMAND, DOperationDone.class);
			registry.put(DPassword.COMMAND, DPassword.class);
			registry.put(DRequestToDock.COMMAND, DRequestToDock.class);
			registry.put(DResult.COMMAND, DResult.class);
			registry.put(DSetTimeout.COMMAND, DSetTimeout.class);
			registry.put(DWhichIcons.COMMAND, DWhichIcons.class);
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
