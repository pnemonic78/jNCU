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

import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Cursor command.
 *
 * @author moshew
 */
public abstract class DCursor extends BaseDockCommandToNewton {

    private int cursorId;

    /**
     * Creates a new cursor command.
     *
     * @param cmd the command.
     */
    public DCursor(String cmd) {
        super(cmd);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        htonl(getCursorId(), data);
    }

    /**
     * Get the cursor id.
     *
     * @return the cursor id.
     */
    public int getCursorId() {
        return cursorId;
    }

    /**
     * Set the cursor id.
     *
     * @param cursorId the cursor id.
     */
    public void setCursorId(int cursorId) {
        this.cursorId = cursorId;
    }

}
