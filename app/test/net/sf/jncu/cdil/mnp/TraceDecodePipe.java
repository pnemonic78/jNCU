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

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;

import java.io.InputStream;

public class TraceDecodePipe extends EmptyPipe {

    private final InputStream receivedFromNewton;
    private final InputStream sentToNewton;

    public TraceDecodePipe(CDLayer layer, InputStream receivedFromNewton, InputStream sentToNewton) throws PlatformException, ServiceNotSupportedException {
        super(layer);
        setName("TraceDecodePipe-" + getId());
        this.receivedFromNewton = receivedFromNewton;
        this.sentToNewton = sentToNewton;
    }

    @Override
    protected MNPPacketLayer createPacketLayer() {
        return new TraceDecodePacketLayer(this, receivedFromNewton, sentToNewton);
    }

    @Override
    protected DockingProtocol<MNPPacket, MNPPacketLayer> createDockingProtocol() {
        return new TraceDecodeDockingProtocol(this);
    }

    @Override
    protected void disconnectImpl() {
        try {
            // Wait for commands to finish.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        if (!Boolean.getBoolean("debug"))
            super.disconnectImpl();
    }
}
