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
package net.sf.jncu.protocol.v2_1;

import java.util.Map;

import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v2_1.session.DSetStatusText;

/**
 * Docking command factory.
 * 
 * @author moshew
 */
public class DockCommandFactory extends net.sf.jncu.protocol.v2_0.DockCommandFactory {

	private static DockCommandFactory instance;

	/**
	 * Creates a new command factory.
	 */
	protected DockCommandFactory() {
		super();
	}

	@Override
	protected void registerFrom(Map<String, Class<? extends DockCommandFromNewton>> registry) {
		super.registerFrom(registry);
	}

	@Override
	protected void registerTo(Map<String, Class<? extends DockCommandToNewton>> registry) {
		super.registerTo(registry);
		registry.put(DSetStatusText.COMMAND, DSetStatusText.class);
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
