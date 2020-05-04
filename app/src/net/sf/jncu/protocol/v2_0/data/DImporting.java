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
 * This command is sent to let the Newton know that an import operation is
 * starting. The Newton will display an appropriate message after it gets this
 * message.
 *
 * <pre>
 * 'dimp'
 * length = 0
 * </pre>
 *
 * @author moshew
 */
public class DImporting extends DockCommandToNewtonBlank {

    /**
     * <tt>kDImporting</tt>
     */
    public static final String COMMAND = "dimp";

    /**
     * Constructs a new command.
     */
    public DImporting() {
        super(COMMAND);
    }

}
