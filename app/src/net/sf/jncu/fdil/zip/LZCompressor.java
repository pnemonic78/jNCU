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

import java.io.OutputStream;

/**
 * @author mwaisberg
 */
public class LZCompressor extends Compressor {

    private class Node {
        short start; // +00
        short level; // +02
        short length; // +04
        Node child; // +08
        Node parent; // +0C
        Node sibling; // +10
    }

    /**
     * Creates new compressor.
     */
    public LZCompressor() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected OutputStream createDeflaterStream(OutputStream out) {
        // TODO Auto-generated method stub
        return null;
    }

    private void setHeader() {
    }

    private int headerSize() {
        return 0;
    }

    private void compressChunk() {
    }

    private void compressBlock() {
    }

    private void finish() {
    }
}
