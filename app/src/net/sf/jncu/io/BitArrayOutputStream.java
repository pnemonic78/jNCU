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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Bit array output stream.
 *
 * @author moshew
 */
public class BitArrayOutputStream extends OutputStream {

    /**
     * The buffer where data is stored.
     */
    protected int[] buf;

    /**
     * The number of valid bits in the buffer.
     */
    protected int count;

    protected static final int[] MASK = new int[33];

    static {
        int mask = 0;
        for (int i = 1; i <= 32; i++) {
            mask = (mask << 1) | 1;
            MASK[i] = mask;
        }
    }

    /**
     * Constructs a new bit array output stream. The buffer capacity is
     * initially 32 bytes, though its size increases if necessary.
     */
    public BitArrayOutputStream() {
        this(32 << 3);
    }

    /**
     * Constructs a new bit array output stream.
     *
     * @param size the initial size.
     * @throws IllegalArgumentException if size is negative.
     */
    public BitArrayOutputStream(int size) {
        super();
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        }
        buf = new int[size >> 5];
    }

    /**
     * Writes the specified byte to this bit array output stream.
     *
     * @param b the byte to be written.
     */
    @Override
    public void write(int b) {
        writeByte((byte) b);
    }

    /**
     * Writes the specified byte to this bit array output stream.
     *
     * @param b the byte to be written.
     */
    public void writeByte(byte b) {
        writeBits(b, 8);
    }

    /**
     * Writes the specified short to this bit array output stream.
     *
     * @param s the short to be written.
     */
    public void writeShort(short s) {
        writeBits(s, 16);
    }

    /**
     * Writes the specified integer to this bit array output stream.
     *
     * @param i the integer to be written.
     */
    public void writeInt(int i) {
        writeBits(i, 32);
    }

    /**
     * Writes the specified long to this bit array output stream.
     *
     * @param l the long to be written.
     */
    public void writeLong(long l) {
        writeBits(l, 64);
    }

    /**
     * Returns the current size of the buffer.
     *
     * @return the value of the <code>count</code> field, which is the number of
     * valid bits in this output stream.
     */
    public synchronized int size() {
        return count;
    }

    /**
     * Creates a newly allocated byte array.
     *
     * @return the current contents of this output stream, as a byte array.
     */
    public synchronized byte[] toByteArray() {
        int length = count >> 3;
        if ((count & 7) != 0) {
            length++;
        }
        byte[] copy = new byte[length];

        int lengthDiv32 = (count >> 5);
        int lengthMod32 = (count & 31);
        int value;
        int i = 0;
        int b = 0;
        while (b < lengthDiv32) {
            value = buf[b++];
            copy[i++] = (byte) (value & 0xFF);
            copy[i++] = (byte) ((value >>> 8) & 0xFF);
            copy[i++] = (byte) ((value >>> 16) & 0xFF);
            copy[i++] = (byte) ((value >>> 24) & 0xFF);
        }
        if (lengthMod32 > 0) {
            value = buf[b] & MASK[lengthMod32];

            copy[i++] = (byte) (value & 0xFF);
            if (lengthMod32 > 8) {
                copy[i++] = (byte) ((value >>> 8) & 0xFF);
                if (lengthMod32 > 16) {
                    copy[i++] = (byte) ((value >>> 16) & 0xFF);
                    if (lengthMod32 > 24) {
                        copy[i++] = (byte) ((value >>> 24) & 0xFF);
                    }
                }
            }
        }
        return copy;
    }

    /**
     * Creates a newly allocated integer array.
     *
     * @return the current contents of this output stream, as an int array.
     */
    public synchronized int[] toIntArray() {
        int length = (count >> 5);
        if ((count & 31) != 0) {
            length++;
        }
        return Arrays.copyOf(buf, length);
    }

    /**
     * Resets the <code>count</code> field of this byte array output stream to
     * zero, so that all currently accumulated output in the output stream is
     * discarded. The output stream can be used again, reusing the already
     * allocated buffer space.
     */
    public synchronized void reset() {
        count = 0;
        Arrays.fill(buf, (byte) 0);
    }

    /**
     * Writes the specified bit.
     *
     * @param b the bit to be written.
     */
    public void writeBit(int b) {
        writeBits(b, 1);
    }

    /**
     * Writes the specified bits.
     *
     * @param b      the bits to be written.
     * @param length the number of bits.
     * @throws IllegalArgumentException if length is negative, or count larger than 32.
     */
    public synchronized void writeBits(int b, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Negative length: " + length);
        }
        if (length > 32) {
            throw new IllegalArgumentException("Length too big: " + length);
        }

        int index = count >> 5;

        if (buf.length <= index + 1) {
            int[] newBuf = new int[index << 1];
            System.arraycopy(buf, 0, newBuf, 0, buf.length);
            buf = newBuf;
        }
        if (b == 0) {
            count += length;
            return;
        }

        int startBitIndex = count & 31;
        int loLength = Math.min(length, 32 - startBitIndex);
        // Inject into an existing integer.
        buf[index] |= ((b & MASK[loLength]) << startBitIndex);
        if (startBitIndex + length > 32) {
            // Populate the last integer with whatever is left over.
            buf[index + 1] = (b >>> loLength) & MASK[length - loLength];
        }

        count += length;
    }

    /**
     * Writes the specified bits.
     *
     * @param b      the bits to be written.
     * @param length the number of bits.
     * @throws IllegalArgumentException if length is negative, or larger than 64.
     */
    public synchronized void writeBits(long b, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Negative length: " + length);
        }
        if (length > 64) {
            throw new IllegalArgumentException("Length too big: " + length);
        }
        int lo = (int) (b & 0xFFFFFFFFL);
        if (length <= 32) {
            writeBits(lo, length);
        } else {
            writeBits(lo, 32);
            writeBits((int) (b >>> 32), length - 32);
        }
    }

    /**
     * Writes the specified bits from a stream.
     *
     * @param int the bits to be written.
     * @throws IOException              if an I/O error occurs.
     * @throws IllegalArgumentException if length is negative, or larger than available.
     */
    public void write(BitArrayInputStream in) throws IOException {
        write(in, in.available());
    }

    /**
     * Pipes the specified bits from a stream.
     *
     * @param in     the bits to be written.
     * @param length the number of bits.
     * @throws IOException              if an I/O error occurs.
     * @throws IllegalArgumentException if length is negative, or larger than available.
     */
    public void write(BitArrayInputStream in, int length) throws IOException {
        if (in == null) {
            throw new NullPointerException();
        }
        if (length < 0) {
            throw new IllegalArgumentException("Negative length: " + length);
        }
        int avail = in.available();
        // Nothing to write?
        if (avail == 0) {
            return;
        }
        if (length > avail) {
            throw new IllegalArgumentException("Length too big: " + length);
        }
        avail = length;
        while (avail >= 32) {
            writeInt(in.readInt());
            avail -= 32;
        }
        writeBits(in.read(avail), avail);
    }

}
