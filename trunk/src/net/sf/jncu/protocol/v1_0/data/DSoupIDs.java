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
package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

/**
 * This command is sent in response to a <tt>kDGetSoupIDs</tt> command. It
 * returns all the IDs from the current soup.
 * 
 * <pre>
 * 'sids'
 * length
 * count
 * array of ids for the soup
 * </pre>
 */
public class DSoupIDs extends BaseDockCommandFromNewton {

	/** <tt>kDSoupIDs</tt> */
	public static final String COMMAND = "sids";

	private final List<Integer> ids = new ArrayList<Integer>();

	/**
	 * Creates a new command.
	 */
	public DSoupIDs() {
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
	 * Get the entry IDs.
	 * 
	 * @return the IDs.
	 */
	public List<Integer> getIDs() {
		return ids;
	}

	/**
	 * Set the entry IDs.
	 * 
	 * @param changedIDs
	 *            the IDs.
	 */
	protected void setIDs(List<Integer> changedIDs) {
		this.ids.clear();
		if (changedIDs != null)
			this.ids.addAll(changedIDs);
	}

	/**
	 * Add an entry ID.
	 * 
	 * @param id
	 *            the ID.
	 */
	protected void addID(Integer id) {
		this.ids.add(id);
	}

}
