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
package net.sf.jncu.fdil.zip;

import net.sf.jncu.fdil.NSOFLargeBinary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Base class for all decompressors.
 *
 * @author mwaisberg
 */
public abstract class Decompressor {

    protected static final int LENGTH_VERSION = 4;
    protected static final int LENGTH_SIZE = 4;

    private int length = 0;

    /**
     * Creates a new decompressor.
     */
    public Decompressor() {
    }

    /**
     * Create the inflater stream.
     *
     * @param in the input stream to inflate.
     * @return the inflater input stream.
     */
    protected abstract InputStream createInflaterStream(InputStream in);

    /**
     * Decompress the input.
     *
     * @param in the input stream.
     * @return the inflater stream.
     * @throws IOException if an I/O error occurs.
     */
    public InputStream decompress(InputStream in) throws IOException {
        // Skip some header - version (usually 0x00000001).
        in.skip(LENGTH_VERSION);

        // Read some header - uncompressed length.
        int n24 = (in.read() & 0xFF) << 24;
        int n16 = (in.read() & 0xFF) << 16;
        int n08 = (in.read() & 0xFF) << 8;
        int n00 = (in.read() & 0xFF) << 0;
        setLength(n24 | n16 | n08 | n00);

        return createInflaterStream(in);
    }

    /**
     * Decompress the input.
     *
     * @param b the input array.
     * @return the inflater stream.
     * @throws IOException if an I/O error occurs.
     */
    public InputStream decompress(byte[] b) throws IOException {
        return decompress(new ByteArrayInputStream(b));
    }

    /**
     * Decompress the input.
     *
     * @param blob the BLOB.
     * @return the inflater stream.
     * @throws IOException if an I/O error occurs.
     */
    public InputStream decompress(NSOFLargeBinary blob) throws IOException {
        return decompress(blob.getValue());
    }

    /**
     * Set the uncompressed length.
     *
     * @param length the length.
     */
    protected void setLength(int length) {
        this.length = length;
    }

    /**
     * Get the uncompressed length.
     *
     * @return the length.
     */
    public int getLength() {
        return length;
    }
}
