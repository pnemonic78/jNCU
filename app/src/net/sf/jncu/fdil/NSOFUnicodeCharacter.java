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
 * Newton Streamed Object Format - Unicode character.
 *
 * @author Moshe
 */
public class NSOFUnicodeCharacter extends NSOFCharacter {

    /**
     * Default Unicode character class.
     */
    public static final NSOFSymbol CLASS_UNICODE = new NSOFSymbol("uniChar");

    /**
     * Constructs a new Unicode character.<br>
     * <em>Reserved for use by decoder!</em>
     */
    public NSOFUnicodeCharacter() {
        super();
        setObjectClass(CLASS_UNICODE);
    }

    /**
     * Constructs a new Unicode character.
     *
     * @param value the character.
     */
    public NSOFUnicodeCharacter(char value) {
        super();
        setObjectClass(CLASS_UNICODE);
        setValue(value);
    }

    /**
     * Constructs a new Unicode character.
     *
     * @param value the code point.
     */
    public NSOFUnicodeCharacter(int value) {
        super();
        setObjectClass(CLASS_UNICODE);
        setValue(value);
    }

    @Override
    public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
        // High byte of character code (byte)
        int hi = in.read();
        if (hi == -1)
            throw new EOFException();
        // Low byte of character code (byte)
        int lo = in.read();
        if (lo == -1)
            throw new EOFException();
        int c = ((hi & 0xFF) << 8) | ((lo & 0xFF) << 0);
        setValue((char) c);
    }

    @Override
    public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
        out.write(NSOF_UNICODE_CHARACTER);
        int val = getValue() & 0xFFFF;
        // High byte of character code (byte)
        out.write((val >> 8) & 0xFF);
        // Low byte of character code (byte)
        out.write((val >> 0) & 0xFF);
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return new NSOFUnicodeCharacter(this.getChar());
    }
}
