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
package net.sf.jncu.cdil.mnp;

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;
import net.sf.jncu.protocol.v2_0.session.DockingState;

public class EmptyDockingProtocol extends DockingProtocol<MNPPacket, MNPPacketLayer> {

    public EmptyDockingProtocol(EmptyPipe pipe) {
        super(pipe);
    }

    @Override
    public void commandReceived(DockCommandFromNewton command) {
        // ignore
    }

    @Override
    public void commandSent(DockCommandToNewton command) {
        // ignore
    }

    @Override
    protected void validateState(DockingState oldState, DockingState newState) throws BadPipeStateException, PipeDisconnectedException {
        // Pretend that all states are valid.
    }
}
