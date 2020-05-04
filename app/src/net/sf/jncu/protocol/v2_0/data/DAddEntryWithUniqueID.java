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
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.protocol.v1_0.data.DAddEntry;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * This command is sent when the desktop wants to add an entry to the current
 * soup. The entry is added with the ID specified in the data frame. If the id
 * already exists an error is returned.
 * <p>
 * <em>Warning! This function should only be used during a restore operation. In other situations there's no way of knowing whether the entrie's id is unique. If an entry is added with this command and the entry isn't unique an error is returned.</em>
 *
 * <pre>
 * 'auni'
 * length
 * data ref
 * </pre>
 *
 * @author moshew
 * @see DAddEntry
 */
public class DAddEntryWithUniqueID extends DockCommandToNewtonScript<NSOFFrame> {

    /**
     * <tt>kDAddEntryWithUniqueID</tt>
     */
    public static final String COMMAND = "auni";

    /**
     * Creates a new command.
     */
    public DAddEntryWithUniqueID() {
        super(COMMAND);
    }
}
