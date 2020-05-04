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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Base class for all compressors.
 *
 * @author mwaisberg
 */
public abstract class Compressor {

    /**
     * Creates a new compressor.
     */
    public Compressor() {
    }

    /**
     * Create the inflater stream.
     *
     * @param out the output stream to deflate.
     * @return the inflater input stream.
     */
    protected abstract OutputStream createDeflaterStream(OutputStream out);

    /**
     * Decompress the input.
     *
     * @param out the output stream.
     * @return the inflater stream.
     * @throws IOException if an I/O error occurs.
     */
    public OutputStream compress(OutputStream out) throws IOException {
        return createDeflaterStream(out);
    }

    /**
     * Decompress the input.
     *
     * @param b the input array.
     * @return the inflater stream.
     * @throws IOException if an I/O error occurs.
     */
    public OutputStream compress(byte[] b) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(b.length);
        out.write(b);
        return compress(out);
    }

    /**
     * Decompress the input.
     *
     * @param blob the BLOB.
     * @return the inflater stream.
     * @throws IOException if an I/O error occurs.
     */
    public OutputStream compress(NSOFLargeBinary blob) throws IOException {
        return compress(blob.getValue());
    }

}
