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
 * Moves the cursor forward <tt>count</tt> entries from its current position and
 * returns that entry. Returns nil if the cursor is moved past the last entry.
 *
 * <pre>
 * 'move'
 * length
 * cursor id
 * count
 * </pre>
 *
 * @author moshew
 */
public class DCursorMove extends DCursor {

    /**
     * <tt>kDCursorMove</tt>
     */
    public static final String COMMAND = "move";

    private int count;

    /**
     * Creates a new command.
     */
    public DCursorMove() {
        super(COMMAND);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        htonl(getCount(), data);
    }

    /**
     * Get the number of entries to move the cursor.
     *
     * @return the count.
     */
    public int getCount() {
        return count;
    }

    /**
     * Set the number of entries to move the cursor.
     *
     * @param count the count.
     */
    public void setCount(int count) {
        this.count = count;
    }

}
