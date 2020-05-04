/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 *
 * http://sourceforge.net/projects/jncu
 *
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 *
 */
package net.sf.jncu.fdil.zip;

import net.sf.jncu.newton.os.Store;

import java.io.InputStream;

/**
 * Lempel-Ziv relocation store decompressor.
 *
 * @author mwaisberg
 */
public class LZRelocStoreDecompressor extends StoreDecompressor {

    private InputStream buffer;
    private LZDecompressor decompressor;
    private Store store;

    /**
     * Creates a new decompressor.
     */
    public LZRelocStoreDecompressor() {
    }

    @Override
    protected InputStream createInflaterStream(InputStream in) {
        // TODO Auto-generated method stub
        return null;
    }

}
