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

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDRestorePatch</tt><br>
 * This command is used to restore the patch backed up with
 * <tt>kDGetPatches</tt>. The Newton returns a <tt>kDResult</tt> of 0 (or an
 * error if appropriate) if the patch wasn't installed. If the patch was
 * installed the Newton restarts.
 * 
 * <pre>
 * 'rpat'
 * length
 * patch
 * </pre>
 * 
 * @author moshew
 */
public class DRestorePatch extends DockCommandToNewton {

	public static final String COMMAND = "rpat";

	private int patch;

	/**
	 * Creates a new command.
	 */
	public DRestorePatch() {
		super(COMMAND);
	}

	/**
	 * Get the patch.
	 * 
	 * @return the patch.
	 */
	public int getPatch() {
		return patch;
	}

	/**
	 * Set the patch.
	 * 
	 * @param patch
	 *            the patch.
	 */
	public void setPatch(int patch) {
		this.patch = patch;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		htonl(getPatch(), data);
	}

}