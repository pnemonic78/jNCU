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

import net.sf.jncu.cdil.CDPacketLayer;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Transfer Control Protocol (TCP) packet layer.
 *
 * @author Moshe
 */
public class TCPPacketLayer extends CDPacketLayer<TCPPacket> {

    /**
     * Constructs a new packet layer.
     *
     * @param pipe the pipe.
     */
    public TCPPacketLayer(TCPPipe pipe) {
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
    protected TCPPacket createPacket(byte[] payload) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected byte[] read() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}
