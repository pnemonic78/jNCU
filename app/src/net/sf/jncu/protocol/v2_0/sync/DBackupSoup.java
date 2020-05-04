/*
 * Copyright 2010, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jncu.protocol.v2_0.sync;

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command is used to backup a soup. The result is a series of commands
 * that includes all entries changed since the last sync time (set by a previous
 * command), all entries with a unique ID greater than the one specified, and
 * the unique ids of all other entries to be used to determine if any entries
 * were deleted. The changed entries are sent with <tt>kDEntry</tt> commands.
 * The unique ids are sent with a <tt>kDBackupIDs</tt> command. A
 * <tt>kDBackupSoupDone</tt> command finishes the sequence. If there are any IDs
 * &lt; {@code 0x7FFF} there could also be a <tt>kDSetBaseID</tt> command. The
 * changed entries and unique ids are sent in unique id sequence. The Newton
 * checks for <tt>kDOperationCanceled</tt> commands occasionally. If the soup
 * hasn't been changed since the last backup a <tt>kDSoupNotDirty</tt> command
 * is sent instead of the ids. A typical sequence could look like this:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th>&#x2194;</th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td>kDBackupSoup</td>
 * <td>&#x2192;</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>&#x2190;</td>
 * <td>kDBackupIDs</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>&#x2190;</td>
 * <td>kDEntry</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>&#x2190;</td>
 * <td>kDEntry</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>&#x2190;</td>
 * <td>kDBackupIDs</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>&#x2190;</td>
 * <td>kDEntry</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>&#x2190;</td>
 * <td>kDSetBaseID</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>&#x2190;</td>
 * <td>kDBackupIDs</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>&#x2190;</td>
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
public class DBackupSoup extends DockCommandToNewtonLong {

    /**
     * <tt>kDBackupSoup</tt>
     */
    public static final String COMMAND = "bksp";

    /**
     * Creates a new command.
     */
    public DBackupSoup() {
        super(COMMAND);
    }

    /**
     * Get the last unique id.
     *
     * @return the id.
     */
    public int getId() {
        return getValue();
    }

    /**
     * Set the last unique id.
     *
     * @param id the id.
     */
    public void setId(int id) {
        setValue(id);
    }
}
