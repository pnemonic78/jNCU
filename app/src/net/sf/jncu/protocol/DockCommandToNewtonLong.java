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
package net.sf.jncu.protocol;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Docking command to the Newton with a single 4-byte long number.
 *
 * @author moshe
 */
public abstract class DockCommandToNewtonLong extends BaseDockCommandToNewton {

    private int value;

    /**
     * Constructs a new command.
     *
     * @param cmd the command.
     */
    public DockCommandToNewtonLong(String cmd) {
        super(cmd);
        setLength(4);
    }

    /**
     * Constructs a new command.
     *
     * @param cmd the command.
     */
    public DockCommandToNewtonLong(byte[] cmd) {
        super(cmd);
        setLength(4);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        htonl(getValue(), data);
    }

    /**
     * Get the value.
     *
     * @return the value.
     */
    protected int getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value the value.
     */
    protected void setValue(int value) {
        this.value = value;
    }
}
