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
package net.sf.jncu.protocol.v2_0.io.win;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDFilters</tt><br>
 * This command returns an array of filters to the Newton. It's sent in response
 * to a <tt>kDGetFilters</tt> command. The filter should be an array of strings
 * which are displayed in the filter pop-up. If the filter array is <tt>nil</tt>
 * no pop-up is displayed. Windows only.
 * 
 * <pre>
 * 'filt'
 * length
 * filter array
 * </pre>
 * 
 * @author moshew
 */
public class DFilters extends DockCommandToNewton {

	public static final String COMMAND = "filt";

	/**
	 * Creates a new command.
	 */
	public DFilters() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO implement me!
	}

}
