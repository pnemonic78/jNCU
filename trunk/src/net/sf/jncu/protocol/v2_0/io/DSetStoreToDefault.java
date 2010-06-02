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
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * This command can be used instead of <tt>kDSetCurrentStore</tt>. It sets the
 * current store to the one the user has picked as the default store (internal
 * or card).
 * 
 * <pre>
 * 'sdef'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DSetStoreToDefault extends DockCommandToNewtonBlank {

	/** <tt>kDSetStoreToDefault</tt> */
	public static final String COMMAND = "sdef";

	/**
	 * Constructs a new command.
	 */
	public DSetStoreToDefault() {
		super(COMMAND);
	}

}
