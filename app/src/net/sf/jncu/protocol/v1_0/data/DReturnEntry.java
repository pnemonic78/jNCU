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
package net.sf.jncu.protocol.v1_0.data;

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command is sent when the desktop wants to retrieve an entry from the
 * current soup.
 *
 * <pre>
 * 'rete'
 * length
 * id
 * </pre>
 *
 * @author moshew
 */
public class DReturnEntry extends DockCommandToNewtonLong {

    /**
     * <tt>kDReturnEntry</tt>
     */
    public static final String COMMAND = "rete";

    /**
     * Creates a new command.
     */
    public DReturnEntry() {
        super(COMMAND);
    }

    /**
     * Get the entry id.
     *
     * @return the id.
     */
    public int getId() {
        return getValue();
    }

    /**
     * Set the entry id.
     *
     * @param id the id.
     */
    public void setId(int id) {
        setValue(id);
    }
}
