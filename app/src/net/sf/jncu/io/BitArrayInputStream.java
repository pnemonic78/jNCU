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
package net.sf.jncu.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Bit input stream.
 *
 * @author moshew
 */
public class BitArrayInputStream extends InputStream {

    /**
     * An array of bits that was provided by the creator of the stream. Elements
     * <code>buf[0]</code> through <code>buf[count-1]</code> are the only bits
     * that can ever be read from the stream; element <code>buf[pos]</code> is
     * the next byte to be read.
     */
    protected int[] buf;

    /**
     * The index of the next character to read from the input stream buffer.
     * This value should always be nonnegative and not larger than the value of
     * <code>count</code>. The next byte to be read from the input stream buffer
     * will be <code>buf[pos]</code>.
     */
    protected int pos = 0;

    /**
     * The currently marked position in the stream. BitArrayInputStream objects
     * are marked at position zero by default when constructed. They may be
     * marked at another position within the buffer by the <code>mark()</code>
     * method. The current buffer position is set to this point by the
     * <code>reset()</code> method.
     * <p>
     * If no mark has been set, then the value of mark is the offset passed to
     * the constructor (or 0 if the offset was not supplied).
     */
    protected int mark = 0;

    /**
     * The index one greater than the last valid character in the input stream
     * buffer. This value should always be nonnegative and not larger than the
     * length of <code>buf</code>. It is one greater than the position of the
     * last byte within <code>buf</code> that can ever be read from the input
     * stream buffer.
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
     * Constructs a new bit input stream that uses <code>buf</code> as its
     * buffer array. The buffer array is not copied. The initial value of
     * <code>pos</code> is <code>0</code> and the initial value of
     * <code>count</code> is the length of <code>buf</code>.
     *
     * @param buf the input buffer.
     */
    public BitArrayInputStream(byte[] buf) {
        int lengthDiv4 = buf.length >> 2; // 4 bytes per integer.
        int lengthMod4 = buf.length & 3;
        int length = lengthDiv4;
        if (lengthMod4 != 0) {
            length++;
        }
        int[] buf32 = new int[length];
        int v;
        int i = 0;
        int j = 0;
        while (i < lengthDiv4) {
            v = buf[j++] & 0xFF;
            v |= (buf[j++] & 0xFF) << 8;
            v |= (buf[j++] & 0xFF) << 16;
            v |= (buf[j++] & 0xFF) << 24;
            buf32[i++] = v;
        }
        if (j < buf.length) {
            v = buf[j++] & 0xFF;
            if (j < buf.length) {
                v |= (buf[j++] & 0xFF) << 8;
                if (j < buf.length) {
                    v |= (buf[j++] & 0xFF) << 16;
                }
            }
            buf32[i] = v;
        }
        this.buf = buf32;
        this.count = buf.length << 3; // 8 bits per byte.
    }

    /**
     * Constructs a new bit input stream that uses <code>buf</code> as its
     * buffer array. The buffer array is not copied. The initial value of
     * <code>pos</code> is <code>0</code> and the initial value of
     * <code>count</code> is the length of <code>buf</code>.
     *
     * @param buf the input buffer.
     */
    public BitArrayInputStream(int[] buf) {
        this.buf = buf;
        this.count = buf.length << 5; // 32 bits per integer.
    }

    /**
     * Reads the next byte of data from this input stream. The value byte is
     * returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned.
     * <p>
     * This <code>read</code> method cannot block.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     * stream has been reached.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public int read() {
        try {
            return read(8);
        } catch (EOFException eofe) {
            return -1;
        }
    }

    /**
     * Skips <code>n</code> bits of input from this input stream. Fewer bits
     * might be skipped if the end of the input stream is reached. The actual
     * number <code>k</code> of bits to be skipped is equal to the smaller of
     * <code>n</code> and <code>count-pos</code>. The value <code>k</code> is
     * added into <code>pos</code> and <code>k</code> is returned.
     *
     * @param n the number of bits to be skipped.
     * @return the actual number of bits skipped.
     */
    @Override
    public synchronized long skip(long n) {
        if (pos + n > count) {
            n = count - pos;
        }
        if (n < 0) {
            return 0;
        }
        pos += n;
        return n;
    }

    /**
     * Returns the number of remaining bits that can be read (or skipped over)
     * from this input stream.
     * <p>
     * The value returned is <code>count&nbsp;- pos</code>, which is the number
     * of bits remaining to be read from the input buffer.
     *
     * @return the number of remaining bits that can be read (or skipped over)
     * from this input stream without blocking.
     */
    @Override
    public synchronized int available() {
        return count - pos;
    }

    /**
     * Tests if this <code>InputStream</code> supports mark/reset. Always
     * returns <code>true</code>.
     */
    @Override
    public boolean markSupported() {
        return true;
    }

    /**
     * Set the current marked position in the stream. BitArrayInputStream
     * objects are marked at position zero by default when constructed. They may
     * be marked at another position within the buffer by this method.
     * <p>
     * If no mark has been set, then the value of the mark is the offset passed
     * to the constructor (or 0 if the offset was not supplied).
     * <p>
     * Note: The <code>readAheadLimit</code> for this class has no meaning.
     */
    @Override
    public void mark(int readAheadLimit) {
        mark = pos;
    }

    /**
     * Resets the buffer to the marked position. The marked position is 0 unless
     * another position was marked or an offset was specified in the
     * constructor.
     */
    @Override
    public synchronized void reset() {
        pos = mark;
    }

    /**
     * Read bits.
     *
     * @param length the length
     * @return the bits.
     * @throws IllegalArgumentException if length negative, or larger than 32 bits.
     * @throws EOFException             if this input stream reaches the end before reading
     *                                  <tt>length</tt> bits.
     */
    public synchronized int read(int length) throws EOFException {
        if (length < 0) {
            throw new IllegalArgumentException("Negative length: " + length);
        }
        if (length > 32) {
            throw new IllegalArgumentException("Length too big: " + length);
        }
        if (pos + length > count) {
            throw new EOFException();
        }

        int index = pos >> 5;
        int bitIndex = pos & 31;
        int lengthLo = Math.min(32 - bitIndex, length);
        int value = (buf[index] >>> bitIndex) & MASK[lengthLo];
        int lengthHi = length - lengthLo;
        if (lengthHi > 0) {
            value |= (buf[index + 1] & MASK[lengthHi]) << lengthLo;
        }

        pos += length;

        return value;
    }

    /**
     * Reads the next bit of data from this input stream. If no bit is available
     * because the end of the stream has been reached, the value <code>-1</code>
     * is returned.
     * <p>
     * This <code>read</code> method cannot block.
     *
     * @return the next bit of data, or <code>-1</code> if the end of the stream
     * has been reached.
     */
    public int readBit() {
        try {
            return read(1);
        } catch (EOFException eofe) {
            return -1;
        }
    }

    /**
     * Read a byte.
     *
     * @return the next 8 bits of this input stream, interpreted as an
     * <code>byte</code>.
     * @throws EOFException if this input stream reaches the end before reading 1 byte.
     */
    public byte readByte() throws EOFException {
        return (byte) read(8);
    }

    /**
     * Read a short integer.
     *
     * @return the next 16 bits of this input stream, interpreted as an
     * <code>short</code>.
     * @throws EOFException if this input stream reaches the end before reading 2 bytes.
     */
    public short readShort() throws EOFException {
        return (short) read(16);
    }

    /**
     * Read an integer.
     *
     * @return the next 32 bits of this input stream, interpreted as an
     * <code>int</code>.
     * @throws EOFException if this input stream reaches the end before reading 4 bytes.
     */
    public int readInt() throws EOFException {
        return read(32);
    }

    /**
     * Read a long integer.
     *
     * @return the next 64 bits of this input stream, interpreted as an
     * <code>long</code>.
     * @throws EOFException if this input stream reaches the end before reading 8 bytes.
     */
    public long readLong() throws EOFException {
        long lo = readInt() & 0xFFFFFFFFL;
        long hi = readInt() & 0xFFFFFFFFL;
        return (hi << 32) | lo;
    }
}
