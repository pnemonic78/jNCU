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

import java.util.Map;

import net.sf.jncu.protocol.DockCommand;
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
public class DockCommandFactory extends net.sf.jncu.protocol.v1_0.DockCommandFactory {

	private static DockCommandFactory instance;

	/**
	 * Creates a new command factory.
	 */
	protected DockCommandFactory() {
		super();
	}

	@Override
	protected void register(Map<String, Class<? extends DockCommand>> registry) {
		super.register(registry);
		registry.put(DDesktopInfo.COMMAND, DDesktopInfo.class);
		registry.put(DInitiateDocking.COMMAND, DInitiateDocking.class);
		registry.put(DKeyboardChar.COMMAND, DKeyboardChar.class);
		registry.put(DKeyboardString.COMMAND, DKeyboardString.class);
		registry.put(DNewtonInfo.COMMAND, DNewtonInfo.class);
		registry.put(DNewtonName.COMMAND, DNewtonName.class);
		registry.put(DOperationCanceled.COMMAND, DOperationCanceled.class);
		registry.put(DOperationCanceledAck.COMMAND, DOperationCanceledAck.class);
		registry.put(DOperationDone.COMMAND, DOperationDone.class);
		registry.put(DPassword.COMMAND, DPassword.class);
		registry.put(DRequestToDock.COMMAND, DRequestToDock.class);
		registry.put(DSetTimeout.COMMAND, DSetTimeout.class);
		registry.put(DWhichIcons.COMMAND, DWhichIcons.class);
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

}
