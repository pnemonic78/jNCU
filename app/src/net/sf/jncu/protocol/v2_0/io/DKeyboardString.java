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
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This command sends a string of characters to the Newton for processing. The
 * characters are 2 byte Unicode characters. If there are an odd number of
 * characters the command should be padded, as usual.
 *
 * <pre>
 * 'kbds'
 * length
 * "string"
 * </pre>
 *
 * @author moshew
 */
public class DKeyboardString extends BaseDockCommandToNewton {

    /**
     * <tt>kDKeyboardString</tt>
     */
    public static final String COMMAND = "kbds";

    private String s;

    /**
     * Creates a new command.
     */
    public DKeyboardString() {
        super(COMMAND);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        writeString(getString(), data);
    }

    /**
     * Get the string.
     *
     * @return the character.
     */
    public String getString() {
        return s;
    }

    /**
     * Set the string.
     *
     * @param s the string.
     */
    public void setString(String s) {
        this.s = s;
    }

}
