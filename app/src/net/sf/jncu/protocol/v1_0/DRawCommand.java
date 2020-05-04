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
package net.sf.jncu.protocol.v1_0;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * Unknown command from Newton with raw data.
 *
 * @author Moshe
 */
public class DRawCommand extends BaseDockCommandFromNewton {

    private byte[] data;

    public DRawCommand(String cmd) {
        super(cmd);
    }

    public DRawCommand(byte[] cmd) {
        super(cmd);
    }

    /**
     * Get the data.
     *
     * @return the data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Set the data.
     *
     * @param data the data.
     */
    protected void setData(byte[] data) {
        this.data = data;
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        final int length = getLength();
        byte[] raw = new byte[length];
        readAll(data, raw);
        setData(raw);
    }

    @Override
    public String toString() {
        return "Raw command: " + getCommand();
    }
}
