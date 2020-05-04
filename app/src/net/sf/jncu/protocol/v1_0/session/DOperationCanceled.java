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
package net.sf.jncu.protocol.v1_0.session;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandBidi;
import net.sf.jncu.protocol.DockCommandToNewtonBlank;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command is sent when the user cancels an operation. Usually no action is
 * required on the receivers part except to return to the "ready" state.
 *
 * <pre>
 * 'opca'
 * length = 0
 * </pre>
 */
public class DOperationCanceled extends DockCommandToNewtonBlank implements DockCommandBidi {

    /**
     * <tt>kDOperationCanceled</tt>
     */
    public static final String COMMAND = "opca";

    /**
     * Creates a new command.
     */
    public DOperationCanceled() {
        this(COMMAND);
    }

    /**
     * Creates a new command.
     *
     * @param cmd the command.
     */
    protected DOperationCanceled(String cmd) {
        super(cmd);
    }

    @Override
    public void decode(InputStream data) throws IOException {
        // nothing to decode.
        setLength(BaseDockCommandFromNewton.ntohl(data));
    }

}
