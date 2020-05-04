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
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.v1_0.data.DSetCurrentSoup;

/**
 * This command is like a combination of <tt>kDSetCurrentSoup</tt> and
 * <tt>kDGetChangedInfo</tt>. It sets the current soup -- see
 * <tt>kDSetCurrentSoup</tt> for details. A <tt>kDSoupInfo</tt> or
 * <tt>kDRes</tt> command is sent by the Newton in response.
 * 
 * <pre>
 * 'ssgi'
 * length
 * soup name
 * </pre>
 * 
 * @author moshew
 */
public class DSetSoupGetInfo extends DSetCurrentSoup {

	/** <tt>kDSetSoupGetInfo</tt> */
	public static final String COMMAND = "ssgi";

	/**
	 * Creates a new command.
	 */
	public DSetSoupGetInfo() {
		super(COMMAND);
	}

}
