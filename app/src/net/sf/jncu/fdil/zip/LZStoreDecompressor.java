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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Lempel-Ziv store decompressor.
 *
 * @author mwaisberg
 */
public class LZStoreDecompressor extends StoreDecompressor {

    private InputStream buffer;
    private LZDecompressor decompressor;
    private Store store;

    /**
     * Creates a new decompressor.
     */
    public LZStoreDecompressor() {
    }

    @Override
    protected InputStream createInflaterStream(InputStream in) {
        return new LZStoreInputStream(in);
    }

    public static void main(String[] args) throws Exception {
        Decompressor decomp = CompanderFactory.getInstance().createDecompressor("TLZStoreDecompressor");

        File f = new File("Packages/Hebrew Font:Prism(48).TLZStoreDecompressor");
        File f2 = new File("Packages/Decompressor/Hebrew.pkg");
        InputStream fin = null;
        InputStream de = null;
        OutputStream fout = null;
        int b;

        try {
            fin = new FileInputStream(f);
            de = decomp.decompress(fin);
            f2.getParentFile().mkdirs();
            fout = new FileOutputStream(f2);
            b = de.read();
            while (b != -1) {
                fout.write(b);
                b = de.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fin != null)
                fin.close();
            if (fout != null)
                fout.close();
        }
        if (f2.length() != decomp.getLength())
            throw new ArrayIndexOutOfBoundsException(decomp.getLength());
    }
}
