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

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * This command is sent in response to a <tt>kDBackupSoup</tt> command if the
 * soup is unchanged from the last backup.
 *
 * <pre>
 * 'ndir'
 * length
 * </pre>
 *
 * @author moshew
 */
public class DSoupNotDirty extends DockCommandFromNewtonBlank {

    /**
     * <tt>kDSoupNotDirty</tt>
     */
    public static final String COMMAND = "ndir";

    /**
     * Creates a new command.
     */
    public DSoupNotDirty() {
        super(COMMAND);
    }

}
