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


/**
 * <tt>kDSetStoreGetNames</tt><br>
 * This command is the same as <tt>kDSetCurrentStore</tt> except that it returns
 * the names of the soups on the stores as if you'd send a
 * <tt>kDGetSoupNames</tt> command. It sets the current store on the Newton. A
 * store frame is sent to uniquely identify the store to be set: <br>
 * <code>{<br>
 * &nbsp;&nbsp;name: "foo",<br>
 * &nbsp;&nbsp;kind: "bar",<br>
 * &nbsp;&nbsp;signature: 1234,<br>
 * &nbsp;&nbsp;info: {&lt;info frame&gt;}		// This one is optional<br>
 * }</code>
 * <br>
 * A <tt>kDSoupNames</tt> is sent by the Newton in response.
 * 
 * <pre>
 * 'ssgn'
 * length
 * store frame
 * </pre>
 * 
 * @author moshew
 */
public class DSetStoreGetNames extends DSetCurrentStore {

	public static final String COMMAND = "ssgn";

	/**
	 * Creates a new command.
	 */
	public DSetStoreGetNames() {
		super(COMMAND);
	}

}
