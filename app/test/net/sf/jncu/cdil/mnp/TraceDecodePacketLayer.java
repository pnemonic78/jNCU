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

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class TraceDecodePacketLayer extends EmptyPacketLayer {

    private final InputStream receivedFromNewton;
    private final InputStream sentToNewton;

    public TraceDecodePacketLayer(MNPPipe pipe, InputStream receivedFromNewton, InputStream sentToNewton) {
        super(pipe);
        setName("TraceDecodePacketLayer-" + getId());
        this.receivedFromNewton = receivedFromNewton;
        this.sentToNewton = new BufferedInputStream(sentToNewton, 1024);
    }

    @Override
    protected InputStream getInput() throws IOException {
        return receivedFromNewton;
    }

    public byte[] readSent() throws IOException {
        return read(sentToNewton);
    }

    @Override
    protected boolean allowAcknowledge(MNPPacket packet) {
        return true;
    }
}
