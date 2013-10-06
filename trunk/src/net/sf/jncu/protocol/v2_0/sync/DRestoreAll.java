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
package net.sf.jncu.protocol.v2_0.sync;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

/**
 * This command is sent to the desktop if the user elects to restore all
 * information. <tt>Merge</tt> is {@code 0} to not merge, {@code 1} to merge.
 * 
 * <pre>
 * 'rall'
 * length
 * merge
 * </pre>
 * 
 * @author moshew
 */
public class DRestoreAll extends BaseDockCommandFromNewton {

	/** <tt>kDRestoreAll</tt> */
	public static final String COMMAND = "rall";

	private boolean merge;

	/**
	 * Creates a new command.
	 */
	public DRestoreAll() {
		super(COMMAND);
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
		setMerge(ntohl(data) != FALSE);
	}

	/**
	 * Is merge?
	 * 
	 * @return merge?
	 */
	public boolean isMerge() {
		return merge;
	}

	/**
	 * Set merge.
	 * 
	 * @param merge
	 *            merge?
	 */
	public void setMerge(boolean merge) {
		this.merge = merge;
	}

}
