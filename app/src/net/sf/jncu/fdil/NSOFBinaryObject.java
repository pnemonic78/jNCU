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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Binary Object.
 * <p>
 * A binary object consist of a series of raw bytes. You may store any data you
 * wish in a binary object. The object may also contain a class symbol
 * identifying the data.
 *
 * @author Moshe
 */
public class NSOFBinaryObject extends NSOFPointer {

    /**
     * Default binary object class.
     */
    public static final NSOFSymbol CLASS_BINARY = new NSOFSymbol("binary");

    private byte[] value;

    /**
     * Constructs a new binary object.
     */
    public NSOFBinaryObject() {
        this(new byte[0]);
    }

    /**
     * Constructs a new binary object.
     *
     * @param value the value.
     */
    public NSOFBinaryObject(byte[] value) {
        super();
        setObjectClass(CLASS_BINARY);
        setValue(value);
    }

    @Override
    public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
        // Number of bytes of data (xlong)
        int numBytesData = XLong.decodeValue(in);
        byte[] data = new byte[numBytesData];

        // Class (object)
        NSOFSymbol symbol = (NSOFSymbol) decoder.inflate(in);
        setObjectClass(symbol);

        // Data
        readAll(in, data);
        setValue(data);
    }

    @Override
    public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
        out.write(NSOF_BINARY);

        byte[] v = getValue();

        // Number of bytes of data (xlong)
        XLong.encode(v.length, out);

        // Class (object)
        encoder.flatten(getObjectClass(), out);

        // Data
        out.write(v);
    }

    /**
     * Get the value.
     *
     * @return the value.
     */
    public byte[] getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value the value.
     */
    public void setValue(byte[] value) {
        if (value == null)
            throw new IllegalArgumentException("non-null value required");
        this.value = value;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        NSOFBinaryObject copy = new NSOFBinaryObject(this.value);
        copy.setObjectClass(this.getObjectClass());
        return copy;
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        byte[] copyValue = new byte[this.value.length];
        System.arraycopy(this.value, 0, copyValue, 0, copyValue.length);
        NSOFBinaryObject copy = new NSOFBinaryObject(copyValue);
        copy.setObjectClass(this.getObjectClass());
        return copy;
    }
}
