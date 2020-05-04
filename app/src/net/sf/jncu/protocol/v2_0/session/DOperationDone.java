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
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandBidi;
import net.sf.jncu.protocol.DockCommandToNewtonBlank;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command is sent when an operation is completed. It's only sent in
 * situations where there might be some ambiguity. Currently, there are two
 * situations where this is sent. When the desktop finishes a restore it sends
 * this command. When a sync is finished and there are no sync results
 * (conflicts) to send to the Newton the desktop sends this command.
 *
 * <pre>
 * 'opdn'
 * length = 0
 * </pre>
 */
public class DOperationDone extends DockCommandToNewtonBlank implements DockCommandBidi {

    /**
     * <tt>kDOperationDone</tt>
     */
    public static final String COMMAND = "opdn";

    /**
     * Creates a new command.
     */
    public DOperationDone() {
        super(COMMAND);
    }

    @Override
    public void decode(InputStream data) throws IOException {
        // nothing to decode.
        setLength(BaseDockCommandFromNewton.ntohl(data));
    }

}
