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
package net.sf.jncu.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * Queued input stream. Behaves almost like a pipe, where the queue is the
 * source, and the stream itself is the sink.
 *
 * @author moshew
 */
public class QueuedInputSteam extends InputStream {

    private final Queue<Byte> q;

    /**
     * Creates a new queued input stream.
     *
     * @param queue the source queue.
     */
    public QueuedInputSteam(Queue<Byte> queue) {
        super();
        this.q = queue;
    }

    @Override
    public int read() throws IOException {
        Byte b;
        if (q instanceof BlockingQueue<?>) {
            BlockingQueue<Byte> bq = (BlockingQueue<Byte>) q;
            try {
                b = bq.take();
            } catch (InterruptedException ie) {
                throw new IOException(ie);
            }
        } else {
            b = q.poll();
            if (b == null) {
                return -1;
            }
        }
        return b.intValue() & 0xFF;
    }

    @Override
    public int available() throws IOException {
        return q.size();
    }

    @Override
    public void close() throws IOException {
        super.close();
        q.clear();
    }
}
