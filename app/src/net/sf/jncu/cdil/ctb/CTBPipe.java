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
package net.sf.jncu.cdil.ctb;

import net.sf.jncu.cdil.CDCommandLayer;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.ServiceNotSupportedException;

/**
 * Macintosh Communication Tool Serial pipe.
 *
 * @author moshew
 */
public class CTBPipe extends CDPipe<CTBPacket, CTBPacketLayer> {

    protected final String toolName;
    protected final String configString;

    /**
     * Creates a new CTB pipe.
     *
     * @param layer        the owner layer.
     * @param toolName     the name of the communication tool.
     * @param configString a tool-dependent configuration string.
     * @throws ServiceNotSupportedException if the service is not supported.
     */
    public CTBPipe(CDLayer layer, String toolName, String configString) throws ServiceNotSupportedException {
        super(layer);
        setName("CTBPipe-" + getId());
        this.toolName = toolName;
        this.configString = configString;
    }

    @Override
    protected CTBPacketLayer createPacketLayer() {
        return new CTBPacketLayer(this);
    }

    @Override
    protected CDCommandLayer<CTBPacket, CTBPacketLayer> createCommandLayer(CTBPacketLayer packetLayer) {
        return null;
    }

}
