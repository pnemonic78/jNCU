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
package net.sf.jncu.cdil.tcp;

import net.sf.jncu.cdil.CDCommandLayer;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.ServiceNotSupportedException;

/**
 * Transfer Control Protocol (TCP) pipe.
 *
 * @author moshew
 */
public class TCPPipe extends CDPipe<TCPPacket, TCPPacketLayer> {

    protected final int port;

    /**
     * Creates a new TCP pipe.
     *
     * @param layer the owner layer.
     * @param port  the TCP port to listen on. Note that once the connection is
     *              made, data transfer actually occurs on a different, randomly
     *              chosen, port. This frees up the port specified in this
     *              parameter for future connections.
     * @throws ServiceNotSupportedException if the service is not supported.
     */
    public TCPPipe(CDLayer layer, int port) throws ServiceNotSupportedException {
        super(layer);
        setName("TCPPipe-" + getId());
        this.port = port;
    }

    @Override
    protected TCPPacketLayer createPacketLayer() {
        return new TCPPacketLayer(this);
    }

    @Override
    protected CDCommandLayer<TCPPacket, TCPPacketLayer> createCommandLayer(TCPPacketLayer packetLayer) {
        return null;
    }

}
