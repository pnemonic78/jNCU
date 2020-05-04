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

import net.sf.jncu.protocol.DockCommandBidi;
import net.sf.jncu.protocol.DockCommandFromNewtonBlank;
import net.sf.jncu.protocol.DockCommandToNewtonBlank;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command is sent from the desktop when the desktop wants to start a sync
 * operation, when both the Newton and the desktop were waiting for the user to
 * specify an operation.
 *
 * <pre>
 * 'ssyn'
 * length = 0
 * </pre>
 *
 * @author moshew
 */
public class DRequestToSync extends DockCommandToNewtonBlank implements DockCommandBidi {

    /**
     * <tt>kDRequestToSync</tt>
     */
    public static final String COMMAND = "ssyn";

    private DockCommandFromNewtonBlank helper;

    /**
     * Creates a new command.
     */
    public DRequestToSync() {
        super(COMMAND);
    }

    @Override
    public void decode(InputStream data) throws IOException {
        if (helper == null)
            helper = new DockCommandFromNewtonBlank(COMMAND) {
            };
        helper.decode(data);
    }
}
