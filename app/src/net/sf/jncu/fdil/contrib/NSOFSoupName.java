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
package net.sf.jncu.fdil.contrib;

import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.SymbolTooLongException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Soup name.
 * <p>
 * A soup name must be shorter than 38 characters:<br>
 * <tt>The userName is limited to 19 characters. You must ensure that the user's soup name doesn't exceed 19 characters if it will be used in multi-user mode.</tt>
 *
 * @author Moshe
 */
public class NSOFSoupName extends NSOFString {

    /**
     * Constructs a new name.
     *
     * @param value the name.
     */
    public NSOFSoupName(String value) {
        super(value);
    }

    @Override
    protected void setValue(String value) {
        if ((value != null) && (value.length() >= 38))
            throw new SymbolTooLongException(value);
        super.setValue(value);
    }

    /**
     * Encode a soup name without using an encoder.
     *
     * @param name the name.
     * @param out  the output.
     * @throws IOException if an I/O error occurs.
     */
    public static void flatten(String name, OutputStream out) throws IOException {
        NSOFSoupName soupName = new NSOFSoupName(name);
        soupName.flatten(out);
    }

    /**
     * Encode a soup name without using an encoder.
     *
     * @param out the output.
     * @throws IOException if an I/O error occurs.
     */
    public void flatten(OutputStream out) throws IOException {
        final String name = getValue();
        try {
            byte[] buf = name.getBytes(CHARSET_UTF16);
            // Bytes [0] and [1] are 0xFE and 0xFF
            if (buf.length >= 2)
                out.write(buf, 2, buf.length - 2);
            out.write(0);
            out.write(0);
        } catch (UnsupportedEncodingException uee) {
            throw new IOException(uee);
        }
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return new NSOFSoupName(this.getValue());
    }
}
