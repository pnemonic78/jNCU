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
