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

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFObject;

/**
 * Applies the specified function to each of the cursor's entries in turn and
 * returns an array of the results. A <tt>kDRefResult</tt> is returned. See
 * MapCursor in NPG.
 * 
 * <pre>
 * 'cmap'
 * length
 * cursor id
 * function
 * </pre>
 * 
 * @author moshew
 */
public class DCursorMap extends DCursor {

	/** <tt>kDCursorMap</tt> */
	public static final String COMMAND = "cmap";

	private NSOFObject function;

	/**
	 * Creates a new command.
	 */
	public DCursorMap() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jncu.protocol.DockCommandToNewton#getCommandData()
	 */
	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		super.writeCommandData(data);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(getFunction(), data);
	}

	/**
	 * Get the function.
	 * 
	 * @return the function.
	 */
	public NSOFObject getFunction() {
		return function;
	}

	/**
	 * Set the function.
	 * 
	 * @param function
	 *            the function.
	 */
	public void setFunction(NSOFObject function) {
		this.function = function;
	}

}
