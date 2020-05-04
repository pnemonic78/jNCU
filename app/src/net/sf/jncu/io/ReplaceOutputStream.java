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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Find and Replace in the output stream.
 *
 * @author mwaisberg
 */
public class ReplaceOutputStream extends BufferedOutputStream {

    protected final byte[] find;
    protected final byte[] replace;

    public ReplaceOutputStream(OutputStream out, byte[] find, byte[] replace) {
        this(out, find, replace, 256);
    }

    public ReplaceOutputStream(OutputStream out, byte[] find, byte[] replace, int size) {
        super(out, Math.max(size, find.length << 1));
        this.find = find;
        this.replace = replace;
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        if (len >= buf.length) {
            /*
             * If the request length exceeds the size of the output buffer,
             * flush the output buffer and then write the data directly. In this
             * way buffered streams will cascade harmlessly.
             */
            flushBuffer();
            out.write(b, off, len);
            return;
        }
        if (len + count > buf.length) {
            flushBuffer();
        }
        System.arraycopy(b, off, buf, count, len);
        count += len;
    }

    @Override
    public synchronized void write(int b) throws IOException {
        if (count >= buf.length) {
            flushBuffer();
        }
        buf[count++] = (byte) b;
    }

    @Override
    public synchronized void flush() throws IOException {
        flushBuffer();
        out.flush();
    }

    private void flushBuffer() throws IOException {
        if (count == 0)
            return;

        // Find all matches.
        final int findLength = find.length;
        final int replaceLength = replace.length;
        int matchStart = 0;
        int matchCount = 0;
        int pos = 0;
        int fpos = pos;
        int written = 0;

        while (pos < count) {
            if (buf[pos] == find[fpos]) {
                if (matchCount == 0)
                    matchStart = pos;
                matchCount++;
                fpos++;

                if (matchCount == findLength) {
                    out.write(buf, 0, matchStart);
                    written += matchStart;
                    out.write(replace, 0, replaceLength);
                    written += replaceLength;
                    matchStart = pos;
                    matchCount = 0;
                }
            } else {
                if (matchCount > 0) {
                    out.write(buf, matchStart, matchCount);
                    written += matchCount;
                }
                out.write(buf[pos]);
                written++;
                matchStart = pos + 1;
                matchCount = 0;
                fpos = 0;
            }
            pos++;
        }

        count -= written;
        if (count > 0)
            System.arraycopy(buf, written, buf, 0, count);
    }
}
