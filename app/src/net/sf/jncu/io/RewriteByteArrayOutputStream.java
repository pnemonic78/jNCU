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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * <tt>ByteArrayOutputStream</tt> that can move to any valid position within the
 * buffer and override the data.
 *
 * @author Moshe
 */
public class RewriteByteArrayOutputStream extends ByteArrayOutputStream {

    /**
     * The index of the last character.
     */
    protected int limit = 0;

    /**
     * Creates a new byte array output stream.
     */
    public RewriteByteArrayOutputStream() {
        super();
    }

    /**
     * Creates a new byte array output stream.
     *
     * @param size the initial size.
     */
    public RewriteByteArrayOutputStream(int size) {
        super(size);
    }

    /**
     * Skips <code>n</code> bytes from this output stream.
     *
     * @param n the number of bytes to be skipped.
     */
    public void skip(int n) {
        seek(count + n);
    }

    /**
     * Seeks to the position this output stream.
     *
     * @param pos the position.
     */
    public void seek(int pos) {
        if ((pos < 0) || (pos > limit)) {
            throw new IndexOutOfBoundsException();
        }
        count = pos;
    }

    /**
     * Resets the buffer to the marked position. The marked position is 0 unless
     * another position was marked or an offset was specified in the
     * constructor.
     */
    public void seekToEnd() {
        count = limit;
    }

    @Override
    public synchronized int size() {
        return limit;
    }

    @Override
    public synchronized void reset() {
        super.reset();
        limit = 0;
    }

    @Override
    public synchronized void write(int b) {
        super.write(b);
        if (limit < count) {
            limit = count;
        }
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
        super.write(b, off, len);
        if (limit < count) {
            limit = count;
        }
    }

    @Override
    public void close() throws IOException {
        seekToEnd();
        super.close();
    }
}
