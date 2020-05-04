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
 * This command requests the definition of the indexes that should be created
 * for the current soup.
 *
 * <pre>
 * 'gidx'
 * length = 0
 * </pre>
 *
 * @author moshew
 */
public class DGetIndexDescription extends DockCommandToNewtonBlank {

    /**
     * <tt>kDGetIndexDescription</tt>
     */
    public static final String COMMAND = "gind";

    /**
     * Creates a new command.
     */
    public DGetIndexDescription() {
        super(COMMAND);
    }

}
