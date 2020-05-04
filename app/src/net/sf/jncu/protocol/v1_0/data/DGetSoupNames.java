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
 * This command is sent when a list of soup names is needed. It expects to
 * receive a <tt>kDSoupNames</tt> command in response.
 *
 * <pre>
 * 'gets'
 * length = 0
 * </pre>
 *
 * @author moshew
 * @see DSoupNames
 */
public class DGetSoupNames extends DockCommandToNewtonBlank {

    /**
     * <tt>kDGetSoupNames</tt>
     */
    public static final String COMMAND = "gets";

    /**
     * Creates a new command.
     */
    public DGetSoupNames() {
        super(COMMAND);
    }

}
