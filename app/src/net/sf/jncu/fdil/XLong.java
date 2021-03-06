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
package net.sf.jncu.fdil;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <tt>xlong</tt>
 *
 * @author Moshe
 */
public class XLong extends NewtonStreamedObjectFormat {

    private int value;

    /**
     * Constructs a new xlong.
     */
    public XLong() {
        super();
    }

    /**
     * Constructs a new xlong.
     *
     * @param value the value.
     */
    public XLong(int value) {
        super();
        this.value = value;
    }

    @Override
    public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
        setValue(decodeValue(in));
    }

    @Override
    public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
        encode(getValue(), out);
    }

    /**
     * Get the value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value the value.
     */
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XLong) {
            return this.getValue() == ((XLong) obj).getValue();
        }
        return super.equals(obj);
    }

    /**
     * Encode the XLong.
     *
     * @param value the xlong value.
     * @param out   the output stream.
     * @throws IOException if an encoding error occurs.
     */
    public static void encode(int value, OutputStream out) throws IOException {
        if ((value >= 0) && (value < 0xFF)) {
            out.write(value & 0xFF);
        } else {
            out.write(0xFF);
            htonl(value, out);
        }
    }

    /**
     * Decode an <tt>XLong</tt>.
     *
     * @param in the input stream.
     * @return the xlong.
     * @throws IOException if an I/O error occurs.
     */
    public static XLong decode(InputStream in) throws IOException {
        return new XLong(decodeValue(in));
    }

    /**
     * Decode an <tt>XLong</tt>.
     *
     * @param in the input stream.
     * @return the xlong.
     * @throws IOException if an I/O error occurs.
     */
    public static int decodeValue(InputStream in) throws IOException {
        // 0 <= value <= 254: unsigned byte
        // else: byte 0xFF followed by signed long
        int l = in.read();
        if (l == -1) {
            throw new EOFException();
        }
        if (l >= 0xFF) {
            l = ntohl(in);
        }
        return l;
    }
}
