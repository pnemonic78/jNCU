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

import net.sf.jncu.cdil.CDPacketLayer;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Apple Data Stream Protocol (ADSP) for AppleTalk packet layer.
 *
 * @author Moshe
 */
public class ADSPPacketLayer extends CDPacketLayer<ADSPPacket> {

    /**
     * Constructs a new packet layer.
     *
     * @param pipe the pipe.
     */
    public ADSPPacketLayer(ADSPPipe pipe) {
        super(pipe);
    }

    @Override
    protected InputStream getInput() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected OutputStream getOutput() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected ADSPPacket createPacket(byte[] payload) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected byte[] read() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}
