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

import java.util.HashMap;
import java.util.Map;

/**
 * Compander and decompressor object factory.
 *
 * @author mwaisberg
 */
public class CompanderFactory {

    /**
     * Specifies the use of the Lempel-Ziv compressor-expander.
     */
    public static final String COMPANDER_LZ_STORE = "TLZStoreCompander";
    /**
     * Specifies the use of a compander specialized for pixel map data. (A
     * bitmap is a pixel map having a bit depth of 1.) This compander assumes
     * that the data in the VBO is a pixel map and that the pixel map data is
     * 32-bit aligned; that is, the length of the rows in the pixel map is an
     * even multiple of 4 bytes.
     */
    public static final String COMPANDER_PIXELMAP = "TPixelMapCompander";

    private static CompanderFactory instance;
    private static final Map<String, Class<? extends Compander>> registryCompanders = new HashMap<String, Class<? extends Compander>>();
    private static final Map<String, Class<? extends Compressor>> registryCompressors = new HashMap<String, Class<? extends Compressor>>();
    private static final Map<String, Class<? extends Decompressor>> registryDecompressors = new HashMap<String, Class<? extends Decompressor>>();

    /**
     * Creates a new factory.
     */
    private CompanderFactory() {
        register();
    }

    /**
     * Register the classes.
     */
    private void register() {
        registerCompanders();
        registerCompressors();
        registerDecompressors();
    }

    /**
     * Register the companders.
     */
    private void registerCompanders() {
        registryCompanders.put(COMPANDER_LZ_STORE, LZStoreCompander.class);
        registryCompanders.put(COMPANDER_PIXELMAP, PixelMapCompander.class);
        registryCompanders.put("TSimpleStoreCompander", SimpleStoreCompander.class);
        registryCompanders.put("TStoreCompanderWrapper", StoreCompanderWrapper.class);
    }

    /**
     * Register the compressors.
     */
    private void registerCompressors() {
        registryCompressors.put("TCallbackCompressor", CallbackCompressor.class);
        registryCompressors.put("TLZCompressor", LZCompressor.class);
        registryCompressors.put("TUnicodeCompressor", UnicodeCompressor.class);
    }

    /**
     * Register the decompressors.
     */
    private void registerDecompressors() {
        registryDecompressors.put("TArithmeticDecompressor", ArithmeticDecompressor.class);
        registryDecompressors.put("TCallbackDecompressor", CallbackDecompressor.class);
        registryDecompressors.put("TLZDecompressor", LZDecompressor.class);
        registryDecompressors.put("TLZRelocStoreDecompressor", LZRelocStoreDecompressor.class);
        registryDecompressors.put("TLZStoreDecompressor", LZStoreDecompressor.class);
        registryDecompressors.put("TObjTextDecompressor", ObjTextDecompressor.class);
        registryDecompressors.put("TSimpleRelocStoreDecompressor", SimpleRelocStoreDecompressor.class);
        registryDecompressors.put("TSimpleStoreDecompressor", SimpleStoreDecompressor.class);
        registryDecompressors.put("TUnicodeDecompressor", UnicodeDecompressor.class);
        registryDecompressors.put("TZippyDecompressor", ZippyDecompressor.class);
        registryDecompressors.put("TZippyRelocStoreDecompressor", ZippyRelocStoreDecompressor.class);
        registryDecompressors.put("TZippyStoreDecompressor", ZippyStoreDecompressor.class);
    }

    /**
     * Get the factory instance.
     *
     * @return the factory.
     */
    public static CompanderFactory getInstance() {
        if (instance == null) {
            instance = new CompanderFactory();
        }
        return instance;
    }

    /**
     * Creates a compander.
     *
     * @param name the compander name.
     * @return the new compander - {@code null} otherwise.
     */
    public Compander createCompander(String name) {
        Class<? extends Compander> clazz = registryCompanders.get(name);
        if (clazz == null)
            return null;
        try {
            return clazz.newInstance();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a compander.
     *
     * @param blob the BLOB.
     * @return the new compander.
     */
    public Compander createCompander(NSOFLargeBinary blob) {
        return createCompander(blob.getCompanderName());
    }

    /**
     * Creates a compressor.
     *
     * @param name the compressor name.
     * @return the new compressor - {@code null} otherwise.
     */
    public Compressor createCompressor(String name) {
        Class<? extends Compressor> clazz = registryCompressors.get(name);
        if (clazz == null)
            return null;
        try {
            return clazz.newInstance();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a compressor.
     *
     * @param blob the BLOB.
     * @return the new compressor.
     */
    public Compressor createCompressor(NSOFLargeBinary blob) {
        return createCompressor(blob.getCompanderName());
    }

    /**
     * Creates a decompressor.
     *
     * @param name the decompressor name.
     * @return the new decompressor - {@code null} otherwise.
     */
    public Decompressor createDecompressor(String name) {
        Class<? extends Decompressor> clazz = registryDecompressors.get(name);
        if (clazz == null)
            return null;
        try {
            return clazz.newInstance();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a decompressor.
     *
     * @param blob the BLOB.
     * @return the new decompressor.
     */
    public Decompressor createDecompressor(NSOFLargeBinary blob) {
        return createDecompressor(blob.getCompanderName());
    }
}
