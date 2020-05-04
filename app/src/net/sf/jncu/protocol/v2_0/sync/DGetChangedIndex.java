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

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * This command is like <tt>kDGetIndexDescription</tt> except that it only
 * returns the index description if it has been changed since the time set by
 * the <tt>kDLastSyncTime</tt> command. If the index hasn't changed a
 * <tt>kDRes</tt> with {@code 0} is returned.
 *
 * <pre>
 * 'cidx'
 * length
 * </pre>
 *
 * @author moshew
 */
public class DGetChangedIndex extends DockCommandToNewtonBlank {

    /**
     * <tt>kDGetChangedIndex</tt>
     */
    public static final String COMMAND = "cidx";

    /**
     * Creates a new command.
     */
    public DGetChangedIndex() {
        super(COMMAND);
    }

}
