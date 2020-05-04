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
package net.sf.jncu.cdil.adsp;

import net.sf.jncu.cdil.CDCommandLayer;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.ServiceNotSupportedException;

/**
 * Apple Data Stream Protocol (ADSP) for AppleTalk pipe.
 *
 * @author moshew
 */
public class ADSPPipe extends CDPipe<ADSPPacket, ADSPPacketLayer> {

    protected final String name;
    protected final byte type;

    /**
     * Creates a new ADSP pipe.
     *
     * @param layer the owner layer.
     * @param name  the name of the ADSP connection. This string is what appears
     *              in the Chooser list on the Newton OS device. If you pass
     *              {@code null} for this parameter, the CDIL uses a default name
     *              based on your desktop computer's preferences (for instance, on
     *              a Macintosh, it will use the strings specified in the File
     *              Sharing control panel).
     * @param type  the connection type. This is searched for by the Chooser on
     *              the Newton OS device. If you pass {@code null} for this
     *              parameter, the CDIL uses the type specified by the
     *              Connection/Dock application.
     * @throws ServiceNotSupportedException if the service is not supported.
     */
    public ADSPPipe(CDLayer layer, String name, byte type) throws ServiceNotSupportedException {
        super(layer);
        setName("ADSPPipe-" + getId());
        this.name = name;
        this.type = type;
    }

    @Override
    protected ADSPPacketLayer createPacketLayer() {
        return new ADSPPacketLayer(this);
    }

    @Override
    protected CDCommandLayer<ADSPPacket, ADSPPacketLayer> createCommandLayer(ADSPPacketLayer packetLayer) {
        return null;
    }

}
