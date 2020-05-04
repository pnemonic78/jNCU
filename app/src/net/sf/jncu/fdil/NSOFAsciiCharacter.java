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
 * Newton Streamed Object Format - Character.
 *
 * @author Moshe
 */
public class NSOFAsciiCharacter extends NSOFCharacter {

    /**
     * Constructs a new character.<br>
     * <em>Reserved for use by decoder!</em>
     */
    public NSOFAsciiCharacter() {
        super();
    }

    /**
     * Constructs a new character.
     *
     * @param value the character.
     */
    public NSOFAsciiCharacter(byte value) {
        super((char) (value & 0xFF));
    }

    /**
     * Constructs a new character.
     *
     * @param value the character.
     */
    public NSOFAsciiCharacter(char value) {
        super(value);
    }

    @Override
    public String toString() {
        final int value = getValue();
        return "$\\" + HEX[value & 0x000F] + HEX[(value >> 4) & 0x000F];
    }

    @Override
    public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
        // Character code (byte)
        int c = in.read();
        if (c == -1) {
            throw new EOFException();
        }
        setValue((char) (c & 0xFF));
    }

    @Override
    public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
        out.write(NSOF_CHARACTER);
        // Character code (byte)
        out.write(getValue() & 0xFF);
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return new NSOFAsciiCharacter(this.getChar());
    }
}
