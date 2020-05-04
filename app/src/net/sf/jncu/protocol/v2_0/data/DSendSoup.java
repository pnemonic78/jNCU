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

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * This command requests that all of the entries in a soup be returned to the
 * desktop. The Newton responds with a series of <tt>kDEntry</tt> commands for
 * all the entries in the current soup followed by a <tt>kDBackupSoupDone</tt>
 * command. All of the entries are sent without any request from the desktop (in
 * other words, a series of commands is sent). The process can be interrupted by
 * the desktop by sending a <tt>kDOperationCanceled</tt> command. The cancel
 * will be detected between entries. The <tt>kDEntry</tt> commands are sent
 * exactly as if they had been requested by a <tt>kDReturnEntry</tt> command
 * (they are long padded).
 *
 * <pre>
 * 'snds'
 * length
 * </pre>
 *
 * @author moshew
 */
public class DSendSoup extends DockCommandToNewtonBlank {

    /**
     * <tt>kDSendSoup</tt>
     */
    public static final String COMMAND = "snds";

    /**
     * Creates a new command.
     */
    public DSendSoup() {
        super(COMMAND);
    }

}
