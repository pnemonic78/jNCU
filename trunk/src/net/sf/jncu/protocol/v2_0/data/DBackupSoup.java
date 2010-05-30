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
 * <tt>kDBackupSoup</tt><br>
 * This command is used to backup a soup. The result is a series of commands
 * that includes all entries changed since the last sync time (set by a previous
 * command), all entries with a unique ID greater than the one specified, and
 * the unique ids of all other entries to be used to determine if any entries
 * were deleted. The changed entries are sent with <tt>kDEntry</tt> commands.
 * The unique ids are sent with a <tt>kDBackupIDs</tt> command. A
 * <tt>kDBackupSoupDone</tt> command finishes the sequence. If there are any IDs
 * > <tt>0x7FFF</tt> there could also be a <tt>kDSetBaseID</tt> command. The
 * changed entries and unique ids are sent in unique id sequence. The Newton
 * checks for <tt>kDOperationCanceled</tt> commands occasionally. If the soup
 * hasn't been changed since the last backup a <tt>kDSoupNotDirty</tt> command
 * is sent instead of the ids. A typical sequence could look like this:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td>kDBackupSoup</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDBackupIDs</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDEntry</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDEntry</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDBackupIDs</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDEntry</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDSetBaseID</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDBackupIDs</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDBackupSoupDone</td>
 * </tr>
 * </table>
 * 
 * <pre>
 * 'bksp'
 * length
 * last unique id
 * </pre>
 * 
 * @author moshew
 */
public class DBackupSoup extends DockCommandToNewton {

	public static final String COMMAND = "bksp";

	private int id;

	/**
	 * Creates a new command.
	 */
	public DBackupSoup(String cmd) {
		super(COMMAND);
	}

	/**
	 * Get the last unique id.
	 * 
	 * @return the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the last unique id.
	 * 
	 * @param id
	 *            the id.
	 */
	public void setId(int id) {
		this.id = id;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		ntohl(getId(), data);
	}
}
