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

import net.sf.jncu.newton.os.Store;

import java.io.InputStream;

/**
 * Lempel-Ziv compressor-expander (compander).
 *
 * @author mwaisberg
 */
public class LZStoreCompander extends StoreCompander {

    private InputStream buffer;
    private Decompressor decompressor;
    private Compressor compressor;
    private Store store;
    private int rootId;
    private int chunksId;
    private boolean allocated;

    /**
     * Creates a new compander.
     */
    public LZStoreCompander() {
    }

}
