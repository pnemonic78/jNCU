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
package net.sf.jncu.protocol.v2_0.query;

/**
 * Moves the cursor to the previous entry in the set of entries referenced by
 * the cursor and returns the entry. If the cursor is moved before the first
 * entry nil is returned.
 * 
 * <pre>
 * 'prev'
 * length
 * cursor id
 * </pre>
 * 
 * @author moshew
 */
public class DCursorPrev extends DCursor {

	/** <tt>kDCursorPrev</tt> */
	public static final String COMMAND = "prev";

	/**
	 * Creates a new command.
	 */
	public DCursorPrev() {
		super(COMMAND);
	}

}
