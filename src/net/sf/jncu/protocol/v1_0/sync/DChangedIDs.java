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
package net.sf.jncu.protocol.v1_0.sync;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * This command is sent in response to a <tt>kDGetChangedIDs</tt> command. It
 * returns all the ids with <tt>mod</tt> time &gt; the <tt>last sync</tt> time.
 * If the last sync time is {@code 0}, no changed entries are returned (this
 * would happen on the first sync).
 * 
 * <pre>
 * 'cids'
 * length
 * count
 * array of ids for the soup
 * </pre>
 */
public class DChangedIDs extends DockCommandFromNewton {

	/** <tt>kDChangedIDs</tt> */
	public static final String COMMAND = "cids";

	private final Set<Integer> ids = new TreeSet<Integer>();

	/**
	 * Creates a new command.
	 */
	public DChangedIDs() {
		super(COMMAND);
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
		ids.clear();
		int count = ntohl(data);
		for (int i = 0; i < count; i++) {
			addID(ntohl(data));
		}
	}

	/**
	 * Get the changed IDs.
	 * 
	 * @return the IDs.
	 */
	public Set<Integer> getIDs() {
		return ids;
	}

	/**
	 * Set the changed IDs.
	 * 
	 * @param ids
	 *            the IDs.
	 */
	protected void setIDs(Set<Integer> ids) {
		this.ids.clear();
		this.ids.addAll(ids);
	}

	/**
	 * Add a changed ID.
	 * 
	 * @param id
	 *            the ID.
	 */
	protected void addID(Integer id) {
		this.ids.add(id);
	}
}
