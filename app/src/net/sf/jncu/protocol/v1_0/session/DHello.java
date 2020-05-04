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

import net.sf.jncu.protocol.DockCommandBidi;
import net.sf.jncu.protocol.DockCommandFromNewtonBlank;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.DockCommandToNewtonBlank;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command is sent during long operations to let the Newton or desktop know
 * that the connection hasn't been dropped.
 *
 * <pre>
 * 'helo'
 * length = 0
 * </pre>
 */
public class DHello extends DockCommandFromNewtonBlank implements DockCommandBidi {

    /**
     * <tt>kDHello</tt>
     */
    public static final String COMMAND = "helo";

    private DockCommandToNewton to;

    /**
     * Creates a new command.
     */
    public DHello() {
        super(COMMAND);
    }

    @Override
    public InputStream getCommandPayload() throws IOException {
        if (to == null) {
            to = new DockCommandToNewtonBlank(COMMAND);
        }
        return to.getCommandPayload();
    }

    @Override
    public int getCommandPayloadLength() throws IOException {
        return to.getCommandPayloadLength();
    }
}
