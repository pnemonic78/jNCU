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
 * Newton Streamed Object Format - Plain Array.
 *
 * @author Moshe
 */
public class NSOFPlainArray extends NSOFArray {

    /**
     * Default plain array class.
     */
    public static final NSOFSymbol CLASS_PLAIN_ARRAY = new NSOFSymbol("plainArray");

    /**
     * Constructs a new array.
     */
    public NSOFPlainArray() {
        super();
        setObjectClass(CLASS_PLAIN_ARRAY);
    }

    /**
     * Constructs a new array.
     *
     * @param size the initial size, number of slots, of the array.
     */
    public NSOFPlainArray(int size) {
        super(size);
        setObjectClass(CLASS_PLAIN_ARRAY);
    }

    /**
     * Constructs a new array.
     *
     * @param value the value.
     */
    public NSOFPlainArray(NSOFObject[] value) {
        super(value);
        setObjectClass(CLASS_PLAIN_ARRAY);
    }

    @Override
    public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
        setValue((NSOFObject[]) null);

        // Number of slots (xlong)
        int length = XLong.decodeValue(in);
        NSOFObject[] entries = new NSOFObject[length];
        // Slot values in ascending order (objects)
        for (int i = 0; i < length; i++) {
            entries[i] = decoder.inflate(in);
        }
        setValue(entries);
    }

    @Override
    public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
        out.write(NSOF_PLAIN_ARRAY);

        NSOFObject[] slots = getValue();
        if (slots == null) {
            // Number of slots (xlong)
            XLong.encode(0, out);
        } else {
            // Number of slots (xlong)
            final int length = slots.length;
            XLong.encode(length, out);
            // Slot values in ascending order (objects)
            for (int i = 0; i < length; i++) {
                encoder.flatten(slots[i], out);
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        NSOFPlainArray copy = new NSOFPlainArray();
        copy.setValue(this.getValue());
        return copy;
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        NSOFPlainArray copy = new NSOFPlainArray();
        int length = this.length();
        for (int i = 0; i < length; i++)
            copy.add(this.get(i).deepClone());
        return copy;
    }
}
