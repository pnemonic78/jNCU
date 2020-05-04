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
package net.sf.jncu.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * <tt>ByteArrayOutputStream</tt> that can move to any valid position within the
 * buffer and override the data.
 *
 * @author Moshe
 */
public class RewriteByteArrayOutputStream extends ByteArrayOutputStream {

    /**
     * The index of the last character.
     */
    protected int limit = 0;

    /**
     * Creates a new byte array output stream.
     */
    public RewriteByteArrayOutputStream() {
        super();
    }

    /**
     * Creates a new byte array output stream.
     *
     * @param size the initial size.
     */
    public RewriteByteArrayOutputStream(int size) {
        super(size);
    }

    /**
     * Skips <code>n</code> bytes from this output stream.
     *
     * @param n the number of bytes to be skipped.
     */
    public void skip(int n) {
        seek(count + n);
    }

    /**
     * Seeks to the position this output stream.
     *
     * @param pos the position.
     */
    public void seek(int pos) {
        if ((pos < 0) || (pos > limit)) {
            throw new IndexOutOfBoundsException();
        }
        count = pos;
    }

    /**
     * Resets the buffer to the marked position. The marked position is 0 unless
     * another position was marked or an offset was specified in the
     * constructor.
     */
    public void seekToEnd() {
        count = limit;
    }

    @Override
    public synchronized int size() {
        return limit;
    }

    @Override
    public synchronized void reset() {
        super.reset();
        limit = 0;
    }

    @Override
    public synchronized void write(int b) {
        super.write(b);
        if (limit < count) {
            limit = count;
        }
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
        super.write(b, off, len);
        if (limit < count) {
            limit = count;
        }
    }

    @Override
    public void close() throws IOException {
        seekToEnd();
        super.close();
    }
}
