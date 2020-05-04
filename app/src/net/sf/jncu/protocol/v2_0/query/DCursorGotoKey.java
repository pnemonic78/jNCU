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
package net.sf.jncu.protocol.v2_0.query;

import java.io.IOException;
import java.io.OutputStream;

/**
 * The entry at the specified key location is returned. <tt>Nil</tt> is returned
 * if there is no entry with the specified key.
 *
 * <pre>
 * 'goto'
 * length
 * cursor id
 * key
 * </pre>
 *
 * @author moshew
 */
public class DCursorGotoKey extends DCursor {

    /**
     * <tt>kDCursorGotoKey</tt>
     */
    public static final String COMMAND = "goto";

    private int key;

    /**
     * Creates a new command.
     */
    public DCursorGotoKey() {
        super(COMMAND);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        htonl(getKey(), data);
    }

    /**
     * Get the key location.
     *
     * @return the key.
     */
    public int getKey() {
        return key;
    }

    /**
     * Set the key location.
     *
     * @param key the key.
     */
    public void setKey(int key) {
        this.key = key;
    }

}
