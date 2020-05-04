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
package net.sf.jncu.protocol.v1_0.sync;

import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.protocol.DockCommandBidi;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command is sent by the Newton in response to a
 * <tt>kDReturnChangedEntry</tt> command from the desktop. It can also be sent
 * by the desktop.
 *
 * <pre>
 * 'cent'
 * length
 * entry
 * </pre>
 */
public class DChangedEntry extends DockCommandFromNewtonScript<NSOFObject> implements DockCommandBidi {

    /**
     * <tt>kDChangedEntry</tt>
     */
    public static final String COMMAND = "cent";

    private DockCommandToNewton to;

    /**
     * Creates a new command.
     */
    public DChangedEntry() {
        super(COMMAND);
    }

    @Override
    public InputStream getCommandPayload() throws IOException {
        if (to == null) {
            to = new DockCommandToNewtonScript<NSOFObject>(COMMAND) {
            };
        }
        return to.getCommandPayload();
    }

    @Override
    public int getCommandPayloadLength() throws IOException {
        return to.getCommandPayloadLength();
    }
}
