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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class implements an input stream filter for reading files in the Simple
 * Store file format.
 *
 * @author mwaisberg
 */
public class SimpleStoreInputStream extends FilterInputStream {

    private int pos = 0;
    private int posSkip = 0;

    /**
     * Creates a new input stream.
     *
     * @param in the actual input stream.
     */
    public SimpleStoreInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (pos == posSkip) {
            if (posSkip == 0)
                posSkip += 12;
            in.skip(8);
            pos += 8;
            posSkip += 1030;
        }
        int count = in.read(b, off, Math.min(1024, len));
        if (count > 0)
            pos += count;
        return count;
    }
}
