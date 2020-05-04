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

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * This command is sent to request a list of entry IDs for the current soup. It
 * expects to receive a <tt>kDSoupIDs</tt> command in response.
 *
 * <pre>
 * 'gids'
 * length = 0
 * </pre>
 *
 * @author moshew
 * @see DSoupIDs
 */
public class DGetSoupIDs extends DockCommandToNewtonBlank {

    /**
     * <tt>kDGetSoupIDs</tt>
     */
    public static final String COMMAND = "gids";

    /**
     * Creates a new command.
     */
    public DGetSoupIDs() {
        super(COMMAND);
    }

}
