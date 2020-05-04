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

import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.mnp.MNPPipe.MNPState;
import net.sf.jncu.io.NullOutputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EmptyPacketLayer extends MNPSerialPacketLayer {

    private final InputStream nullIn;
    private final OutputStream nullOut;

    public EmptyPacketLayer(MNPPipe pipe) {
        super(pipe, null);
        setName("EmptyPacketLayer-" + getId());
        setTimeout(Integer.MAX_VALUE);
        this.nullIn = new ByteArrayInputStream(new byte[]{});
        this.nullOut = new NullOutputStream();
    }

    @Override
    protected InputStream getInput() throws IOException {
        return nullIn;
    }

    @Override
    protected OutputStream getOutput() throws IOException {
        return nullOut;
    }

    @Override
    protected boolean allowAcknowledge(MNPPacket packet) {
        return false;
    }

    @Override
    public void run() {
        try {
            ((MNPPipe) pipe).setState(MNPState.MNP_HANDSHAKE_LR_LISTEN);
        } catch (PipeDisconnectedException pde) {
            pde.printStackTrace();
        }
        super.run();
    }
}
